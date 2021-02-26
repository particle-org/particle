package com.particle.game.block.interactor.processor.world.direction;

import com.particle.game.block.attribute.BlockAttributeService;
import com.particle.game.block.interactor.processor.world.BreakableBlockProcessor;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.BlockDetailFace;
import com.particle.model.math.BlockWallSignFace;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockSignWorldProcessor extends BreakableBlockProcessor {

    @Inject
    private PositionService positionService;

    @Inject
    private LevelService levelService;

    @Inject
    private BlockAttributeService blockAttributeService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        if (targetBlock.getType() == BlockPrototype.WALL_SIGN
                || targetBlock.getType() == BlockPrototype.SPRUCE_WALL_SIGN
                || targetBlock.getType() == BlockPrototype.BIRCH_WALL_SIGN
                || targetBlock.getType() == BlockPrototype.JUNGLE_WALL_SIGN
                || targetBlock.getType() == BlockPrototype.ACACIA_WALL_SIGN
                || targetBlock.getType() == BlockPrototype.DARKOAK_WALL_SIGN) {
            BlockWallSignFace playerDirection = positionService.get4FaceDirection(targetPosition, clickPosition, BlockWallSignFace.class);
            BlockPrototype blockPrototype = levelService.getBlockTypeAt(level, targetPosition);
            int meta = 0;
            // 空氣 且可被覆蓋
            if (playerDirection != null && this.blockAttributeService.isCanBeCover(blockPrototype)) {
                meta = playerDirection.getIndex();
                targetBlock.setMeta(meta);
                return super.onBlockPrePlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
            }
        }

        // 站牌
        if (targetBlock.getType() == BlockPrototype.STANDING_SIGN
                || targetBlock.getType() == BlockPrototype.SPRUCE_STANDING_SIGN
                || targetBlock.getType() == BlockPrototype.BIRCH_STANDING_SIGN
                || targetBlock.getType() == BlockPrototype.JUNGLE_STANDING_SIGN
                || targetBlock.getType() == BlockPrototype.ACACIA_STANDING_SIGN
                || targetBlock.getType() == BlockPrototype.DARKOAK_STANDING_SIGN) {
            BlockDetailFace playerDirection = positionService.get16FaceDirectionByPlayer(player);
            BlockPrototype blockPrototype = levelService.getBlockTypeAt(level, targetPosition);
            int meta = 0;
            if (playerDirection != null && this.blockAttributeService.isCanBeCover(blockPrototype)) {
                meta = playerDirection.getIndex();
                targetBlock.setMeta(meta);
                return super.onBlockPrePlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
            }
        }

        return false;
    }
}
