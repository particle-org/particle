package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.block.common.modules.CookModule;

public class CookSerialization implements IStringSerialization<CookModule> {
    @Override
    public String serialization(GameObject gameObject, CookModule cookModule) {
        return cookModule.getCookTime() + "|" + cookModule.getStatus();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, CookModule cookModule) {
        String[] split = data.split("\\|");

        cookModule.setCookTime(Short.parseShort(split[0]));
        cookModule.setStatus(Short.parseShort(split[1]));
    }
}
