package com.particle.game.entity.movement.system;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.core.ecs.system.IntervalECSSystem;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.game.world.level.ChunkService;
import com.particle.model.entity.Entity;
import com.particle.model.level.Chunk;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class PositionUpdaterSystemFactory implements ECSSystemFactory<PositionUpdaterSystemFactory.PositionUpdaterSystem> {

    private static Logger LOGGER = LoggerFactory.getLogger(PositionUpdaterSystemFactory.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Inject
    private ChunkService chunkService;

    @Inject
    private EntitySpawnService entitySpawnService;

    public class PositionUpdaterSystem extends IntervalECSSystem {


        private Entity entity;
        private TransformModule transformModule;

        PositionUpdaterSystem(Entity entity) {
            this.entity = entity;
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        protected int getInterval() {
            return 1;
        }

        @Override
        protected void doTick(long deltaTime) {
            if (this.transformModule.needUpdate()) {
                if (!(this.entity instanceof Player)) {
                    // 若生物移动至未加载的区块，则移除该生物
                    Vector3f position = this.transformModule.getPosition();
                    Chunk chunk = chunkService.getChunk(this.entity.getLevel(), position.getFloorX() >> 4, position.getFloorZ() >> 4, false);
                    if (chunk == null) {
                        entitySpawnService.despawn(this.entity);

                        LOGGER.info("Despawn entity at {},{} because in unload chunk!", position.getX(), position.getZ());
                    }
                }


                //广播移动数据
                broadcastServiceProxy.broadcast(this.entity, this.transformModule.getMoveEntityPacketBuilder().build());

                this.transformModule.markUpdated();
            }
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{TransformModule.class, EntityMovementModule.class};
    }

    @Override
    public PositionUpdaterSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new PositionUpdaterSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<PositionUpdaterSystem> getSystemClass() {
        return PositionUpdaterSystem.class;
    }
}
