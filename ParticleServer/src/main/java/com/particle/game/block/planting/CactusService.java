package com.particle.game.block.planting;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;

@Singleton
public class CactusService {

    @Inject
    private LevelService levelService;

    public boolean checkPositionIllegal(Level level, Vector3 position) {
        // 下方检查
        BlockPrototype downBlock = levelService.getBlockTypeAt(level, position.down(1));
        // 仙人掌，只能种在沙子和仙人掌上
        if (downBlock != BlockPrototype.SAND && downBlock != BlockPrototype.CACTUS) {
            return false;
        }

        // 周围检查
        BlockPrototype northBlock = levelService.getBlockTypeAt(level, position.north(1));
        BlockPrototype southBlock = levelService.getBlockTypeAt(level, position.south(1));
        BlockPrototype eastBlock = levelService.getBlockTypeAt(level, position.east(1));
        BlockPrototype westBlock = levelService.getBlockTypeAt(level, position.west(1));
        // 周围方块可穿透才允许放置
        return northBlock.getBlockGeometry().canPassThrow()
                && southBlock.getBlockGeometry().canPassThrow()
                && eastBlock.getBlockGeometry().canPassThrow()
                && westBlock.getBlockGeometry().canPassThrow();
    }

}
