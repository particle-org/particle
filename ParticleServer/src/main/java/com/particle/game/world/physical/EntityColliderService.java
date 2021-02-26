package com.particle.game.world.physical;

import com.particle.api.physical.IEntityColliderServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.world.physical.collider.AABBCollider;
import com.particle.game.world.physical.collider.PointCollider;
import com.particle.game.world.physical.module.ColliderDetectAlgorithm;
import com.particle.game.world.physical.modules.BoxColliderModule;
import com.particle.game.world.physical.modules.EntityColliderCallbackModule;
import com.particle.game.world.physical.modules.PointColliderModule;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import java.util.function.Consumer;

public class EntityColliderService implements IEntityColliderServiceApi {

    private static final ECSModuleHandler<BoxColliderModule> BOX_COLLIDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(BoxColliderModule.class);
    private static final ECSModuleHandler<PointColliderModule> POINT_COLLIDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(PointColliderModule.class);

    private static final ECSModuleHandler<EntityColliderCallbackModule> ENTITY_COLLIDER_CALLBACK_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityColliderCallbackModule.class);

    /**
     * 绑定碰撞箱
     *
     * @param entity
     * @param center
     * @param size
     */
    @Override
    public void bindAABBBindBox(Entity entity, Vector3f position, Vector3f center, Vector3f size) {
        AABBCollider aabbCollider = new AABBCollider();
        aabbCollider.setCenter(center);
        aabbCollider.setSize(size);
        aabbCollider.setLastPosition(position);

        BOX_COLLIDER_MODULE_HANDLER.bindModule(entity).setAABBCollider(aabbCollider);
    }

    /**
     * 绑定碰撞箱
     *
     * @param entity
     * @param center
     */
    @Override
    public void bindPointBindBox(Entity entity, Vector3f position, Vector3f center) {
        PointCollider pointCollider = new PointCollider();
        pointCollider.setCenter(center);
        pointCollider.setLastPosition(position.subtract(center));

        POINT_COLLIDER_MODULE_HANDLER.bindModule(entity).setPointCollider(pointCollider);
    }

    /**
     * 绑定碰撞检测算法
     *
     * @param entity
     */
    @Override
    public void bindDefaultColliderDetector(Entity entity) {
        BoxColliderModule boxColliderModule = BOX_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (boxColliderModule != null) {
            boxColliderModule.setDetectAlgorithm(ColliderDetectAlgorithm.SAMPLING);
            return;
        }

        PointColliderModule pointColliderModule = POINT_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (pointColliderModule != null) {
            pointColliderModule.setDetectAlgorithm(ColliderDetectAlgorithm.SAMPLING);
            return;
        }
    }

    /**
     * 绑定碰撞检测算法
     *
     * @param entity
     */
    @Override
    public void bindRadiationDetectionComponent(Entity entity) {
        BoxColliderModule boxColliderModule = BOX_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (boxColliderModule != null) {
            boxColliderModule.setDetectAlgorithm(ColliderDetectAlgorithm.SWEPT_VOLUME);
            return;
        }

        PointColliderModule pointColliderModule = POINT_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (pointColliderModule != null) {
            pointColliderModule.setDetectAlgorithm(ColliderDetectAlgorithm.SWEPT_VOLUME);
            return;
        }
    }

    @Override
    public Vector3f getAABBBindBoxCenter(Entity entity) {
        BoxColliderModule boxColliderModule = BOX_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (boxColliderModule != null) {
            return boxColliderModule.getAABBCollider().getCenter();
        }
        return null;
    }

    @Override
    public Vector3f getAABBBindBoxSize(Entity entity) {
        BoxColliderModule boxColliderModule = BOX_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (boxColliderModule != null) {
            return boxColliderModule.getAABBCollider().getSize();
        }
        return null;
    }

    /**
     * 更新位置，用于tp
     *
     * @param entity
     * @param lastPosition
     */
    public void updateLastPosition(Entity entity, Vector3f lastPosition) {
        BoxColliderModule boxColliderModule = BOX_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (boxColliderModule != null) {
            boxColliderModule.getAABBCollider().setLastPosition(lastPosition);
            boxColliderModule.getAABBCollider().setLastMovement(new Vector3f(0, 0, 0));
            return;
        }

        PointColliderModule pointColliderModule = POINT_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (pointColliderModule != null) {
            pointColliderModule.getPointCollider().setLastPosition(lastPosition.subtract(pointColliderModule.getPointCollider().getCenter()));
            pointColliderModule.getPointCollider().setLastMovement(new Vector3f(0, 0, 0));
            return;
        }
    }

    public void removeCollider(Entity entity) {
        BOX_COLLIDER_MODULE_HANDLER.removeModule(entity);
        POINT_COLLIDER_MODULE_HANDLER.removeModule(entity);
    }

    public void updateColliderEntityCallback(Entity entity, Consumer<Entity> callback) {
        EntityColliderCallbackModule entityColliderEntityModule = ENTITY_COLLIDER_CALLBACK_MODULE_HANDLER.bindModule(entity);

        entityColliderEntityModule.setOnColliderWithEntityCallback(callback);
    }

    public void removeColliderEntityCallback(Entity entity) {
        EntityColliderCallbackModule module = ENTITY_COLLIDER_CALLBACK_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            module.setOnColliderWithEntityCallback(null);
        }
    }

    public void updateColliderBlockCallback(Entity entity, Consumer<Vector3f> callback) {
        EntityColliderCallbackModule entityColliderCallbackModule = ENTITY_COLLIDER_CALLBACK_MODULE_HANDLER.bindModule(entity);

        entityColliderCallbackModule.setOnColliderWithBlockCallback(callback);
    }
}
