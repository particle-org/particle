package com.particle.game.block.furnace;

import com.particle.core.ecs.component.ECSComponent;

public class FurnaceFuelComponent implements ECSComponent {

    /**
     * 燃料的燃烧时间，每个tick递减一
     */
    private short burnTime = 0;

    /**
     * 当前燃料的最大燃烧时间
     */
    private short maxFuelTime = 0;

    public short getBurnTime() {
        return burnTime;
    }

    public void setBurnTime(short burnTime) {
        this.burnTime = burnTime;
    }

    public short getMaxFuelTime() {
        return maxFuelTime;
    }

    public void setMaxFuelTime(short maxFuelTime) {
        this.maxFuelTime = maxFuelTime;
    }
}
