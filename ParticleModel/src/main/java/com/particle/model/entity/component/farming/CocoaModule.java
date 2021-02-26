package com.particle.model.entity.component.farming;

import com.particle.core.ecs.module.BehaviorModule;

public class CocoaModule extends BehaviorModule {

    private long lastGrowTime = System.currentTimeMillis();

    public long getLastGrowTime() {
        return lastGrowTime;
    }

    public void setLastGrowTime(long lastGrowTime) {
        this.lastGrowTime = lastGrowTime;
    }
}
