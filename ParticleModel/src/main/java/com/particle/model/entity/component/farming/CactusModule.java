package com.particle.model.entity.component.farming;

import com.particle.core.ecs.module.BehaviorModule;

public class CactusModule extends BehaviorModule {

    private int cactusFactor;

    public int getCactusFactor() {
        return cactusFactor;
    }

    public void setCactusFactor(int cactusFactor) {
        this.cactusFactor = cactusFactor;
    }
}
