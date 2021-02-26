package com.particle.game.entity.movement.system;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.movement.module.AutoDirectionModule;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.projectile.Arrow;
import com.particle.model.entity.model.projectile.FireworksRocket;
import com.particle.model.entity.model.projectile.Trident;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;

public class AutoDirectionFixSystemFactory implements ECSSystemFactory<AutoDirectionFixSystemFactory.AutoDirectionFixSystem> {

    private static final ECSModuleHandler<EntityMovementModule> ENTITY_MOVEMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMovementModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    public class AutoDirectionFixSystem implements ECSSystem {

        private Entity entity;
        private EntityMovementModule entityMovementModule;
        private TransformModule transformModule;

        public AutoDirectionFixSystem(Entity entity) {
            this.entity = entity;
            this.entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            if (this.transformModule.isOnGround()) {
                return;
            }

            Vector3f speed = this.entityMovementModule.getMotion();

            if (speed.lengthSquared() > 0) {
                Direction speedDirection = new Direction(speed);

                if (this.entity instanceof Arrow || this.entity instanceof Trident || this.entity instanceof FireworksRocket) {
                    speedDirection.setYaw(-speedDirection.getYaw());
                    speedDirection.setPitch(-speedDirection.getPitch());
                }

                this.transformModule.setDirection(speedDirection);
            }
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{TransformModule.class, EntityMovementModule.class, AutoDirectionModule.class};
    }

    @Override
    public AutoDirectionFixSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new AutoDirectionFixSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<AutoDirectionFixSystem> getSystemClass() {
        return AutoDirectionFixSystem.class;
    }
}
