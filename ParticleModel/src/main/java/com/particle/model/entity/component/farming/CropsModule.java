package com.particle.model.entity.component.farming;

import com.particle.core.ecs.module.BehaviorModule;

public class CropsModule extends BehaviorModule {

    private int maxGrowMeta;
    private boolean removeTileEntityAfterMature;

    public CropsModule() {
    }

    public void setMaxGrowMeta(int maxGrowMeta) {
        this.maxGrowMeta = maxGrowMeta;
    }

    public void setRemoveTileEntityAfterMature(boolean removeTileEntityAfterMature) {
        this.removeTileEntityAfterMature = removeTileEntityAfterMature;
    }

    public int getMaxGrowMeta() {
        return maxGrowMeta;
    }

    public boolean isRemoveTileEntityAfterMature() {
        return removeTileEntityAfterMature;
    }
}
