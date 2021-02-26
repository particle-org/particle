package com.particle.game.block.interactor.processor.world;

import com.particle.game.entity.movement.PositionService;
import com.particle.model.block.Block;
import com.particle.model.level.Level;
import com.particle.model.math.BlockFace;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;

public abstract class DirectionBlockProcessor extends BasicBlockProcessor {

    @Inject
    private PositionService positionService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        BlockFace playerDirection = positionService.getFaceDirection(player);
        int meta = 0;
        if (playerDirection != null) {
            meta = playerDirection.getOppositeIndex();
        }
        targetBlock.setMeta(meta);

        return super.onBlockPrePlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
    }
}
