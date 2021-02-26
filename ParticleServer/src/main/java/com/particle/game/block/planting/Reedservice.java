package com.particle.game.block.planting;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;

@Singleton
public class Reedservice {

    @Inject
    private LevelService levelService;

    public boolean checkPositionIllegal(Level level, Vector3 position) {
        // 下方检查
        Vector3 positionDown = position.down(1);
        BlockPrototype downBlock = levelService.getBlockTypeAt(level, positionDown);

        // 下方为甘蔗，快速判断合法
        if (downBlock == BlockPrototype.REEDS) {
            return true;
        }

        // 下方非泥土或沙子，快速判断非法
        if (downBlock != BlockPrototype.SAND
                && downBlock != BlockPrototype.DIRT
                && downBlock != BlockPrototype.GRASS) {
            return false;
        }

        // 周围检查
        BlockPrototype northBlock = levelService.getBlockTypeAt(level, positionDown.north(1));
        BlockPrototype southBlock = levelService.getBlockTypeAt(level, positionDown.south(1));
        BlockPrototype eastBlock = levelService.getBlockTypeAt(level, positionDown.east(1));
        BlockPrototype westBlock = levelService.getBlockTypeAt(level, positionDown.west(1));
        // 周围有水
        return northBlock == BlockPrototype.WATER
                || southBlock == BlockPrototype.WATER
                || eastBlock == BlockPrototype.WATER
                || westBlock == BlockPrototype.WATER
                || northBlock == BlockPrototype.FLOWING_WATER
                || southBlock == BlockPrototype.FLOWING_WATER
                || eastBlock == BlockPrototype.FLOWING_WATER
                || westBlock == BlockPrototype.FLOWING_WATER;
    }

}
