package com.particle.game.entity.attribute.explevel;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.entity.attribute.EntityAttributeDataComponent;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.attribute.EntityAttributeType;

import java.util.Map;

public class EntityExperienceModule extends ECSModule {

    private static final ECSComponentHandler<EntityAttributeDataComponent> ENTITY_ATTRIBUTE_DATA_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntityAttributeDataComponent.class);
    private static final ECSComponentHandler<EntityExperienceComponent> ENTITY_EXPERIENCE_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntityExperienceComponent.class);
    private static final ECSComponentHandler<EntityExpLevelComponent> ENTITY_EXP_LEVEL_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntityExpLevelComponent.class);

    private static final Class[] REQUIRE_CLASSES = new Class[]{EntityAttributeDataComponent.class, EntityExperienceComponent.class, EntityExpLevelComponent.class};


    private EntityAttributeDataComponent entityAttributeDataComponent;
    private EntityExperienceComponent entityExperienceComponent;
    private EntityExpLevelComponent entityExpLevelComponent;


    // ------------ 经验操作 ------------

    /**
     * 设置玩家经验
     *
     * @param value
     */
    public void setEntityExperience(int value) {
        this.entityExperienceComponent.setExperience(value);

        this.refreshEntityExperienceAttribute();
    }

    /**
     * 给玩家增加经验
     *
     * @param exp
     * @return
     */
    public boolean addExperience(int exp) {
        //计算升级经验
        int currentExp = this.entityExperienceComponent.getExperience();

        int currentLevel = this.entityExpLevelComponent.getLevel();

        int nextLevelExp = this.levelUpgradeNeeded(currentLevel);

        //如果当前经验+获得经验大于升级所需经验，则直接升级
        while (currentExp + exp >= nextLevelExp) {
            // 换算经验和等级
            exp -= (nextLevelExp - currentExp);
            currentExp = 0;
            currentLevel++;

            // 升级过程需要循环判断，保证玩家获得大额经验也能正确处理
            nextLevelExp = this.levelUpgradeNeeded(currentLevel);
        }

        this.entityExperienceComponent.setExperience(currentExp + exp);
        this.entityExperienceComponent.setMaxExperience(nextLevelExp);
        this.entityExpLevelComponent.setLevel(currentLevel);

        this.refreshEntityExperienceAttribute();
        this.refreshEntityExperienceLevelAttribute();

        return true;
    }

    /**
     * 获取玩家经验
     *
     * @return
     */
    public int getEntityExperience() {
        return entityExperienceComponent.getExperience();
    }

    /**
     * 获取经验属性
     *
     * @return
     */
    public EntityAttribute getEntityExpAttribute() {
        EntityAttribute entityAttribute = this.entityAttributeDataComponent.getEntityAttributeMap().get(EntityAttributeType.EXPERIENCE);
        if (entityAttribute == null) {
            entityAttribute = this.refreshEntityExperienceAttribute();
        }

        return entityAttribute;
    }

    public EntityAttribute refreshEntityExperienceAttribute() {
        float expAttributeValue = ((float) this.entityExperienceComponent.getExperience()) / this.entityExperienceComponent.getMaxExperience();

        Map<EntityAttributeType, EntityAttribute> entityAttributeMap = this.entityAttributeDataComponent.getEntityAttributeMap();
        EntityAttribute entityAttribute = entityAttributeMap.get(EntityAttributeType.EXPERIENCE);
        if (entityAttribute == null) {
            entityAttribute = new EntityAttribute(EntityAttributeType.EXPERIENCE,
                    this.entityExperienceComponent.getMinExperience(),
                    this.entityExperienceComponent.getMaxExperience(),
                    expAttributeValue,
                    0);

            entityAttributeMap.put(EntityAttributeType.EXPERIENCE, entityAttribute);
        }
        entityAttribute.setCurrentValue(expAttributeValue);

        return entityAttribute;
    }


    // ------------ 等级操作 ------------

    public void setEntityExperienceLevel(int value) {
        this.entityExpLevelComponent.setLevel(value);
        this.entityExperienceComponent.setMaxExperience(this.levelUpgradeNeeded(value));

        this.refreshEntityExperienceLevelAttribute();
    }

    /**
     * 获取生物等级
     *
     * @return
     */
    public int getEntityLevel() {
        return this.entityExpLevelComponent.getLevel();
    }

    public boolean addLevel(int level) {
        //直接增加等级
        level = this.entityExpLevelComponent.getLevel() + level;
        if (level < 0) {
            this.entityExpLevelComponent.setLevel(0);
        } else {
            this.entityExpLevelComponent.setLevel(level);
        }

        this.refreshEntityExperienceLevelAttribute();

        return true;
    }

    public EntityAttribute getEntityExpLevelAttribute() {
        EntityAttribute entityAttribute = this.entityAttributeDataComponent.getEntityAttributeMap().get(EntityAttributeType.EXPERIENCE_LEVEL);
        if (entityAttribute == null) {
            entityAttribute = refreshEntityExperienceLevelAttribute();
        }

        return entityAttribute;
    }

    public EntityAttribute refreshEntityExperienceLevelAttribute() {
        Map<EntityAttributeType, EntityAttribute> entityAttributeMap = this.entityAttributeDataComponent.getEntityAttributeMap();
        EntityAttribute entityAttribute = entityAttributeMap.get(EntityAttributeType.EXPERIENCE_LEVEL);
        if (entityAttribute == null) {
            entityAttribute = new EntityAttribute(EntityAttributeType.EXPERIENCE_LEVEL,
                    this.entityExpLevelComponent.getMinLevel(),
                    this.entityExpLevelComponent.getMaxLevel(),
                    this.entityExpLevelComponent.getLevel(),
                    0);

            entityAttributeMap.put(EntityAttributeType.EXPERIENCE_LEVEL, entityAttribute);
        }
        entityAttribute.setCurrentValue(this.entityExpLevelComponent.getLevel());

        return entityAttribute;
    }

    /**
     * 花费等级，与直接操作的区别是会记录花销，用于附魔/修复场景
     *
     * @param cost
     */
    public boolean spendLevel(int cost) {
        int resultLevel = this.entityExpLevelComponent.getLevel() - cost;
        if (resultLevel < 0) {
            return false;
        } else {
            this.entityExpLevelComponent.setLastSpendLevel(cost);
            this.entityExpLevelComponent.setLevel(resultLevel);
            this.refreshEntityExperienceAttribute();
            this.refreshEntityExperienceLevelAttribute();

            return true;
        }
    }

    /**
     * 确认生物花销的等级
     *
     * @return
     */
    public int confirmLevelSpend() {
        int lastSpendLevel = this.entityExpLevelComponent.getLastSpendLevel();
        if (lastSpendLevel > 0) {
            this.entityExpLevelComponent.setLastSpendLevel(0);
            this.refreshEntityExperienceAttribute();
            this.refreshEntityExperienceLevelAttribute();
        }

        return lastSpendLevel;
    }

    /**
     * 每一等级升级需要的经验
     *
     * @param currentLevel
     * @return
     */
    public int levelUpgradeNeeded(int currentLevel) {
        if (currentLevel < 16) {
            return (currentLevel - 1) * 2 + 7;
        } else if (currentLevel < 31) {
            return (currentLevel - 1) * 5 - 38;
        } else {
            return (currentLevel - 1) * 9 - 158;
        }
    }

    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return REQUIRE_CLASSES;
    }

    @Override
    public void bindGameObject(GameObject gameObject) {
        this.entityAttributeDataComponent = ENTITY_ATTRIBUTE_DATA_COMPONENT_HANDLER.getComponent(gameObject);
        this.entityExperienceComponent = ENTITY_EXPERIENCE_COMPONENT_HANDLER.getComponent(gameObject);
        this.entityExpLevelComponent = ENTITY_EXP_LEVEL_COMPONENT_HANDLER.getComponent(gameObject);
    }
}
