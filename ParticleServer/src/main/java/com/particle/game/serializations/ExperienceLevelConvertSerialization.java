package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.entity.attribute.explevel.EntityExperienceModule;

public class ExperienceLevelConvertSerialization implements IStringSerialization<EntityExperienceModule> {
    @Override
    public String serialization(GameObject gameObject, EntityExperienceModule entityExperienceModule) {
        return "";
    }

    @Override
    public void deserialization(GameObject gameObject, String data, EntityExperienceModule entityExperienceModule) {
        int convertData = Integer.parseInt(data.substring(data.lastIndexOf(":") + 1));
        entityExperienceModule.setEntityExperienceLevel(convertData);
    }
}
