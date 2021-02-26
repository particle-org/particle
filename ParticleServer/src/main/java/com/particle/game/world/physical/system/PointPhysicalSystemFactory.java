package com.particle.game.world.physical.system;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.movement.module.EntityMovementModule;
import com.particle.game.world.physical.collider.PointCollider;
import com.particle.game.world.physical.modules.PointColliderModule;
import com.particle.game.world.physical.modules.RigibodyModule;
import com.particle.game.world.physical.service.BlockColliderDetectTool;
import com.particle.game.world.physical.service.ColliderService;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

public class PointPhysicalSystemFactory implements ECSSystemFactory<PointPhysicalSystemFactory.PhysicalSystem> {

    private static final ECSModuleHandler<PointColliderModule> POINT_COLLIDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(PointColliderModule.class);
    private static final ECSModuleHandler<RigibodyModule> RIGIBODY_MODULE_HANDLER = ECSModuleHandler.buildHandler(RigibodyModule.class);
    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<EntityMovementModule> ENTITY_MOVEMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMovementModule.class);

    public class PhysicalSystem implements ECSSystem {

        private Entity entity;
        private PointColliderModule colliderModule;
        private RigibodyModule rigibodyModule;
        private TransformModule transformModule;
        private EntityMovementModule entityMovementModule;


        public PhysicalSystem(Entity entity) {
            this.entity = entity;
            this.colliderModule = POINT_COLLIDER_MODULE_HANDLER.getModule(entity);
            this.rigibodyModule = RIGIBODY_MODULE_HANDLER.getModule(entity);
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
            this.entityMovementModule = ENTITY_MOVEMENT_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            if (this.rigibodyModule.getLinkedEntity() != null) {
                TransformModule linkedTransform = TRANSFORM_MODULE_HANDLER.getModule(this.rigibodyModule.getLinkedEntity());
                Vector3f position = linkedTransform.getPosition();
                this.colliderModule.getPointCollider().setLastPosition(position);
                this.colliderModule.getPointCollider().setLastMovement(new Vector3f(0, 0, 0));
                this.transformModule.setPosition(position);
                return;
            }

            // 如果点碰撞箱子在地上，且没有motion就不模拟物理了，提高性能
            if (transformModule.isOnGround() && colliderModule.getPointCollider().getLastMovement().isZero()) {
                // 检查是否站在地面上
                boolean standOnBlock = BlockColliderDetectTool.isStandOnBlock(entity, transformModule.getPosition(), colliderModule.getPointCollider());
                transformModule.setOnGround(standOnBlock);
            } else {
                ColliderService.processCollider(entity.getLevel(), entity, colliderModule);
                ColliderService.processEnvironmentForce(entity.getLevel(), entity, rigibodyModule, transformModule, entityMovementModule);
                ColliderService.processSpeed(transformModule, entityMovementModule, deltaTime);
                ColliderService.processVoxelHit(entity, colliderModule, transformModule, entityMovementModule);

                // 缓存LastPosition
                PointCollider collider = colliderModule.getPointCollider();
                collider.setLastMovement(transformModule.getPosition().subtract(collider.getLastPosition()));
                collider.setLastPosition(transformModule.getPosition().subtract(collider.getCenter()));

                boolean standOnBlock = BlockColliderDetectTool.isStandOnBlock(entity, transformModule.getPosition(), colliderModule.getPointCollider());
                transformModule.setOnGround(standOnBlock);
            }
        }
    }

    @Override
    public PhysicalSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new PointPhysicalSystemFactory.PhysicalSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<PhysicalSystem> getSystemClass() {
        return PhysicalSystem.class;
    }

    private static final Class<? extends ECSModule>[] REQUEST_SERVICE = new Class[]{TransformModule.class, EntityMovementModule.class, RigibodyModule.class, PointColliderModule.class};

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return REQUEST_SERVICE;
    }
}
