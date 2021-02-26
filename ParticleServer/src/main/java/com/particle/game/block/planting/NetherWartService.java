package com.particle.game.block.planting;

import com.google.inject.Inject;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;

import javax.inject.Singleton;

@Singleton
public class NetherWartService {
    @Inject
    private LevelService levelService;

    public boolean checkPositionIllegal(Level level, Vector3 position) {
        // 下方检查
        BlockPrototype downBlock = levelService.getBlockTypeAt(level, position.down(1));
        // 地狱疣下方不能为空
        return downBlock == BlockPrototype.SOUL_SAND;
    }
}
