package com.particle.game.entity.spawn;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.model.entity.Entity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AutoRemovedSystemFactory implements ECSSystemFactory<AutoRemovedSystemFactory.AutoRemovedSystem> {

    private static final ECSModuleHandler<AutoRemovedModule> AUTO_REMOVED_MODULE_HANDLER = ECSModuleHandler.buildHandler(AutoRemovedModule.class);

    @Inject
    private EntitySpawnService entitySpawnService;

    public class AutoRemovedSystem implements ECSSystem {

        private Entity entity;
        private AutoRemovedModule autoRemovedModule;

        public AutoRemovedSystem(Entity entity) {
            this.entity = entity;

            this.autoRemovedModule = AUTO_REMOVED_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            if (this.autoRemovedModule.getKeepLiveTime() > 0) {
                this.autoRemovedModule.ttl();
            } else {
                entitySpawnService.despawn(entity);
            }
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{AutoRemovedModule.class};
    }

    @Override
    public AutoRemovedSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new AutoRemovedSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<AutoRemovedSystem> getSystemClass() {
        return AutoRemovedSystem.class;
    }
}
