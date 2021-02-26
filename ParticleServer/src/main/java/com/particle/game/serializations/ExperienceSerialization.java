package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.entity.attribute.explevel.EntityExperienceModule;

public class ExperienceSerialization implements IStringSerialization<EntityExperienceModule> {
    @Override
    public String serialization(GameObject gameObject, EntityExperienceModule entityExperienceModule) {
        return entityExperienceModule.getEntityLevel() + "|" + entityExperienceModule.getEntityExperience();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, EntityExperienceModule entityExperienceModule) {
        String[] split = data.split("\\|");

        entityExperienceModule.setEntityExperienceLevel(Integer.parseInt(split[0]));
        entityExperienceModule.setEntityExperience(Integer.parseInt(split[1]));
    }
}
