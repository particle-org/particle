package com.particle.game.block.planting;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.block.planting.components.GrassSeparateModule;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GrassSeparateSystemFactory implements ECSSystemFactory<GrassSeparateSystemFactory.GrassSeparateSystem> {

    @Inject
    private GrassService grassService;

    @Inject
    private PositionService positionService;

    public class GrassSeparateSystem implements ECSSystem {

        private Entity entity;

        public GrassSeparateSystem(Entity entity) {
            this.entity = entity;
        }

        @Override
        public void tick(long deltaTime) {
            if (Math.random() < 0.01) {
                grassService.grow(entity.getLevel(), positionService.getFloorPosition(entity));
            }
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{GrassSeparateModule.class};
    }

    @Override
    public GrassSeparateSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new GrassSeparateSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<GrassSeparateSystem> getSystemClass() {
        return GrassSeparateSystem.class;
    }
}
