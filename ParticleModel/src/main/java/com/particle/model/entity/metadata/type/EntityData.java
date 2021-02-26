package com.particle.model.entity.metadata.type;

public abstract class EntityData<T> {
    public abstract EntityMetadataVariableType getType();

    public abstract T getData();

    public abstract void setData(T data);
}
