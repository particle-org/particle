package com.particle.game.world.physical;

import com.particle.api.physical.ForbidColliderServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.world.physical.modules.ForbidColliderMoudle;
import com.particle.model.entity.Entity;

import javax.inject.Singleton;

@Singleton
public class ForbidColliderService implements ForbidColliderServiceApi {

    private static final ECSModuleHandler<ForbidColliderMoudle> COLLIDER_CONDITION_MOUDLE_HANDLER = ECSModuleHandler.buildHandler(ForbidColliderMoudle.class);

    @Override
    public void initForbidCollider(Entity entity, boolean forbidColliderVoxel, boolean forbidColliderEntity, boolean forbidKinckback) {
        ForbidColliderMoudle forbidColliderMoudle = COLLIDER_CONDITION_MOUDLE_HANDLER.bindModule(entity);
        forbidColliderMoudle.setForbidColliderVoxel(forbidColliderVoxel);
        forbidColliderMoudle.setForbidColliderEntity(forbidColliderEntity);
        forbidColliderMoudle.setForbidKinckback(forbidKinckback);
    }

    public boolean isForbidColliderVoxel(Entity entity) {
        ForbidColliderMoudle forbidColliderMoudle = COLLIDER_CONDITION_MOUDLE_HANDLER.getModule(entity);
        return forbidColliderMoudle != null && forbidColliderMoudle.isForbidColliderVoxel();
    }


    public boolean isForbidColliderEntity(Entity entity) {
        ForbidColliderMoudle forbidColliderMoudle = COLLIDER_CONDITION_MOUDLE_HANDLER.getModule(entity);
        return forbidColliderMoudle != null && forbidColliderMoudle.isForbidColliderEntity();
    }


    public boolean isForbidKinckback(Entity entity) {
        ForbidColliderMoudle forbidColliderMoudle = COLLIDER_CONDITION_MOUDLE_HANDLER.getModule(entity);
        return forbidColliderMoudle != null && forbidColliderMoudle.isForbidKinckback();
    }
}