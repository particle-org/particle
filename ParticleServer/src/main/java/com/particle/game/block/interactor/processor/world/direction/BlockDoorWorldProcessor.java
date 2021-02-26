package com.particle.game.block.interactor.processor.world.direction;

import com.particle.game.block.interactor.processor.world.BasicBlockProcessor;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.BlockDoorFace;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockDoorWorldProcessor extends BasicBlockProcessor {

    @Inject
    private PositionService positionService;

    @Inject
    private LevelService levelService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        BlockPrototype blockPrototype = levelService.getBlockTypeAt(level, targetPosition.up());
        if (clickOffsetPosition.getY() >= 1 && blockPrototype == BlockPrototype.AIR) {
            BlockDoorFace playerDirection = positionService.getDoorFaceDirection(player);
            int meta = 0;
            if (playerDirection != null) {
                meta = playerDirection.getIndex();
            }
            targetBlock.setMeta(meta);

            return super.onBlockPrePlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
        } else {
            return false;
        }
    }

    @Override
    public boolean handleBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        // 門有上下部份
        // 下部份
        boolean state = this.levelService.setBlockAt(player.getLevel(), targetBlock, targetPosition);
        if (!state) {
            return false;
        }

        // 上部份
        targetBlock.setMeta(targetBlock.getMeta() ^ 8);
        BlockPrototype leftBlock = BlockPrototype.AIR;
        // 左邊有門
        switch (BlockDoorFace.getBlockDoorFace(targetBlock.getMeta() % 4)) {
            case EAST:
                leftBlock = levelService.getBlockTypeAt(level, targetPosition.south());
                break;
            case WEST:
                leftBlock = levelService.getBlockTypeAt(level, targetPosition.north());
                break;
            case NORTH:
                leftBlock = levelService.getBlockTypeAt(level, targetPosition.east());
                break;
            case SOUTH:
                leftBlock = levelService.getBlockTypeAt(level, targetPosition.west());
                break;
        }
        // 設定右開
        targetBlock.setMeta(8);
        if (leftBlock == targetBlock.getType()) {
            targetBlock.setMeta(targetBlock.getMeta() ^ 1);
        }


        state = this.levelService.setBlockAt(player.getLevel(), targetBlock, targetPosition.up());
        if (!state) {
            return false;
        }

        return true;
    }

    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        // 若是上部份
        if (targetBlock.getMeta() >> 3 == 1) {
            // 把下部份也破壞掉
            levelService.setBlockAt(level, Block.getBlock(BlockPrototype.AIR), targetPosition.down());
            return super.onBlockPreDestroy(level, player, targetBlock, targetPosition);
        }

        return true;
    }
}
