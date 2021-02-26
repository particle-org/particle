package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.api.entity.attribute.PositionServiceApi;
import com.particle.api.particle.ParticleServiceApi;
import com.particle.api.sound.SoundServiceApi;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.attribute.BlockAttributeService;
import com.particle.game.block.interactor.BlockWorldService;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.ChunkService;
import com.particle.game.world.level.LevelService;
import com.particle.game.world.nav.NavMeshService;
import com.particle.model.block.Block;
import com.particle.model.block.geometry.BlockGeometry;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.events.level.block.BlockBreakEvent;
import com.particle.model.level.Level;
import com.particle.model.level.LevelEventType;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.CustomParticleType;
import com.particle.model.sound.SoundId;

import javax.inject.Inject;

public class EntityExplosionAction implements IAction {
    @Inject
    private BlockAttributeService blockAttributeService;

    @Inject
    private PositionServiceApi positionServiceApi;

    @Inject
    private ParticleServiceApi particleServiceApi;

    @Inject
    private SoundServiceApi soundServiceApi;

    @Inject
    private BlockService blockService;

    @Inject
    private LevelService levelService;

    @Inject
    private BlockWorldService blockWorldService;

    @Inject
    private NavMeshService navMeshService;

    @Inject
    private ChunkService chunkService;


    // 爆炸范围
    private int scope = 3;
    // 破快block的最大硬度
    private float maxHardness = 0.8F;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Override
    public void onInitialize() {
    }

    @Override
    public EStatus tick(Entity entity) {
        Level level = entity.getLevel();

        Vector3f curPosition = positionServiceApi.getPosition(entity);

        // 播放爆炸粒子效果
        this.particleServiceApi.playParticle(level, CustomParticleType.HUGE_EXPLOSION_EMITTER_NEW, curPosition.add(0, 1, 0));

        this.soundServiceApi.broadcastLevelSound(level, curPosition, SoundId.Explode);

        for (int x = curPosition.getFloorX() - scope; x <= curPosition.getFloorX() + scope; x++) {
            for (int y = curPosition.getFloorY(); y <= curPosition.getFloorY() + scope; y++) {
                for (int z = curPosition.getFloorZ() - scope; z < curPosition.getFloorZ() + scope; z++) {

                    Vector3 position = new Vector3(x, y, z);

                    Block blockAt = levelService.getBlockAt(level, position);
                    if (blockAt == null || blockAt.getType().equals(BlockPrototype.AIR)) {
                        continue;
                    }

                    breakBlock(level, position);
                }
            }
        }

        return EStatus.SUCCESS;
    }

    /**
     * block爆炸效果
     */
    private void breakBlock(Level level, Vector3 position) {
        Block blockAt = this.levelService.getBlockAt(level, position);
        if (blockAt.getType() == BlockPrototype.AIR) {
            return;
        }

        // 过滤掉液体
        if (blockAt.getType().getBlockGeometry().equals(BlockGeometry.EMPTY)) {
            return;
        }

        double hardness = blockAttributeService.getHardness(blockAt);

        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(level);
        blockBreakEvent.setPlayer(null);
        blockBreakEvent.setPosition(position);
        blockBreakEvent.setBlock(blockAt);
        blockBreakEvent.setCaused(BlockBreakEvent.Caused.LEVEL);

        this.eventDispatcher.dispatchEvent(blockBreakEvent);

        if (blockBreakEvent.isCancelled()) {
            return;
        }

        // 播放block被破快的粒子效果
        this.particleServiceApi.playParticle(level, LevelEventType.ParticlesDestroyBlock, blockAt, new Vector3f(position), blockAt.getRuntimeId());

        // 处理预破坏逻辑，计算掉落物
        try {
            boolean state = this.blockWorldService.onBlockPreDestroy(level, null, blockAt, position);
            if (!state) {
                return;
            }
            // 更新方块
            this.levelService.setBlockAt(level, Block.getBlock(BlockPrototype.AIR), position);

            // 更新NavSquare
            this.navMeshService.refreshLayout(level, this.chunkService.indexChunk(level, position), position.getY());
            this.navMeshService.refreshLayout(level, this.chunkService.indexChunk(level, position), position.getY() - 1);

            //处理破坏完成逻辑
            this.blockWorldService.onBlockDestroyed(level, null, blockAt, position);
        } catch (Exception e) {
        }
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void config(String key, Object val) {
        if (key.equalsIgnoreCase("Scope") && val instanceof Integer) {
            this.scope = (Integer) val;
        }

        if (key.equalsIgnoreCase("MaxHardness") && val instanceof Float) {
            this.maxHardness = (Float) val;
        }
    }
}