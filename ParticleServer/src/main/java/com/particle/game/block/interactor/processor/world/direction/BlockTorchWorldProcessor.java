package com.particle.game.block.interactor.processor.world.direction;

import com.particle.game.block.interactor.processor.world.BasicBlockProcessor;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.geometry.BlockGeometry;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.BlockTouchFace;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockTorchWorldProcessor extends BasicBlockProcessor {

    @Inject
    private PositionService positionService;

    @Inject
    private LevelService levelService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        BlockTouchFace playerDirection = positionService.getTouchFaceDirection(targetPosition, clickPosition);
        BlockPrototype block = levelService.getBlockTypeAt(level, targetPosition);
        BlockPrototype clickBlock = levelService.getBlockTypeAt(level, clickPosition);
        int meta = 0;
        if (playerDirection != null && block == BlockPrototype.AIR && clickBlock.getBlockGeometry() == BlockGeometry.SOLID) {
            meta = playerDirection.getIndex();
            targetBlock.setMeta(meta);
            return super.onBlockPrePlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
        }

        return false;
    }
}
