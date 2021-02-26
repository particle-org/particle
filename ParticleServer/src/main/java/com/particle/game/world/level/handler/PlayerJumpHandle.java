package com.particle.game.world.level.handler;

import com.particle.event.dispatcher.EventDispatcher;
import com.particle.event.dispatcher.EventRank;
import com.particle.event.handle.AbstractLevelEventHandle;
import com.particle.game.block.interactor.BlockWorldService;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.events.annotation.EventHandler;
import com.particle.model.events.level.player.PlayerJumpGameEvent;
import com.particle.model.events.level.player.PlayerStepFarmlandEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@EventHandler
@Singleton
public class PlayerJumpHandle extends AbstractLevelEventHandle<PlayerJumpGameEvent> {

    private static final Logger logger = LoggerFactory.getLogger(PlayerJumpHandle.class);

    @Inject
    private LevelService levelService;

    @Inject
    private TileEntityService tileEntityService;

    @Inject
    private PositionService positionService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private BlockWorldService blockWorldService;

    @Override
    public Class<PlayerJumpGameEvent> getListenerEvent() {
        return PlayerJumpGameEvent.class;
    }

    @Override
    public void onHandle(Level level, PlayerJumpGameEvent playerJumpGameEvent) {
        Player player = playerJumpGameEvent.getPlayer();

        Vector3f playerPos = this.positionService.getPosition(player);
        Vector3 downPos = new Vector3((int) Math.floor(playerPos.getX()),
                (int) Math.floor(playerPos.getY() - 1), (int) Math.floor(playerPos.getZ()));
        logger.debug("playerPos:{}, downPos:{}", playerPos, downPos);
        BlockPrototype downBlock = this.levelService.getBlockTypeAt(level, downPos);
        if (downBlock == null) {
            return;
        }

        if (downBlock == BlockPrototype.FARMLAND) {
            TileEntity tileEntity = this.tileEntityService.getEntityAt(level, downPos);
            if (tileEntity != null) {
                // 有生物在上面跳跃，会把耕地变成泥土，同时植物会掉落
                Vector3 targetPosition = positionService.getFloorPosition(tileEntity);

                PlayerStepFarmlandEvent event = new PlayerStepFarmlandEvent(player);
                eventDispatcher.dispatchEvent(event);
                if (!event.isCancelled()) {
                    Block currentBlock = this.levelService.getBlockAt(level, targetPosition);
                    this.replaceFarmlandBlock(level, currentBlock, targetPosition);
                }
            }
        }
    }

    @Override
    public EventRank getEventRank() {
        return EventRank.LOCAL;
    }

    /**
     * 替换耕地为泥土方块
     *
     * @param level
     * @param position
     */
    private void replaceFarmlandBlock(Level level, Block targetBlock, Vector3 position) {
        //处理破坏完成逻辑
        this.blockWorldService.onBlockDestroyed(level, null, targetBlock, position);
        //更新方块
        Block newBlock = Block.getBlock(BlockPrototype.DIRT);
        this.levelService.setBlockAt(level, newBlock, position);
        // 初始化对应的blockEntity
        this.blockWorldService.onBlockPlaced(level, null, newBlock, position);
        // TODO收割
    }
}
