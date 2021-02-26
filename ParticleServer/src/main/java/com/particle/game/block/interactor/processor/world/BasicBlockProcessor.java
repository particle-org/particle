package com.particle.game.block.interactor.processor.world;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.drops.BlockDropService;
import com.particle.game.block.interactor.IBlockWorldProcessor;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.game.entity.service.ItemEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.sound.SoundService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.events.level.block.TiledBlockBreakEvent;
import com.particle.model.item.ItemStack;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import com.particle.model.sound.SoundId;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 基础的方块处理器，判断硬度，掉落原方块
 */
@Singleton
public abstract class BasicBlockProcessor implements IBlockWorldProcessor {

    private static final ECSModuleHandler<EntityMovementModule> ENTITY_MOVEMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMovementModule.class);

    @Inject
    protected ItemEntityService itemEntityService;

    @Inject
    protected TileEntityService tileEntityService;

    @Inject
    protected EntitySpawnService entitySpawnService;

    @Inject
    private SoundService soundService;

    @Inject
    private LevelService levelService;

    @Inject
    private BlockDropService blockDropService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        return true;
    }

    @Override
    public boolean handleBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        boolean state = this.levelService.setBlockAt(player.getLevel(), targetBlock, targetPosition);
        return state;
    }

    @Override
    public boolean onBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        //刷新BlockEntity
        TileEntity entity = tileEntityService.createEntity(targetBlock.getType(), targetPosition);
        if (entity != null) {
            this.entitySpawnService.spawn(level, entity);
        }

        this.soundService.broadcastLevelSound(level, new Vector3f(targetPosition), SoundId.Place, 128);

        return true;
    }

    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        ItemStack item;
        if (this.blockDropService.keepMetaData(targetBlock)) {
            item = ItemStack.getItem(targetBlock.getType().getName(), targetBlock.getMeta(), 1);
        } else {
            item = ItemStack.getItem(targetBlock.getType().getName());
        }

        if (item != null) {
            ItemEntity itemEntity = itemEntityService.createEntity(item, targetPosition, new Vector3f(0, 4f, 0));

            EntityMovementModule entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(itemEntity);
            entityMovementModule.setMotionX(2);

            this.entitySpawnService.spawn(level, itemEntity);
        }

        return true;
    }

    @Override
    public boolean onBlockDestroyed(Level level, Player player, Block targetBlock, Vector3 targetPosition) {

        TiledBlockBreakEvent tiledBlockBreakEvent = new TiledBlockBreakEvent(level);
        tiledBlockBreakEvent.setPlayer(player);
        tiledBlockBreakEvent.setBlock(targetBlock);
        tiledBlockBreakEvent.setPosition(targetPosition);
        tiledBlockBreakEvent.setTileEntity(this.tileEntityService.getEntityAt(level, targetPosition));

        this.eventDispatcher.dispatchEvent(tiledBlockBreakEvent);

        // 去除其对应的tileEntity
        this.entitySpawnService.despawnTileEntity(level, targetPosition);

        return true;
    }
}
