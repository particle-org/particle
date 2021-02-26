package com.particle.game.world.level;

import com.particle.model.block.types.BlockPrototype;

import javax.inject.Singleton;

@Singleton
public class CropsService {

    /**
     * 判断该方块是否植物
     *
     * @return
     */
    public boolean isCrops(BlockPrototype type) {
        if (type == BlockPrototype.BEETROOT || type == BlockPrototype.CARROTS ||
                type == BlockPrototype.POTATOES || type == BlockPrototype.MELON_STEM
                || type == BlockPrototype.PUMPKIN_STEM || type == BlockPrototype.WHEAT) {
            return true;
        }
        return false;
    }
}
