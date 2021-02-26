package com.particle.game.serializations;

import com.alibaba.fastjson.JSONObject;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.entity.attribute.satisfaction.EntitySatisfactionModule;

public class SatisfactionConvertSerialization implements IStringSerialization<EntitySatisfactionModule> {
    @Override
    public String serialization(GameObject gameObject, EntitySatisfactionModule entityHealthModule) {
        return "";
    }

    @Override
    public void deserialization(GameObject gameObject, String data, EntitySatisfactionModule entitySatisfactionModule) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        entitySatisfactionModule.setFoodLevel(jsonObject.getInteger("foodLevel"));
        entitySatisfactionModule.setFoodSaturationLevel(jsonObject.getFloat("foodSaturationLevel"));
        entitySatisfactionModule.setFoodExhaustionLevel(jsonObject.getFloat("foodExhuastionLevel"));
    }
}
