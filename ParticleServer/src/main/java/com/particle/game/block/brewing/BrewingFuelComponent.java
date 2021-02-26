package com.particle.game.block.brewing;

import com.particle.core.ecs.component.ECSComponent;

public class BrewingFuelComponent implements ECSComponent {

    /**
     * java版本的燃料，每个烈焰粉可使用20次
     */
    public static final int MAX_FUEL_BE_USED_COUNTS = 20;

    /**
     * 烈焰粉使用总次数，用于java版本
     */
    private int fuelTotal = MAX_FUEL_BE_USED_COUNTS;

    /**
     * 烈焰粉当前使用次数，用于java版本
     */
    private int fuelAmount = 0;

    public int getFuelTotal() {
        return fuelTotal;
    }

    public void setFuelTotal(int fuelTotal) {
        this.fuelTotal = fuelTotal;
    }

    public int getFuelAmount() {
        return fuelAmount;
    }

    public void setFuelAmount(int fuelAmount) {
        this.fuelAmount = fuelAmount;
    }
}
