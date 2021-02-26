package com.particle.game.entity.attribute.satisfaction;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.entity.attribute.EntityAttributeDataComponent;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.attribute.EntityAttributeType;

import java.util.Map;

public class EntitySatisfactionModule extends ECSModule {

    private static final ECSComponentHandler<EntityAttributeDataComponent> ENTITY_ATTRIBUTE_DATA_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntityAttributeDataComponent.class);
    private static final ECSComponentHandler<EntitySatisfactionComponent> ENTITY_SATISFACTION_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(EntitySatisfactionComponent.class);

    private static final Class[] REQUIRE_CLASSES = new Class[]{EntityAttributeDataComponent.class, EntitySatisfactionComponent.class};

    public static final float HUNGRY_CHANGE_THREASHOLD = 12.0f;

    private EntityAttributeDataComponent entityAttributeDataComponent;
    private EntitySatisfactionComponent entitySatisfactionComponent;

    public int getFoodLevel() {
        return this.entitySatisfactionComponent.getFoodLevel();
    }

    public int getMaxFoodLevel() {
        return this.entitySatisfactionComponent.getMaxFoodLevel();
    }

    public void setFoodLevel(int foodLevel) {
        if (foodLevel < 0) foodLevel = 0;
        else if (foodLevel > this.entitySatisfactionComponent.getMaxFoodLevel())
            foodLevel = this.entitySatisfactionComponent.getMaxFoodLevel();

        this.entitySatisfactionComponent.setFoodLevel(foodLevel);

        this.refreshEntityHungerAttribute();
    }

    public void addFoodLevel(int addValue) {
        int newFoodLevel = entitySatisfactionComponent.getFoodLevel() + addValue;
        setFoodLevel(newFoodLevel);
    }

    public void setMaxFoodLevel(int maxFoodLevel) {
        this.entitySatisfactionComponent.setMaxFoodLevel(maxFoodLevel);

        this.refreshEntityHungerAttribute();
    }

    public float getFoodSaturationLevel() {
        return this.entitySatisfactionComponent.getFoodSaturationLevel();
    }

    public float getMaxFoodSaturationLevel() {
        return this.entitySatisfactionComponent.getMaxFoodSaturationLevel();
    }

    public void setFoodSaturationLevel(float foodSaturationLevel) {
        if (foodSaturationLevel > this.entitySatisfactionComponent.getFoodLevel())
            foodSaturationLevel = this.entitySatisfactionComponent.getFoodLevel();
        else if (foodSaturationLevel < 0) foodSaturationLevel = 0;
        else if (foodSaturationLevel > this.entitySatisfactionComponent.getMaxFoodSaturationLevel())
            foodSaturationLevel = this.entitySatisfactionComponent.getMaxFoodSaturationLevel();

        this.entitySatisfactionComponent.setFoodSaturationLevel(foodSaturationLevel);

        this.refreshEntitySaturationAttribute();
    }

    public void setMaxFoodSaturationLevel(float maxFoodSaturationLevel) {
        this.entitySatisfactionComponent.setMaxFoodSaturationLevel(maxFoodSaturationLevel);

        this.refreshEntitySaturationAttribute();
    }

    public float getFoodExhaustionLevel() {
        return this.entitySatisfactionComponent.getFoodExhaustionLevel();
    }

    public void setFoodExhaustionLevel(float foodExhaustionLevel) {
        if (foodExhaustionLevel > HUNGRY_CHANGE_THREASHOLD) foodExhaustionLevel = HUNGRY_CHANGE_THREASHOLD;
        else if (foodExhaustionLevel < 0) foodExhaustionLevel = 0;

        this.entitySatisfactionComponent.setFoodExhaustionLevel(foodExhaustionLevel);
    }

    public void addFoodExhaustionLevel(float foodExhaustionLevel) {
        foodExhaustionLevel += this.entitySatisfactionComponent.getFoodExhaustionLevel();

        if (foodExhaustionLevel > HUNGRY_CHANGE_THREASHOLD) foodExhaustionLevel = HUNGRY_CHANGE_THREASHOLD;
        else if (foodExhaustionLevel < 0) foodExhaustionLevel = 0;

        this.entitySatisfactionComponent.setFoodExhaustionLevel(foodExhaustionLevel);
    }

    public EntityAttribute getEntityHungerAttribute() {
        EntityAttribute entityAttribute = this.entityAttributeDataComponent.getEntityAttributeMap().get(EntityAttributeType.HUNGER);
        if (entityAttribute == null) {
            entityAttribute = refreshEntityHungerAttribute();
        }

        return entityAttribute;
    }

    public EntityAttribute refreshEntityHungerAttribute() {
        Map<EntityAttributeType, EntityAttribute> entityAttributeMap = this.entityAttributeDataComponent.getEntityAttributeMap();
        EntityAttribute entityAttribute = entityAttributeMap.get(EntityAttributeType.HUNGER);
        if (entityAttribute == null) {
            entityAttribute = new EntityAttribute(EntityAttributeType.HUNGER,
                    0,
                    this.entitySatisfactionComponent.getMaxFoodLevel(),
                    this.entitySatisfactionComponent.getFoodLevel(),
                    0);

            entityAttributeMap.put(EntityAttributeType.HUNGER, entityAttribute);
        }
        entityAttribute.setCurrentValue(this.entitySatisfactionComponent.getFoodLevel());
        entityAttribute.setMaxValue(this.entitySatisfactionComponent.getMaxFoodLevel());

        return entityAttribute;
    }

    public EntityAttribute getEntitySaturationAttribute() {
        EntityAttribute entityAttribute = this.entityAttributeDataComponent.getEntityAttributeMap().get(EntityAttributeType.SATURATION);
        if (entityAttribute == null) {
            entityAttribute = refreshEntitySaturationAttribute();
        }

        return entityAttribute;
    }

    public EntityAttribute refreshEntitySaturationAttribute() {
        Map<EntityAttributeType, EntityAttribute> entityAttributeMap = this.entityAttributeDataComponent.getEntityAttributeMap();
        EntityAttribute entityAttribute = entityAttributeMap.get(EntityAttributeType.SATURATION);
        if (entityAttribute == null) {
            entityAttribute = new EntityAttribute(EntityAttributeType.SATURATION,
                    0,
                    this.entitySatisfactionComponent.getMaxFoodSaturationLevel(),
                    this.entitySatisfactionComponent.getFoodSaturationLevel(),
                    0);

            entityAttributeMap.put(EntityAttributeType.SATURATION, entityAttribute);
        }
        entityAttribute.setCurrentValue(this.entitySatisfactionComponent.getFoodSaturationLevel());
        entityAttribute.setMaxValue(this.entitySatisfactionComponent.getMaxFoodSaturationLevel());

        return entityAttribute;
    }

    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return REQUIRE_CLASSES;
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.entityAttributeDataComponent = ENTITY_ATTRIBUTE_DATA_COMPONENT_HANDLER.getComponent(gameObject);
        this.entitySatisfactionComponent = ENTITY_SATISFACTION_COMPONENT_HANDLER.getComponent(gameObject);
    }
}
