package com.particle.game.block.interactor.processor.updater;

import com.particle.game.block.interactor.BlockOperationType;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.events.level.block.BlockBreakEvent;
import com.particle.model.level.Level;
import com.particle.model.math.BlockWallSignFace;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockSignUpdater implements IBlockWorldUpdater {

    @Inject
    private LevelService levelService;

    @Inject
    private BlockService blockService;

    @Override
    public boolean onBlockUpdated(Level level, Player player, Block targetBlock, Vector3 targetPosition, BlockOperationType blockOperationType) {
        BlockWallSignFace blockWallSignFace = BlockWallSignFace.getBlockWallSignFace(targetBlock.getMeta());

        if (blockWallSignFace != null) {
            switch (blockWallSignFace) {
                case EAST:
                    if (this.levelService.getBlockTypeAt(level, targetPosition.add(-1, 0, 0)) == BlockPrototype.AIR)
                        this.blockService.breakBlockByPlayer(player, targetPosition, BlockBreakEvent.Caused.PLAYER);
                    break;
                case WEST:
                    if (this.levelService.getBlockTypeAt(level, targetPosition.add(1, 0, 0)) == BlockPrototype.AIR)
                        this.blockService.breakBlockByPlayer(player, targetPosition, BlockBreakEvent.Caused.PLAYER);
                    break;
                case NORTH:
                    if (this.levelService.getBlockTypeAt(level, targetPosition.add(0, 0, 1)) == BlockPrototype.AIR)
                        this.blockService.breakBlockByPlayer(player, targetPosition, BlockBreakEvent.Caused.PLAYER);
                    break;
                case SOUTH:
                    if (this.levelService.getBlockTypeAt(level, targetPosition.add(0, 0, -1)) == BlockPrototype.AIR)
                        this.blockService.breakBlockByPlayer(player, targetPosition, BlockBreakEvent.Caused.PLAYER);
                    break;
            }
        }


        return false;
    }
}
