package com.particle.game.block.interactor.processor.updater.planting;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.particle.game.block.interactor.BlockOperationType;
import com.particle.game.block.interactor.BlockWorldService;
import com.particle.game.block.interactor.processor.updater.IBlockWorldUpdater;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

@Singleton
public class BlockSandUpdater implements IBlockWorldUpdater {

    @Inject
    private LevelService levelService;

    @Inject
    private BlockWorldService blockWorldService;

    @Override
    public boolean onBlockUpdated(Level level, Player player, Block targetBlock, Vector3 targetPosition, BlockOperationType blockOperationType) {
        targetPosition = targetPosition.add(0, 1, 0);
        // 补充检查甘蔗
        if (this.levelService.getBlockTypeAt(level, targetPosition) == BlockPrototype.REEDS) {
            this.blockWorldService.doUpdate(level, player, targetPosition, blockOperationType);
        }

        return true;
    }
}
