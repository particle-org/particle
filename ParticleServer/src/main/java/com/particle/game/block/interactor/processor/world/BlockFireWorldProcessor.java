package com.particle.game.block.interactor.processor.world;

import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.geometry.BlockGeometry;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockFireWorldProcessor extends BasicBlockProcessor {
    @Inject
    private LevelService levelService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {        // 若為石塊
        BlockPrototype downBlock = levelService.getBlockTypeAt(level, targetPosition.down());
        if (downBlock != BlockPrototype.AIR
                && downBlock != BlockPrototype.STONE_SLAB
                && downBlock != BlockPrototype.WOODEN_SLAB
                && downBlock != BlockPrototype.STONE_SLAB2
                && downBlock != BlockPrototype.IRON_BARS
                && downBlock.getBlockGeometry() != BlockGeometry.EMPTY) {
            return true;
        }


        return false;
    }
}
