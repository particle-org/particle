package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.block.furnace.FurnaceFuelModule;

public class FurnaceFuelSerialization implements IStringSerialization<FurnaceFuelModule> {
    @Override
    public String serialization(GameObject gameObject, FurnaceFuelModule entityGameModeModule) {
        return entityGameModeModule.getBurnTime() + "|" + entityGameModeModule.getMaxFuelTime();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, FurnaceFuelModule entityGameModeModule) {
        String[] split = data.split("\\|");

        entityGameModeModule.setBurnTime(Short.parseShort(split[0]));
        entityGameModeModule.setMaxFuelTime(Short.parseShort(split[1]));
    }
}
