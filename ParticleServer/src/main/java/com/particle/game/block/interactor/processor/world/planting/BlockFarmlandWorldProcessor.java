package com.particle.game.block.interactor.processor.world.planting;

import com.particle.game.block.interactor.processor.world.BreakableBlockProcessor;
import com.particle.model.block.Block;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Singleton;

@Singleton
public class BlockFarmlandWorldProcessor extends BreakableBlockProcessor {
    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        targetBlock.setMeta(0);
        return super.onBlockPreDestroy(level, player, targetBlock, targetPosition);
    }
}
