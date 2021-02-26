package com.particle.game.block.interactor.processor.world.container;

import com.particle.game.block.attribute.BlockAttributeService;
import com.particle.game.world.level.BlockService;
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
public class BlockBrewingStandWorldProcessor extends ContainerBlockProcessor {

    @Inject
    private LevelService levelService;

    @Inject
    private BlockService blockService;

    @Inject
    private BlockAttributeService blockAttributeService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        BlockPrototype downBlock = this.levelService.getBlockTypeAt(level, targetPosition.getX(), targetPosition.getY() - 1, targetPosition.getZ());
        if (downBlock == null || this.blockAttributeService.isTransparency(downBlock)) {
            // 透明或者不存在
            return false;
        }

        return true;
    }
}
