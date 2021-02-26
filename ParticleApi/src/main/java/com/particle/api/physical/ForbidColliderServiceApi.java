package com.particle.api.physical;

import com.particle.model.entity.Entity;

public interface ForbidColliderServiceApi {

    void initForbidCollider(Entity entity, boolean forbidColliderVoxel, boolean forbidColliderEntity, boolean forbidKinckback);

    boolean isForbidColliderVoxel(Entity entity);


    boolean isForbidColliderEntity(Entity entity);


    boolean isForbidKinckback(Entity entity);
}