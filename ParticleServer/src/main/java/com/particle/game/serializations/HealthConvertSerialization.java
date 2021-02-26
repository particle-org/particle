package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.entity.attribute.health.EntityHealthModule;

public class HealthConvertSerialization implements IStringSerialization<EntityHealthModule> {
    @Override
    public String serialization(GameObject gameObject, EntityHealthModule entityExperienceModule) {
        return "";
    }

    @Override
    public void deserialization(GameObject gameObject, String data, EntityHealthModule entityExperienceModule) {
        float convertData = Float.parseFloat(data.substring(data.lastIndexOf(":") + 1));
        entityExperienceModule.setHealth(convertData);
    }
}
