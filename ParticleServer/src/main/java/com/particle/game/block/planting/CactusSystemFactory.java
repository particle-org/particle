package com.particle.game.block.planting;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.block.interactor.BlockWorldService;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.farming.CactusModule;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 原始地图中，仙人掌没有对应的tileEntity
 */
@Singleton
public class CactusSystemFactory implements ECSSystemFactory<CactusSystemFactory.CactusSystem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CactusSystemFactory.class);

    private static final ECSModuleHandler<PlantGrowUpProgressModule> PLANT_GROW_UP_PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);
    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE__HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);


    @Inject
    private LevelService levelService;

    @Inject
    private BlockWorldService blockWorldService;

    public class CactusSystem implements ECSSystem {
        private Entity entity;
        private PlantGrowUpProgressModule plantGrowUpProgressModule;
        private TransformModule transformModule;

        public CactusSystem(Entity entity) {
            this.entity = entity;
            this.plantGrowUpProgressModule = PLANT_GROW_UP_PROGRESS_MODULE_HANDLER.getModule(entity);
            this.transformModule = TRANSFORM_MODULE__HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            Vector3 targetPosition = this.transformModule.getFloorPosition();
            BlockPrototype cactusBlockPrototype = levelService.getBlockTypeAt(this.entity.getLevel(), targetPosition);
            if (cactusBlockPrototype != BlockPrototype.CACTUS) {
                return;
            }

            Vector3 down = targetPosition.down(1);
            BlockPrototype downBlockPrototype = levelService.getBlockTypeAt(this.entity.getLevel(), down);

            // 处理仙人掌的增长
            if (downBlockPrototype == BlockPrototype.SAND) {
                long future = this.plantGrowUpProgressModule.getFutureUpdateTime();
                if (System.currentTimeMillis() > future) {
                    this.plantGrowUpProgressModule.randomFutureUpdateTime();
                    this.growUp(this.entity.getLevel(), targetPosition, downBlockPrototype, down);
                }
            }
        }

        /**
         * 成长
         *
         * @param level
         * @param position
         */
        private void growUp(Level level, Vector3 position, BlockPrototype downBlockPrototype, Vector3 downPosition) {
            for (int y = 1; y <= 2; y++) {
                Vector3 upPosition = position.up(y);
                BlockPrototype up = levelService.getBlockTypeAt(level, upPosition);
                if (up == null || up == BlockPrototype.AIR) {
                    // 更新方块信息
                    Block targetBlock = Block.getBlock(BlockPrototype.CACTUS);
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
        return new Class[]{CactusModule.class, PlantGrowUpProgressModule.class, TransformModule.class};
    }

    @Override
    public CactusSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new CactusSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<CactusSystem> getSystemClass() {
        return CactusSystem.class;
    }
}
