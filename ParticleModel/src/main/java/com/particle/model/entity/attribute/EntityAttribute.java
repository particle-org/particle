package com.particle.model.entity.attribute;

public class EntityAttribute {
    private EntityAttributeType type;
    private float minValue;
    private float maxValue;
    private float currentValue;
    private float defaultValue;

    public EntityAttribute(EntityAttributeType type, float minValue, float maxValue, float currentValue, float defaultValue) {
        this.type = type;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return type == null ? "" : type.value();
    }

    public EntityAttributeType getType() {
        return type;
    }

    public void setType(EntityAttributeType type) {
        this.type = type;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue = defaultValue;
    }
}
