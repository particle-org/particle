package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.model.block.Block;
import com.particle.model.entity.component.farming.StemModule;

public class StemModuleSerialization implements IStringSerialization<StemModule> {
    @Override
    public String serialization(GameObject gameObject, StemModule stemModule) {
        return stemModule.getStemType().getId() + ":" + stemModule.getFruitType().getId();
    }

    @Override
    public void deserialization(GameObject gameObject, String data, StemModule stemModule) {
        String[] split = data.split(":");
        if (split.length == 2) {
            stemModule.setStemType(Block.getBlockType(Integer.parseInt(split[0])));
            stemModule.setFruitType(Block.getBlockType(Integer.parseInt(split[1])));
        }
    }
}
