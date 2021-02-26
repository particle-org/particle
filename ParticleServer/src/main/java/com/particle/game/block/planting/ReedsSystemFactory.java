package com.particle.game.block.planting;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.block.interactor.BlockWorldService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.entity.component.farming.ReedsModule;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 原始地图中，蘆筍没有对应的tileEntity
 */
@Singleton
public class ReedsSystemFactory implements ECSSystemFactory<ReedsSystemFactory.ReedsSystem> {

    private static final Logger logger = LoggerFactory.getLogger(ReedsSystemFactory.class);

    private static final ECSModuleHandler<PlantGrowUpProgressModule> PLANT_GROW_UP_PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);


    @Inject
    private LevelService levelService;

    @Inject
    private BlockWorldService blockWorldService;

    @Inject
    private PositionService positionService;

    public class ReedsSystem implements ECSSystem {

        private Entity entity;
        private PlantGrowUpProgressModule plantGrowupProgressModule;

        public ReedsSystem(Entity entity) {
            this.entity = entity;
            this.plantGrowupProgressModule = PLANT_GROW_UP_PROGRESS_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            Vector3 targetPosition = new Vector3(positionService.getPosition(entity));
            BlockPrototype reedsBlock = levelService.getBlockTypeAt(entity.getLevel(), targetPosition);
            if (reedsBlock == null || reedsBlock != BlockPrototype.REEDS) {
                return;
            }

            Vector3 down = targetPosition.down(1);
            BlockPrototype downBlock = levelService.getBlockTypeAt(entity.getLevel(), down);

            // 处理蘆薈的增长
            long future = plantGrowupProgressModule.getFutureUpdateTime();
            if (System.currentTimeMillis() > future) {
                plantGrowupProgressModule.randomFutureUpdateTime();
                entity.getLevel().getLevelSchedule().scheduleSimpleTask("reeds_grow_task", () -> this.growUp(entity.getLevel(), targetPosition, downBlock, down));
            }
        }

        /**
         * 成长
         *
         * @param level
         * @param position
         */
        private void growUp(Level level, Vector3 position, BlockPrototype downBlock, Vector3 downPosition) {
            for (int y = 1; y <= 3; y++) {
                Vector3 upPosition = position.up(y);
                BlockPrototype up = levelService.getBlockTypeAt(level, upPosition);
                if (up == null || up == BlockPrototype.AIR) {
                    // 更新方块信息
                    Block targetBlock = Block.getBlock(BlockPrototype.REEDS);
                    targetBlock.setMeta(15);
                    //更新方块
                    this.placeBlock(level, targetBlock, upPosition);
                    return;
                }
            }
        }

        /**
         * 设置方块
         *
         * @param level
         * @param position
         */
        private void placeBlock(Level level, Block block, Vector3 position) {
            //更新方块
            levelService.setBlockAt(level, block, position);

            // 初始化对应的blockEntity
            blockWorldService.onBlockPlaced(level, null, block, position);
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{ReedsModule.class, PlantGrowUpProgressModule.class};
    }

    @Override
    public ReedsSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new ReedsSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<ReedsSystem> getSystemClass() {
        return ReedsSystem.class;
    }
}
