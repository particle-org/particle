package com.particle.core.ecs.serialization;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;

public interface IStringSerialization<T extends ECSModule> {

    String serialization(GameObject gameObject, T t);

    void deserialization(GameObject gameObject, String data, T t);

}
