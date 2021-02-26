package com.particle.game.world.physical.modules;

import com.particle.core.ecs.module.BehaviorModule;

public class ForbidColliderMoudle extends BehaviorModule {
    // 是否禁止与环境的碰撞
    private boolean forbidColliderVoxel = true;
    // 是否禁止与entity的碰撞
    private boolean forbidColliderEntity = true;
    // 是否禁止碰撞后的弹力
    private boolean forbidKinckback = true;

    public boolean isForbidColliderVoxel() {
        return forbidColliderVoxel;
    }

    public void setForbidColliderVoxel(boolean forbidColliderVoxel) {
        this.forbidColliderVoxel = forbidColliderVoxel;
    }

    public boolean isForbidColliderEntity() {
        return forbidColliderEntity;
    }

    public void setForbidColliderEntity(boolean forbidColliderEntity) {
        this.forbidColliderEntity = forbidColliderEntity;
    }

    public boolean isForbidKinckback() {
        return forbidKinckback;
    }

    public void setForbidKinckback(boolean forbidKinckback) {
        this.forbidKinckback = forbidKinckback;
    }
}