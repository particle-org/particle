package com.particle.game.entity.state;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.model.entity.Entity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntityStateSystemFactory implements ECSSystemFactory<EntityStateSystemFactory.EntityStateSystem> {

    private static final ECSModuleHandler<EntityStateModule> ENTITY_STATE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityStateModule.class);

    @Inject
    private EntityStateService entityStateService;

    public class EntityStateSystem implements ECSSystem {

        private Entity entity;
        private EntityStateModule entityStateModule;

        public EntityStateSystem(Entity entity) {
            this.entity = entity;
            this.entityStateModule = ENTITY_STATE_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            entityStateService.tickEntityState(this.entity, this.entityStateModule);
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{EntityStateModule.class};
    }

    @Override
    public EntityStateSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new EntityStateSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<EntityStateSystem> getSystemClass() {
        return EntityStateSystem.class;
    }
}
