package com.particle.game.block.interactor.processor.world;

import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockWaterlilyWorldProcessor extends BasicBlockProcessor {

    @Inject
    private LevelService levelService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        Vector3 down = targetPosition.down(1);
        BlockPrototype downBlock = levelService.getBlockTypeAt(level, down);
        // 睡蓮，只能放在水、冰和浮冰
        if (downBlock == null || (downBlock != BlockPrototype.WATER &&
                downBlock != BlockPrototype.FLOWING_WATER &&
                downBlock != BlockPrototype.ICE &&
                downBlock != BlockPrototype.PACKED_ICE)) {
            return false;
        }

        return true;
    }
}
