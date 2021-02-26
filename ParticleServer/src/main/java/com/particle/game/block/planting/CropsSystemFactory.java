package com.particle.game.block.planting;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.farming.CropsModule;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CropsSystemFactory implements ECSSystemFactory<CropsSystemFactory.CropsSystem> {

    private static final Logger logger = LoggerFactory.getLogger(CropsSystemFactory.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<PlantGrowUpProgressModule> PLANT_GROW_UP_PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);
    private static final ECSModuleHandler<CropsModule> CROPS_MODULE_HANDLER = ECSModuleHandler.buildHandler(CropsModule.class);


    @Inject
    private LevelService levelService;

    @Inject
    private EntitySpawnService entitySpawnService;

    public class CropsSystem implements ECSSystem {

        private Entity entity;
        private PlantGrowUpProgressModule plantGrowupProgressModule;
        private CropsModule cropsModule;
        private TransformModule transformModule;

        public CropsSystem(Entity entity) {
            this.entity = entity;
            this.plantGrowupProgressModule = PLANT_GROW_UP_PROGRESS_MODULE_HANDLER.getModule(entity);
            this.cropsModule = CROPS_MODULE_HANDLER.getModule(entity);
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            long now = System.currentTimeMillis();
            if (plantGrowupProgressModule.getFutureUpdateTime() < now) {
                Vector3 targetPosition = transformModule.getFloorPosition();
                Block targetBlock = levelService.getBlockAt(entity.getLevel(), targetPosition);

                if (targetBlock.getMeta() < cropsModule.getMaxGrowMeta()
                        && this.isAllowGrowUp(entity.getLevel(), targetPosition, targetBlock)) {
                    targetBlock.setMeta(targetBlock.getMeta() + 1);
                    plantGrowupProgressModule.randomFutureUpdateTime();
                    levelService.setBlockAt(entity.getLevel(), targetBlock, targetPosition);
                }

                if (targetBlock.getMeta() >= cropsModule.getMaxGrowMeta() && cropsModule.isRemoveTileEntityAfterMature()) {
                    entity.getLevel().getLevelSchedule().scheduleSimpleTask("crops_remove", () -> this.removeCropTileEntity(entity.getLevel(), targetPosition));
                    return;
                }
            }
        }

        /**
         * 去除掉tileEntity
         *
         * @param level
         * @param targetPosition
         */
        private void removeCropTileEntity(Level level, Vector3 targetPosition) {
            // 去除其对应的tileEntity
            entitySpawnService.despawnTileEntity(level, targetPosition);
        }

        /**
         * 是否允许生长
         *
         * @param level
         * @param position
         * @param targetBlock
         * @return
         */
        private boolean isAllowGrowUp(Level level, Vector3 position, Block targetBlock) {
            if (!this.isBrightEnough(level, position)) {
                return false;
            }
            float randonSeed = 0;
            Vector3 downPos = position.down(1);
            Block downBlock = levelService.getBlockAt(level, downPos);
            if (downBlock.getType() != BlockPrototype.FARMLAND) {
                return false;
            }
            if (downBlock.getMeta() > 0) {
                randonSeed += 4;
            } else {
                randonSeed += 2;
            }
            int y = position.getY() - 1;
            Block[] aroundBlocks = new Block[8];
            int index = -1;
            for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
                for (int z = position.getZ() - 1; z <= position.getZ() + 1; z++) {
                    if (x == position.getX() && z == position.getZ()) {
                        continue;
                    }
                    index++;
                    Vector3 aroundPos = new Vector3(x, y, z);
                    Block aroundDownBlock = levelService.getBlockAt(level, aroundPos);
                    if (aroundDownBlock.getType() != BlockPrototype.FARMLAND) {
                        continue;
                    }
                    if (aroundDownBlock.getMeta() > 0) {
                        randonSeed += 0.75;
                    } else {
                        randonSeed += 0.25;
                    }
                    Block aroundBlock = levelService.getBlockAt(level, new Vector3(x, position.getY(), z));
                    aroundBlocks[index] = aroundBlock;
                }
            }
            boolean needHalf = false;
            int[] diagonal = new int[]{0, 2, 4, 7};
            // 如果对角线方向有相同的需要减半
            for (int diaIndex = 0; diaIndex < diagonal.length; diaIndex++) {
                if (aroundBlocks[diaIndex] == null) {
                    continue;
                } else if (targetBlock.getType() == aroundBlocks[diaIndex].getType()) {
                    needHalf = true;
                    break;
                }
            }
            if (!needHalf) {
                int[] xLine = new int[]{1, 6};
                int[] zLine = new int[]{3, 4};
                // 如果东西位置和南北位置都至少有一个相同的作物，需要减半
                for (int xIndex = 0; xIndex < xLine.length; xIndex++) {
                    if (aroundBlocks[xIndex] == null) {
                        continue;
                    }
                    boolean NS = false;
                    if (targetBlock.getType() == aroundBlocks[xIndex].getType()) {
                        NS = true;
                        for (int zIndex = 0; zIndex < zLine.length; zIndex++) {
                            if (aroundBlocks[zIndex] == null) {
                                continue;
                            }
                            if (targetBlock.getType() == aroundBlocks[zIndex].getType()) {
                                needHalf = true;
                                break;
                            }
                        }
                    }
                    if (needHalf || NS) {
                        break;
                    }
                }
            }
            if (needHalf) {
                randonSeed = randonSeed / 2;
            }

            // 提高生長機率
            randonSeed = randonSeed * 3;
            double growProbability = Math.floor(25 / randonSeed) + 1;
            growProbability = 1 / growProbability;
            logger.debug("--position:{}, --growProbability:{}", position, growProbability);
            if (Math.random() < growProbability) {
                return true;
            } else {
                return false;
            }

        }

        /**
         * 判断周边的亮度
         *
         * @param level
         * @param position
         * @return
         */
        private boolean isBrightEnough(Level level, Vector3 position) {
            boolean isDay = levelService.isDay(level);
            if (isDay) {
                return true;
            }

            // TODO: 2019/6/21 临时关闭扫描操作，该操作会占用服务端约8%的性能
        /*
        // 火把光照等级为14
        int minY = Math.max(0, position.getY() - 5);
        int maxY = Math.min(255, position.getY() + 5);

        for (int x = position.getX() - 5; x <= position.getX() + 5; x++) {
            for (int z = position.getZ() - 5; z < position.getZ() + 5; z++) {
                for (int y = minY; y <= maxY; y++) {
                    if (x == position.getX() && z == position.getZ() && y == position.getY()) {
                        continue;
                    }
                    Block aroundBlock = null;
                    try
                    {
                        aroundBlock = levelService.getBlockAt(level, x, y, z);
                    }
                    catch (Exception e)
                    {
                        // 章凱叫我弄的! 問題可能被屏蔽~
                        logger.error("{} chunk error.", this.getClass().getName(), e);
                        level.getLevelSchedule().scheduleSimpleTask("crops_remove", ()-> this.removeCropTileEntity(level, position));
                        return false;
                    }

                    if (aroundBlock == null) {
                        continue;
                    }
                    if (aroundBlock.getType().getLuminance() - this.getManhattanDistance(x, y, z, position) > 9) {
                        return true;
                    }
                }
            }
        }
        */
            return false;
        }

        /**
         * 计算对角线的曼哈顿距离
         *
         * @param x
         * @param y
         * @param z
         * @param position
         * @return
         */
        private int getManhattanDistance(int x, int y, int z, Vector3 position) {
            int xLine = Math.abs(x - position.getX());
            int yLine = Math.abs(y - position.getY());
            int zLine = Math.abs(z - position.getZ());
            return xLine + yLine + zLine;
        }
    }


    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{
                CropsModule.class,
                PlantGrowUpProgressModule.class,
                TransformModule.class
        };
    }

    @Override
    public CropsSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new CropsSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<CropsSystem> getSystemClass() {
        return CropsSystem.class;
    }
}
