package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.entity.attribute.health.EntityHealthModule;

public class HealthSerialization implements IStringSerialization<EntityHealthModule> {
    @Override
    public String serialization(GameObject gameObject, EntityHealthModule entityExperienceModule) {
        return entityExperienceModule.getHealth() + "|" + entityExperienceModule.getMaxHealth();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, EntityHealthModule entityExperienceModule) {
        String[] split = data.split("\\|");

        entityExperienceModule.setHealth(Float.parseFloat(split[0]));

        // 保存最大值可能会和一些buff冲突，导致重复叠加属性
        entityExperienceModule.setMaxHealth(20);
    }
}
