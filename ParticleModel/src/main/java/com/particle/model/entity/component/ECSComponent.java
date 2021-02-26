package com.particle.model.entity.component;

@Deprecated
public abstract class ECSComponent {

    protected static int ECS_COMPONENT_ID = 0;

    public abstract int getId();

    public abstract String getName();
}
