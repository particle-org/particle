package com.particle.game.block.interactor.processor.world.container;

import com.particle.game.block.tile.TileEntityService;
import com.particle.model.block.Block;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockChestWorldProcessor extends ContainerBlockProcessor {

    @Inject
    private TileEntityService tileEntityService;

    @Override
    public boolean onBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        super.onBlockPlaced(level, player, targetBlock, targetPosition);

        return true;
    }

    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        TileEntity tileEntity = this.tileEntityService.getEntityAt(level, targetPosition);
        if (tileEntity == null)
            return true;

        return super.onBlockPreDestroy(level, player, targetBlock, targetPosition);
    }

}
