package com.particle.game.block.interactor.processor.world.planting;

import com.particle.game.block.interactor.processor.world.BasicBlockProcessor;
import com.particle.game.block.planting.CakeService;
import com.particle.model.block.Block;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockCakePlaced extends BasicBlockProcessor {

    @Inject
    private CakeService cakeService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        return this.cakeService.checkPositionIllegal(level, targetPosition);
    }

    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        return true;
    }
}
