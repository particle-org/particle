package com.particle.game.block.interactor.processor.updater;

import com.particle.game.block.interactor.BlockOperationType;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockWaterUpdater implements IBlockWorldUpdater {
    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private TileEntityService tileEntityService;

    @Inject
    private LevelService levelService;

    @Override
    public boolean onBlockUpdated(Level level, Player player, Block targetBlock, Vector3 targetPosition, BlockOperationType blockOperationType) {
        // 若是水
        if (targetBlock.getType() == BlockPrototype.WATER || targetBlock.getType() == BlockPrototype.FLOWING_WATER) {
            // 激活
            TileEntity entity = tileEntityService.getEntityAt(level, targetPosition);
            if (entity == null) {
                entity = tileEntityService.createEntity(targetBlock.getType(), targetPosition);
                if (entity != null) {
                    this.entitySpawnService.spawn(level, entity);
                }
            }
        }

        return true;
    }
}
