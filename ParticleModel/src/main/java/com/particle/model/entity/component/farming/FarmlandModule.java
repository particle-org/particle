package com.particle.model.entity.component.farming;

import com.particle.core.ecs.module.BehaviorModule;

public class FarmlandModule extends BehaviorModule {

    private int farmlandFactor;

    public int getFarmlandFactor() {
        return farmlandFactor;
    }

    public void setFarmlandFactor(int farmlandFactor) {
        this.farmlandFactor = farmlandFactor;
    }
}
