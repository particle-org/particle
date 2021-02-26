package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.model.entity.component.farming.CropsModule;

public class CropsModuleSerialization implements IStringSerialization<CropsModule> {
    @Override
    public String serialization(GameObject gameObject, CropsModule cropsModule) {
        return cropsModule.getMaxGrowMeta() + ":" + (cropsModule.isRemoveTileEntityAfterMature() ? 1 : 0);
    }

    @Override
    public void deserialization(GameObject gameObject, String data, CropsModule cropsModule) {
        String[] split = data.split(":");

        if (split.length != 2) {
            return;
        }

        cropsModule.setMaxGrowMeta(Integer.parseInt(split[0]));
        cropsModule.setRemoveTileEntityAfterMature(split[1].equals("1"));
    }
}
