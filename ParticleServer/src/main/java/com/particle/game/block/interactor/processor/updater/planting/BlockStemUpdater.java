package com.particle.game.block.interactor.processor.updater.planting;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.interactor.BlockOperationType;
import com.particle.game.block.interactor.processor.updater.IBlockWorldUpdater;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.events.level.block.BlockBreakEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockStemUpdater implements IBlockWorldUpdater {

    private static final ECSModuleHandler<PlantGrowUpProgressModule> PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private TileEntityService tileEntityService;

    @Inject
    private LevelService levelService;

    @Inject
    private BlockService blockService;


    @Override
    public boolean onBlockUpdated(Level level, Player player, Block targetBlock, Vector3 targetPosition, BlockOperationType blockOperationType) {
        // 若是梗
        if (targetBlock.getType() == BlockPrototype.MELON_STEM || targetBlock.getType() == BlockPrototype.PUMPKIN_STEM) {
            BlockPrototype underBlock = levelService.getBlockTypeAt(level, targetPosition.down());
            BlockPrototype type = underBlock;
            // 檢查生長環境
            if (type == BlockPrototype.DIRT
                    || type == BlockPrototype.FARMLAND
                    || type == BlockPrototype.GRASS) {
                // 若這梗四周有其他果實在
                BlockPrototype eastBlock = levelService.getBlockTypeAt(level, targetPosition.east());
                BlockPrototype westBlock = levelService.getBlockTypeAt(level, targetPosition.west());
                BlockPrototype southBlock = levelService.getBlockTypeAt(level, targetPosition.south());
                BlockPrototype northBlock = levelService.getBlockTypeAt(level, targetPosition.north());
                if ((targetBlock.getType() == BlockPrototype.MELON_STEM &&
                        (eastBlock == BlockPrototype.MELON_BLOCK
                                || westBlock == BlockPrototype.MELON_BLOCK
                                || southBlock == BlockPrototype.MELON_BLOCK
                                || northBlock == BlockPrototype.MELON_BLOCK)) ||
                        (targetBlock.getType() == BlockPrototype.PUMPKIN_STEM &&
                                (eastBlock == BlockPrototype.PUMPKIN
                                        || westBlock == BlockPrototype.PUMPKIN
                                        || southBlock == BlockPrototype.PUMPKIN
                                        || northBlock == BlockPrototype.PUMPKIN))) {
                } else {
                    // 激活
                    TileEntity entity = tileEntityService.getEntityAt(level, targetPosition);
                    if (entity == null) {
                        entity = tileEntityService.createEntity(targetBlock.getType(), targetPosition);
                        if (entity != null) {
                            this.entitySpawnService.spawn(level, entity);

                            // 設置下次時間
                            PlantGrowUpProgressModule plantGrowupProgressModule = PROGRESS_MODULE_HANDLER.getModule(entity);
                            targetBlock.setMeta(targetBlock.getMeta());
                            levelService.setBlockAt(level, targetBlock, targetPosition);
                            plantGrowupProgressModule.randomFutureUpdateTime();
                        }
                    }
                }
            }
            // 破壞梗
            else {
                blockService.breakBlockByLevel(level, targetPosition, BlockBreakEvent.Caused.LEVEL);
            }
        }

        return true;
    }
}
