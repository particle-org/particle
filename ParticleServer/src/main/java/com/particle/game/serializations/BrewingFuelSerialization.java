package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.block.brewing.BrewingFuelModule;

public class BrewingFuelSerialization implements IStringSerialization<BrewingFuelModule> {
    @Override
    public String serialization(GameObject gameObject, BrewingFuelModule brewingFuelModule) {
        return String.valueOf(brewingFuelModule.getFuelAmount());
    }

    @Override
    public void deserialization(GameObject gameObject, String data, BrewingFuelModule cookModule) {
        cookModule.setFuelAmount(Short.parseShort(data));
    }
}
