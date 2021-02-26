package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.entity.attribute.satisfaction.EntitySatisfactionModule;

public class SatisfactionSerialization implements IStringSerialization<EntitySatisfactionModule> {
    @Override
    public String serialization(GameObject gameObject, EntitySatisfactionModule entitySatisfactionModule) {
        return entitySatisfactionModule.getFoodLevel()
                + "|" + entitySatisfactionModule.getMaxFoodLevel()
                + "|" + entitySatisfactionModule.getFoodExhaustionLevel()
                + "|" + entitySatisfactionModule.getMaxFoodSaturationLevel();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, EntitySatisfactionModule entitySatisfactionModule) {
        String[] split = data.split("\\|");

        entitySatisfactionModule.setFoodLevel(Integer.parseInt(split[0]));
        entitySatisfactionModule.setMaxFoodLevel(Integer.parseInt(split[1]));
        entitySatisfactionModule.setFoodSaturationLevel(Float.parseFloat(split[2]));
        entitySatisfactionModule.setMaxFoodSaturationLevel(Float.parseFloat(split[3]));
    }
}
