package com.particle.game.world.physical.system;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.game.world.physical.collider.AABBCollider;
import com.particle.game.world.physical.modules.BoxColliderModule;
import com.particle.game.world.physical.modules.RigibodyModule;
import com.particle.game.world.physical.service.BlockColliderDetectTool;
import com.particle.game.world.physical.service.ColliderService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

public class BoxPhysicalSystemFactory implements ECSSystemFactory<BoxPhysicalSystemFactory.PhysicalSystem> {

    private static final ECSModuleHandler<BoxColliderModule> BOX_COLLIDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(BoxColliderModule.class);
    private static final ECSModuleHandler<RigibodyModule> RIGIBODY_MODULE_HANDLER = ECSModuleHandler.buildHandler(RigibodyModule.class);
    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<EntityMovementModule> ENTITY_MOVEMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMovementModule.class);

    public class PhysicalSystem implements ECSSystem {

        private Entity entity;
        private BoxColliderModule colliderModule;
        private RigibodyModule rigibodyModule;
        private TransformModule transformModule;
        private EntityMovementModule entityMovementModule;

        public PhysicalSystem(Entity entity) {
            this.entity = entity;
            this.colliderModule = BOX_COLLIDER_MODULE_HANDLER.getModule(entity);
            this.rigibodyModule = RIGIBODY_MODULE_HANDLER.getModule(entity);
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
            this.entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            if (this.rigibodyModule.isKinematic()) {
                // 不进行物理计算，但是需要更新collider的motion，保证其他生物与自己的碰撞计算正常
                this.colliderModule.getAABBCollider().setLastPosition(transformModule.getPosition());
                return;
            }

            if (this.rigibodyModule.getLinkedEntity() != null) {
                TransformModule linkedTransform = TRANSFORM_MODULE_HANDLER.getModule(this.rigibodyModule.getLinkedEntity());
                Vector3f position = linkedTransform.getPosition();
                this.colliderModule.getAABBCollider().setLastPosition(position);
                this.colliderModule.getAABBCollider().setLastMovement(new Vector3f(0, 0, 0));
                this.transformModule.setPosition(position);
                return;
            }

            ColliderService.processCollider(entity.getLevel(), entity, colliderModule);
            ColliderService.processEnvironmentForce(entity.getLevel(), entity, rigibodyModule, transformModule, entityMovementModule);
            ColliderService.processColliderForce(colliderModule, entityMovementModule);
            ColliderService.processSpeed(transformModule, entityMovementModule, deltaTime);
            ColliderService.processVoxelHit(entity, colliderModule, transformModule, entityMovementModule);


            // 缓存LastPosition
            AABBCollider collider = colliderModule.getAABBCollider();
            collider.setLastMovement(transformModule.getPosition().subtract(collider.getLastPosition()));
            collider.setLastPosition(transformModule.getPosition());

            boolean standOnBlock = BlockColliderDetectTool.isStandOnBlock(entity, transformModule.getPosition(), colliderModule.getAABBCollider());
            transformModule.setOnGround(standOnBlock);
        }
    }

    @Override
    public PhysicalSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new BoxPhysicalSystemFactory.PhysicalSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<PhysicalSystem> getSystemClass() {
        return PhysicalSystem.class;
    }

    private static final Class<? extends ECSModule>[] REQUEST_SERVICE = new Class[]{TransformModule.class, EntityMovementModule.class, RigibodyModule.class, BoxColliderModule.class};

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return REQUEST_SERVICE;
    }
}
