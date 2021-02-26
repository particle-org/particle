package com.particle.api.entity.attribute;

import com.particle.model.entity.Entity;

public interface IHungerServiceApi {
    /**
     * 获取玩家的食物水平
     */
    int getFoodLevel(Entity entity);

    /**
     * 设置玩家的食物水平
     */
    void setFoodLevel(Entity entity, int foodLevel);

    /**
     * reset玩家的食物水平
     */
    void resetFoodLevel(Entity entity);

    /**
     * add玩家的食物水平
     */
    void addFoodLevel(Entity entity, int addValue);

    /**
     * 获得玩家的食物饱和度
     */
    float getFoodSaturationLevel(Entity entity);

    /**
     * 设置玩家的食物饱和度
     */
    void setFoodSaturationLevel(Entity entity, float foodSaturationLevel);

    /**
     * add玩家的食物饱和度
     */
    void addFoodSaturationLevel(Entity entity, float foodSaturationLevel);

    /**
     * 获取玩家的饥饿等级
     */
    float getFoodExhuastionLevel(Entity entity);

    /**
     * 设置玩家的饥饿等级
     */
    void setFoodExhuastionLevel(Entity entity, float foodExhaustionLevel);

    /**
     * add玩家的饥饿等级
     */
    void addFoodExhuastionLevel(Entity entity, float foodExhaustionLevel);
}