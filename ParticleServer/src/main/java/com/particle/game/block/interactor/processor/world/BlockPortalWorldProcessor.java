package com.particle.game.block.interactor.processor.world;

import com.particle.model.block.Block;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Singleton;

@Singleton
public class BlockPortalWorldProcessor extends BasicBlockProcessor {
    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        return false;
    }
}
