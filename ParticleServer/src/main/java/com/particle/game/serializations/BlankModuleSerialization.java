package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.serialization.IStringSerialization;

public class BlankModuleSerialization implements IStringSerialization<ECSModule> {
    @Override
    public String serialization(GameObject gameObject, ECSModule ecsModule) {
        return "";
    }

    @Override
    public void deserialization(GameObject gameObject, String data, ECSModule ecsModule) {

    }
}
