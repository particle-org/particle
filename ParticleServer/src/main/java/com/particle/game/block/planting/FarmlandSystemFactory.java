package com.particle.game.block.planting;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.block.attribute.BlockAttributeService;
import com.particle.game.block.interactor.BlockWorldService;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.world.level.CropsService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.geometry.BlockGeometry;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.farming.FarmlandModule;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.level.Level;
import com.particle.model.level.Weather;
import com.particle.model.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FarmlandSystemFactory implements ECSSystemFactory<FarmlandSystemFactory.FarmlandSystem> {

    private static final Logger logger = LoggerFactory.getLogger(FarmlandSystemFactory.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<PlantGrowUpProgressModule> PLANT_GROW_UP_PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);


    @Inject
    private LevelService levelService;

    @Inject
    private BlockWorldService blockWorldService;

    @Inject
    private CropsService cropsService;

    @Inject
    private BlockAttributeService blockAttributeService;

    public class FarmlandSystem implements ECSSystem {

        private Entity entity;
        private TransformModule transformModule;
        private PlantGrowUpProgressModule plantGrowupProgressModule;

        public FarmlandSystem(Entity entity) {
            this.entity = entity;
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
            this.plantGrowupProgressModule = PLANT_GROW_UP_PROGRESS_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            Vector3 targetPosition = transformModule.getFloorPosition();
            Vector3 upPosition = targetPosition.up(1);
            BlockPrototype upBlock = levelService.getBlockTypeAt(entity.getLevel(), upPosition);
            // 上面是固体方块且不可被沖刷，退化为耕地
            if (upBlock != null && upBlock.getBlockGeometry() == BlockGeometry.SOLID && !blockAttributeService.isCanBeScour(upBlock)) {
                Block currentBlock = levelService.getBlockAt(entity.getLevel(), targetPosition);
                entity.getLevel().getLevelSchedule().scheduleSimpleTask("replace_farmland_block", () -> this.replaceFarmlandBlock(entity.getLevel(), currentBlock, targetPosition, upBlock, upPosition));
                return;
            }

            // 如果上面种植有作物
            if (cropsService.isCrops(upBlock)) {
                return;
            }

            long future = plantGrowupProgressModule.getFutureUpdateTime();
            if (System.currentTimeMillis() > future) {
                plantGrowupProgressModule.randomFutureUpdateTime();
                entity.getLevel().getLevelSchedule().scheduleSimpleTask("wet_progress", () -> this.wetProgress(entity.getLevel(), targetPosition, upBlock, upPosition));
            }
        }

        /**
         * 耕地湿润或者退化
         *
         * @param level
         * @param position
         * @param upBlock
         * @param upPosition
         */
        private void wetProgress(Level level, Vector3 position, BlockPrototype upBlock, Vector3 upPosition) {
            Block currentBlock = levelService.getBlockAt(level, position);
            if (currentBlock == null || currentBlock.getType() != BlockPrototype.FARMLAND) {
                return;
            }
            boolean isExistedWater = this.isExistedWater(level, position);
            if (isExistedWater) {
                int meta = currentBlock.getMeta();
                if (meta < 7) {
                    currentBlock.setMeta(meta + 1);
                    //更新方块
                    levelService.setBlockAt(level, currentBlock, position);
                }
            } else {
                int meta = currentBlock.getMeta();
                if (meta > 0) {
                    currentBlock.setMeta(meta - 1);
                    //更新方块
                    levelService.setBlockAt(level, currentBlock, position);
                } else {
                    this.replaceFarmlandBlock(level, currentBlock, position, upBlock, upPosition);
                }
            }
        }

        /**
         * 替换耕地为泥土方块
         *
         * @param level
         * @param position
         */
        private void replaceFarmlandBlock(Level level, Block targetBlock, Vector3 position,
                                          BlockPrototype upBlock, Vector3 upPosition) {
            //处理破坏完成逻辑
            blockWorldService.onBlockDestroyed(level, null, targetBlock, position);
            //更新方块
            Block newBlock = Block.getBlock(BlockPrototype.DIRT);
            levelService.setBlockAt(level, newBlock, position);
            // 初始化对应的blockEntity
            blockWorldService.onBlockPlaced(level, null, newBlock, position);
            // TODO收割
        }

        /**
         * 判断周边四块方块是否存在水
         *
         * @param level
         * @param position
         * @return
         */
        private boolean isExistedWater(Level level, Vector3 position) {
            if (level.getWeather() == Weather.RAIN) {
                return true;
            }
            for (int x = position.getX() - 4; x <= position.getX() + 4; x++) {
                for (int z = position.getZ() - 4; z <= position.getZ() + 4; z++) {
                    for (int y = position.getY(); y <= position.getY() + 1; y++) {
                        if (x == position.getX() && z == position.getZ() && y == position.getY()) {
                            continue;
                        }
                        BlockPrototype aroundBlock = levelService.getBlockTypeAt(level, x, y, z);
                        if (aroundBlock == null) {
                            continue;
                        }
                        if (aroundBlock == BlockPrototype.WATER ||
                                aroundBlock == BlockPrototype.FLOWING_WATER) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }


    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{FarmlandModule.class, PlantGrowUpProgressModule.class, TransformModule.class};
    }

    @Override
    public FarmlandSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new FarmlandSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<FarmlandSystem> getSystemClass() {
        return FarmlandSystem.class;
    }
}
