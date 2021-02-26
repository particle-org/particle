package com.particle.game.block.interactor.processor.world.direction;

import com.particle.game.block.interactor.processor.world.BasicBlockProcessor;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.block.Block;
import com.particle.model.level.Level;
import com.particle.model.math.BlockStairsFace;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockStairsWorldProcessor extends BasicBlockProcessor {
    @Inject
    private PositionService positionService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        BlockStairsFace playerDirection = positionService.getStairsFaceDirection(player);
        int meta = 0;
        if (playerDirection != null) {
            meta = playerDirection.getIndex();
        }
        targetBlock.setMeta(meta);

        return super.onBlockPrePlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
    }
}
