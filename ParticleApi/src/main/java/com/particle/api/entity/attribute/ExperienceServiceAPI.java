package com.particle.api.entity.attribute;

import com.particle.model.entity.Entity;
import com.particle.model.entity.attribute.EntityAttribute;

public interface ExperienceServiceAPI {

    /**
     * 设置生物经验
     *
     * @param entity 操作的生物
     * @param value
     */
    void setEntityExperience(Entity entity, int value);

    /**
     * 设置生物等级
     *
     * @param entity 操作的生物
     * @param value
     */
    void setEntityExperienceLevel(Entity entity, int value);

    /**
     * 获取生物经验
     *
     * @param entity
     * @return
     */
    int getEntityExperience(Entity entity);

    /**
     * 获取生物等级
     *
     * @param entity
     * @return
     */
    int getEntityLevel(Entity entity);

    /**
     * 给玩家增加经验
     *
     * @param entity  玩家
     * @param exp     经验
     * @param isLevel 等级
     * @return
     */
    boolean addExperience(Entity entity, int exp, boolean isLevel);

    /**
     * 查询玩家经验属性
     *
     * @param entity 操作的生物
     * @return
     */
    EntityAttribute getEntityExpAttribute(Entity entity);

    /**
     * 查询玩家等级属性
     *
     * @param entity 操作的生物
     * @return
     */
    EntityAttribute getEntityExpLevelAttribute(Entity entity);

    /**
     * 每一等级升级需要的经验
     *
     * @param currentLevel
     * @return
     */
    int levelUpgradeNeeded(Entity entity, int currentLevel);
}
