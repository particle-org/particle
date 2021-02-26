package com.particle.model.entity.component.saver;

public interface ISaveableComponent<T> {
    T serialization();

    void deserialization(T t);
}
