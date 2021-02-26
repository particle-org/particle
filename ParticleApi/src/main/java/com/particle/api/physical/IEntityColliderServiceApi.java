package com.particle.api.physical;

import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

public interface IEntityColliderServiceApi {
    void bindAABBBindBox(Entity entity, Vector3f position, Vector3f center, Vector3f size);

    void bindPointBindBox(Entity entity, Vector3f position, Vector3f center);

    void bindDefaultColliderDetector(Entity entity);

    void bindRadiationDetectionComponent(Entity entity);

    /**
     * 如果Entit是Box碰撞，返回Entity碰撞的center，否则返回null
     */
    Vector3f getAABBBindBoxCenter(Entity entity);

    /**
     * 如果Entit是Box碰撞，返回Entity的碰撞的size，否则返回null
     */
    Vector3f getAABBBindBoxSize(Entity entity);
}
