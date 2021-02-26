package com.particle.game.block.interactor.processor.updater;

import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.attribute.BlockAttributeService;
import com.particle.game.block.interactor.BlockOperationType;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.events.level.block.BlockBreakEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockDestroyUpdater implements IBlockWorldUpdater {

    @Inject
    protected LevelService levelService;

    protected EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private BlockService blockService;

    @Inject
    private BlockAttributeService blockAttributeService;

    @Override
    public boolean onBlockUpdated(Level level, Player player, Block targetBlock, Vector3 targetPosition, BlockOperationType blockOperationType) {
        Vector3 down = targetPosition.down();
        BlockPrototype downBlock = this.levelService.getBlockTypeAt(level, down);
        if (downBlock == null || this.blockAttributeService.isTransparency(downBlock)) {
            if (player == null) {
                this.blockService.breakBlockByLevel(level, targetPosition, BlockBreakEvent.Caused.LEVEL);
            } else {
                this.blockService.breakBlockByPlayer(player, targetPosition, BlockBreakEvent.Caused.PLAYER);
            }
        }
        return true;
    }
}
