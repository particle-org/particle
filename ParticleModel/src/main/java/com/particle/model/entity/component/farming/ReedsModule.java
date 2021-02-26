package com.particle.model.entity.component.farming;

import com.particle.core.ecs.module.BehaviorModule;

public class ReedsModule extends BehaviorModule {

    private int reedsFactor;

    public int getReedsFactor() {
        return reedsFactor;
    }

    public void setReedsFactor(int reedsFactor) {
        this.reedsFactor = reedsFactor;
    }
}
