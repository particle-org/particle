package com.particle.game.entity.link;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.core.ecs.system.IntervalECSSystem;
import com.particle.game.common.modules.TransformModule;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

public class EntityPassengerSystemFactory implements ECSSystemFactory<EntityPassengerSystemFactory.EntityPassengerSystem> {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<EntityPassengerModule> ENTITY_PASSENGER_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityPassengerModule.class);

    public class EntityPassengerSystem extends IntervalECSSystem {

        private EntityPassengerModule entityPassengerModule;
        private TransformModule transformModule;

        public EntityPassengerSystem(GameObject gameObject) {
            this.transformModule = TRANSFORM_MODULE_ECS_MODULE_HANDLER.getModule(gameObject);
            this.entityPassengerModule = ENTITY_PASSENGER_MODULE_HANDLER.getModule(gameObject);
        }

        @Override
        protected int getInterval() {
            return 9;
        }

        @Override
        protected void doTick(long deltaTime) {
            Entity vehicle = this.entityPassengerModule.getVehicle();
            if (vehicle != null) {
                TransformModule vehicleTransform = TRANSFORM_MODULE_ECS_MODULE_HANDLER.getModule(vehicle);

                if (vehicleTransform != null) {
                    Vector3f targetPosition = vehicleTransform.getPosition();
                    Vector3f currentPosition = this.transformModule.getPosition();

                    if (!currentPosition.equals(targetPosition)) {
                        this.transformModule.setPosition(targetPosition);
                    }
                }
            }
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{EntityPassengerModule.class, TransformModule.class};
    }

    @Override
    public EntityPassengerSystem buildECSSystem(GameObject gameObject) {
        return new EntityPassengerSystem(gameObject);
    }

    @Override
    public Class<EntityPassengerSystem> getSystemClass() {
        return EntityPassengerSystem.class;
    }
}
