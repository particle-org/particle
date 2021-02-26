package com.particle.game.block.interactor.processor.updater;

import com.particle.game.block.interactor.BlockOperationType;
import com.particle.game.block.interactor.BlockWorldService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FallingBlockUpdater implements IBlockWorldUpdater {

    @Inject
    private LevelService levelService;

    @Inject
    private BlockWorldService blockWorldService;

    @Override
    public boolean onBlockUpdated(Level level, Player player, Block targetBlock, Vector3 targetPosition, BlockOperationType blockOperationType) {
        BlockPrototype blockTypeAt = this.levelService.getBlockTypeAt(level, targetPosition.getX(), targetPosition.getY() - 1, targetPosition.getZ());

        if (blockTypeAt == BlockPrototype.AIR) {
            int fixPosition = this.levelService.getTopBlockHeightBelow(level, targetPosition.subtract(0, 1, 0)) + 1;

            this.levelService.setBlockAt(level, Block.getBlock(BlockPrototype.AIR), targetPosition);
            this.levelService.setBlockAt(level, targetBlock, new Vector3(targetPosition.getX(), fixPosition, targetPosition.getZ()));

            level.getLevelSchedule().scheduleDelayTask("FallingBlock", () -> {
                this.blockWorldService.doUpdate(level, player, targetPosition.add(0, 1, 0), blockOperationType);
            }, 100);
        }

        return false;
    }
}
