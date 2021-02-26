package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.common.modules.TransformModule;

public class TransformConvertSerialization implements IStringSerialization<TransformModule> {

    @Override
    public String serialization(GameObject gameObject, TransformModule transformModule) {
        return String.format("%f,%f,%f,%f,%f,%f",
                transformModule.getX(),
                transformModule.getY(),
                transformModule.getZ(),
                transformModule.getPitch(),
                transformModule.getYaw(),
                transformModule.getYawHead());
    }

    @Override
    public void deserialization(GameObject gameObject, String data, TransformModule transformModule) {
        String[] dataSplited = data.split(",");

        transformModule.setPosition(Float.parseFloat(dataSplited[0]), Float.parseFloat(dataSplited[1]), Float.parseFloat(dataSplited[2]));
        transformModule.setDirection(Float.parseFloat(dataSplited[3]), Float.parseFloat(dataSplited[4]), Float.parseFloat(dataSplited[5]));
    }
}
