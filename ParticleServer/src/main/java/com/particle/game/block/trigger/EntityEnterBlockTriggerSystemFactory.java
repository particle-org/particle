package com.particle.game.block.trigger;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.block.trigger.components.EntityEnterBlockTriggerModule;
import com.particle.model.entity.Entity;

import javax.inject.Inject;

public class EntityEnterBlockTriggerSystemFactory implements ECSSystemFactory<EntityEnterBlockTriggerSystemFactory.EntityEnterBlockTriggerSystem> {

    @Inject
    private EntityEnterBlockTriggerService entityEnterBlockTriggerService;

    public class EntityEnterBlockTriggerSystem implements ECSSystem {

        private Entity entity;

        public EntityEnterBlockTriggerSystem(Entity entity) {
            this.entity = entity;
        }

        @Override
        public void tick(long deltaTime) {
            entityEnterBlockTriggerService.doDetect(entity);
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{
                EntityEnterBlockTriggerModule.class
        };
    }

    @Override
    public EntityEnterBlockTriggerSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new EntityEnterBlockTriggerSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<EntityEnterBlockTriggerSystem> getSystemClass() {
        return EntityEnterBlockTriggerSystem.class;
    }
}
