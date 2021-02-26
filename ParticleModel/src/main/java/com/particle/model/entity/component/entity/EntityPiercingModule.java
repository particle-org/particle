package com.particle.model.entity.component.entity;

import com.particle.core.ecs.module.BehaviorModule;

public class EntityPiercingModule extends BehaviorModule {
    private int piercingCount;

    private int maxCount;

    public int getPiercingCount() {
        return piercingCount;
    }

    public void setPiercingCount(int piercingCount) {
        this.piercingCount = piercingCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
