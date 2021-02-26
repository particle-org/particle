package com.particle.game.entity.attribute.satisfaction;

import com.particle.core.ecs.component.ECSComponent;

public class EntitySatisfactionComponent implements ECSComponent {

    private int maxFoodLevel = 20;

    /**
     * 食物水平，(0-20)
     */
    private int foodLevel = 20;

    private float maxFoodSaturationLevel = 20;

    /**
     * 食物饱和度
     */
    private float foodSaturationLevel = 20;

    /**
     * 饥饿等级（0.0-4.0）
     */
    private float foodExhaustionLevel = 0f;

    public int getMaxFoodLevel() {
        return maxFoodLevel;
    }

    public void setMaxFoodLevel(int maxFoodLevel) {
        this.maxFoodLevel = maxFoodLevel;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public float getMaxFoodSaturationLevel() {
        return maxFoodSaturationLevel;
    }

    public void setMaxFoodSaturationLevel(float maxFoodSaturationLevel) {
        this.maxFoodSaturationLevel = maxFoodSaturationLevel;
    }

    public float getFoodSaturationLevel() {
        return foodSaturationLevel;
    }

    public void setFoodSaturationLevel(float foodSaturationLevel) {
        this.foodSaturationLevel = foodSaturationLevel;
    }

    public float getFoodExhaustionLevel() {
        return foodExhaustionLevel;
    }

    public void setFoodExhaustionLevel(float foodExhaustionLevel) {
        this.foodExhaustionLevel = foodExhaustionLevel;
    }
}
