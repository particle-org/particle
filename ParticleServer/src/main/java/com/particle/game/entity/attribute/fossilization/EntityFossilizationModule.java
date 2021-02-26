package com.particle.game.entity.attribute.fossilization;

import com.particle.core.ecs.module.BehaviorModule;

public class EntityFossilizationModule extends BehaviorModule {
    // 是否石化
    private boolean isFossilization = false;
    // 石化结束时间
    private long fossilizationEndTime = -1;

    public boolean isFossilization() {
        return isFossilization;
    }

    public void setFossilization(boolean fossilization) {
        isFossilization = fossilization;
    }

    public long getFossilizationEndTime() {
        return fossilizationEndTime;
    }

    public void setFossilizationEndTime(long fossilizationEndTime) {
        this.fossilizationEndTime = fossilizationEndTime;
    }

    public boolean isEndFossilization() {
        return System.currentTimeMillis() > fossilizationEndTime;
    }

    public void relieveFossilization() {
        isFossilization = false;
        fossilizationEndTime = -1;
    }
}
