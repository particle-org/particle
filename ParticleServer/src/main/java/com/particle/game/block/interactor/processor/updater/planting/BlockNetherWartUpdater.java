package com.particle.game.block.interactor.processor.updater.planting;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.particle.game.block.interactor.BlockOperationType;
import com.particle.game.block.interactor.processor.updater.IBlockWorldUpdater;
import com.particle.game.block.planting.NetherWartService;
import com.particle.game.world.level.BlockService;
import com.particle.model.block.Block;
import com.particle.model.events.level.block.BlockBreakEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

@Singleton
public class BlockNetherWartUpdater implements IBlockWorldUpdater {

    @Inject
    private NetherWartService netherWartService;

    @Inject
    private BlockService blockService;

    @Override
    public boolean onBlockUpdated(Level level, Player player, Block targetBlock, Vector3 targetPosition, BlockOperationType blockOperationType) {
        boolean positionIllegal = this.netherWartService.checkPositionIllegal(level, targetPosition);

        if (!positionIllegal) {
            //处理破坏完成逻辑
            if (player == null) {
                this.blockService.breakBlockByLevel(level, targetPosition, BlockBreakEvent.Caused.LEVEL);
            } else {
                this.blockService.breakBlockByPlayer(player, targetPosition, BlockBreakEvent.Caused.PLAYER);
            }
        }

        return false;
    }
}
