package com.particle.game.block.interactor.processor.world;

import com.particle.game.block.planting.GrassService;
import com.particle.model.block.Block;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockDirtPlaceProcessor extends DefaultBlockProcessor {

    @Inject
    private GrassService grassService;

    @Override
    public boolean onBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        this.grassService.checkUpdate(level, targetPosition);

        return true;
    }

}
