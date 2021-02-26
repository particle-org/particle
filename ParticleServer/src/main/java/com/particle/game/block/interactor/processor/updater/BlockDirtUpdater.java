package com.particle.game.block.interactor.processor.updater;

import com.particle.game.block.interactor.BlockOperationType;
import com.particle.game.block.planting.GrassService;
import com.particle.model.block.Block;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockDirtUpdater implements IBlockWorldUpdater {

    @Inject
    private GrassService grassService;

    @Override
    public boolean onBlockUpdated(Level level, Player player, Block targetBlock, Vector3 targetPosition, BlockOperationType blockOperationType) {
        this.grassService.checkUpdate(level, targetPosition);

        return true;
    }
}
