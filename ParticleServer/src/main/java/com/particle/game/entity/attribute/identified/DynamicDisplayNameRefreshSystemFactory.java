package com.particle.game.entity.attribute.identified;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.world.level.ChunkService;
import com.particle.model.entity.Entity;
import com.particle.model.level.Chunk;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DynamicDisplayNameRefreshSystemFactory implements ECSSystemFactory<DynamicDisplayNameRefreshSystemFactory.DynamicNameTagRefreshSystem> {

    private static final ECSModuleHandler<DynamicDisplayNameModule> DYNAMIC_NAME_TAG_MODULE_HANDLER = ECSModuleHandler.buildHandler(DynamicDisplayNameModule.class);
    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    @Inject
    private ChunkService chunkService;

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{DynamicDisplayNameModule.class, TransformModule.class};
    }

    @Override
    public DynamicNameTagRefreshSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new DynamicNameTagRefreshSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<DynamicNameTagRefreshSystem> getSystemClass() {
        return DynamicNameTagRefreshSystem.class;
    }

    public class DynamicNameTagRefreshSystem implements ECSSystem {

        private Entity entity;
        private DynamicDisplayNameModule dynamicDisplayNameModule;
        private TransformModule transformModule;

        DynamicNameTagRefreshSystem(Entity entity) {
            this.entity = entity;
            this.dynamicDisplayNameModule = DYNAMIC_NAME_TAG_MODULE_HANDLER.getModule(entity);
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            if (!this.dynamicDisplayNameModule.isForceRefresh() && this.dynamicDisplayNameModule.getRefreshInterval() == -1) {
                return;
            }

            long timestamp = System.currentTimeMillis();
            if (this.dynamicDisplayNameModule.isForceRefresh() || timestamp - this.dynamicDisplayNameModule.getLastRefreshTimestamp() > this.dynamicDisplayNameModule.getRefreshInterval()) {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        Chunk chunk = chunkService.getChunk(entity.getLevel(), (int) Math.floor(transformModule.getX() / 16) + i, (int) Math.floor(transformModule.getZ() / 16) + j, false);

                        if (chunk != null) {
                            chunk.getPlayersCollection().getEntitiesViewer().forEach((player -> {
                                dynamicDisplayNameModule.refresh(player);
                            }));
                        }
                    }
                }

                this.dynamicDisplayNameModule.setForceRefresh(false);
                this.dynamicDisplayNameModule.setLastRefreshTimestamp(timestamp);
            }
        }
    }
}
