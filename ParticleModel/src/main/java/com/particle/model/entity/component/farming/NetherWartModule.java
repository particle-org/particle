package com.particle.model.entity.component.farming;

import com.particle.core.ecs.module.BehaviorModule;

public class NetherWartModule extends BehaviorModule {

    private int maxGrowMeta;
    private boolean removeTileEntityAfterMature;

    public NetherWartModule(int maxGrowMeta, boolean removeTileEntityAfterMature) {
        this.maxGrowMeta = maxGrowMeta;
        this.removeTileEntityAfterMature = removeTileEntityAfterMature;
    }

    public NetherWartModule() {
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
