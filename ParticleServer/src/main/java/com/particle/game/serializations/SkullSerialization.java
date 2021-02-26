package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.block.common.modules.SkullModule;

public class SkullSerialization implements IStringSerialization<SkullModule> {
    @Override
    public String serialization(GameObject gameObject, SkullModule skullModule) {
        return skullModule.getRot() + "|" + skullModule.getSkullType();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, SkullModule skullModule) {
        String[] split = data.split("\\|");
        if (split.length == 2) {
            skullModule.setRot(Byte.valueOf(split[0]));
            skullModule.setSkullType(Byte.valueOf(split[1]));
        }
    }
}
