package com.particle.game.block.interactor.processor.world.planting;

import com.particle.game.block.interactor.processor.world.BasicBlockProcessor;
import com.particle.game.block.planting.Reedservice;
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
public class BlockReedsWorldProcessor extends BasicBlockProcessor {

    @Inject
    private Reedservice reedservice;

    @Inject
    private LevelService levelService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        return this.reedservice.checkPositionIllegal(level, targetPosition);
    }

    @Override
    public boolean onBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        BlockPrototype blockTypeAt = this.levelService.getBlockTypeAt(level, targetPosition.down());

        if (blockTypeAt != BlockPrototype.REEDS) {
            super.onBlockPlaced(level, player, targetBlock, targetPosition);
        }

        return true;
    }
}
