package com.particle.game.block.interactor.processor.updater;

import com.particle.game.block.interactor.BlockOperationType;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockFireUpdater implements IBlockWorldUpdater {

    @Inject
    private LevelService levelService;

    @Override
    public boolean onBlockUpdated(Level level, Player player, Block targetBlock, Vector3 targetPosition, BlockOperationType blockOperationType) {
        Vector3 down = targetPosition.down();
        BlockPrototype downBlockPrototype = this.levelService.getBlockTypeAt(level, down);
        if (downBlockPrototype == null || downBlockPrototype == BlockPrototype.AIR) {
            // 变成空氣
            this.levelService.setBlockAt(level, Block.getBlock(BlockPrototype.AIR), targetPosition);
            return true;
        }

        return true;
    }
}
