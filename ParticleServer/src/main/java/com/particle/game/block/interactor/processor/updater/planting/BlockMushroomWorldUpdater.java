package com.particle.game.block.interactor.processor.updater.planting;

import com.particle.game.block.interactor.BlockOperationType;
import com.particle.game.block.interactor.processor.updater.IBlockWorldUpdater;
import com.particle.game.block.planting.MushroomService;
import com.particle.game.world.level.BlockService;
import com.particle.model.block.Block;
import com.particle.model.events.level.block.BlockBreakEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockMushroomWorldUpdater implements IBlockWorldUpdater {
    @Inject
    private MushroomService mushroomService;

    @Inject
    private BlockService blockService;

    @Override
    public boolean onBlockUpdated(Level level, Player player, Block targetBlock,
                                  Vector3 targetPosition, BlockOperationType blockOperationType) {
        if (mushroomService.isMushroom(level, targetPosition)
                && !mushroomService.checkEnvironment(level, targetPosition)) {
            this.blockService.breakBlockByLevel(level, targetPosition, BlockBreakEvent.Caused.LEVEL);
        }

        return true;
    }
}
