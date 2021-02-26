package com.particle.api.entity.attribute;

import com.particle.model.entity.Entity;
import com.particle.model.events.level.entity.EntityDamageType;

public interface HealthServiceAPI {

    void initHealthComponent(Entity entity, float health);

    void initHealthComponent(Entity entity, float health, long minDamageInterval);

    /**
     * 查询生物生命值
     *
     * @param entity 操作的生物
     * @return 查询到的生命值
     */
    float getHealth(Entity entity);

    /**
     * 查询生物最高生命值
     *
     * @param entity 操作的生物
     * @return 查询到的生命值
     */
    float getMaxHealth(Entity entity);

    /**
     * 设置生物生命值
     *
     * @param entity 操作的生物
     * @param health 生命值
     */
    void setHealth(Entity entity, float health);

    /**
     * 设置最大生命值
     *
     * @param entity
     * @param health
     */
    void setMaxHealth(Entity entity, float health);

    /**
     * 获得护盾值
     */
    float getAbsorption(Entity entity);

    /**
     * 设置护盾值
     */
    void setAbsorption(Entity entity, float absorption);

    /**
     * 获得最大护盾值
     */
    float getMaxAbsorption(Entity entity);

    /**
     * 设置最大护盾值
     */
    void setMaxAbsorption(Entity entity, float maxAbsorption);

    /**
     * 检测生物存活状态
     *
     * @param entity 操作的生物
     * @return 是否存活
     */
    boolean isAlive(Entity entity);

    /**
     * 对玩家造成伤害
     *
     * @param entity 操作的生物
     * @param damage 伤害值
     */
    void damageEntity(Entity entity, float damage, EntityDamageType damageType, Entity damager);

    /**
     * 击杀玩家
     *
     * @param entity 操作的生物
     */
    void killEntity(Entity entity, Entity damager);

    /**
     * 重置玩家生命
     *
     * @param entity 操作的生物
     */
    void resetHealth(Entity entity);

    /**
     * 治疗玩家
     *
     * @param entity 操作的生物
     * @param health 生命值
     */
    void healing(Entity entity, float health);

}
