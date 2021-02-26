package com.particle.game.block.planting;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.block.tile.model.SaplingTileEntity;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.entity.component.farming.SaplingModule;
import com.particle.model.events.level.block.BlockBreakEvent;
import com.particle.model.item.types.SaplingType;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;

/**
 * 樹苗
 */
@Singleton
public class SaplingSystemFactory implements ECSSystemFactory<SaplingSystemFactory.SaplingSystem> {

    private static final Logger logger = LoggerFactory.getLogger(SaplingSystemFactory.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<PlantGrowUpProgressModule> PLANT_GROW_UP_PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);


    private Random random = new Random();

    @Inject
    private LevelService levelService;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private SaplingService saplingService;

    @Inject
    private BlockService blockService;

    public class SaplingSystem implements ECSSystem {

        private Entity entity;
        private PlantGrowUpProgressModule plantGrowupProgressModule;
        private TransformModule transformModule;

        public SaplingSystem(Entity entity) {
            this.entity = entity;
            this.plantGrowupProgressModule = PLANT_GROW_UP_PROGRESS_MODULE_HANDLER.getModule(entity);
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            if (!(entity instanceof SaplingTileEntity)) {
                return;
            }

            long now = System.currentTimeMillis();

            if (plantGrowupProgressModule.getFutureUpdateTime() < now) {

                Vector3 targetPosition = transformModule.getFloorPosition();
                Block targetBlock = levelService.getBlockAt(entity.getLevel(), targetPosition);
                // 非法樹苗
                if (targetBlock.getMeta() > 5) {
                    // 變成空氣
                    levelService.setBlockAt(entity.getLevel(), Block.getBlock(BlockPrototype.AIR), targetPosition);
                    return;
                }

                // 若是種植環境
                if (isPlant(entity.getLevel(), targetPosition, targetBlock)) {

                    // 選取高度
                    int treeTop = chanceSize(entity.getLevel(), targetPosition, targetBlock);
                    if (isAllowGrowUp(entity.getLevel(), targetPosition, targetBlock, treeTop)) {
                        // 若可以成長
                        growTree(entity.getLevel(), targetPosition, treeTop, targetBlock);

                        // 樹苗只長一次, 長好長滿, 長完即死
                        entity.getLevel().getLevelSchedule().scheduleSimpleTask("saplingRemove", () -> entitySpawnService.despawnTileEntity(entity.getLevel(), targetPosition));
                        return;
                    }
                } else {
                    // 變為可撿狀態
                    blockService.breakBlockByLevel(entity.getLevel(), targetPosition, BlockBreakEvent.Caused.LEVEL);

                    return;
                }

                // 设置下一次更新时间
                plantGrowupProgressModule.randomFutureUpdateTime();
            }
        }
    }


    /**
     * 是否可種植
     *
     * @param level
     * @param position
     * @param targetBlock
     * @return
     */
    private boolean isPlant(Level level, Vector3 position, Block targetBlock) {
        // 光照不足
        if (!this.isBrightEnough(level, position)) {
            // 上方有東西遮擋
            for (int i = 1; i < 256 - position.getY(); i++) {
                BlockPrototype upBlock = levelService.getBlockTypeAt(level, position.up(i));
                // 非空氣且非玻璃且非樹葉
                if (upBlock != BlockPrototype.AIR && upBlock != BlockPrototype.GLASS && upBlock != BlockPrototype.LEAVES) {
                    return false;
                }
            }
        }

        // 不在對應土地上
        Vector3 down = position.down(1);
        BlockPrototype downBlock = levelService.getBlockTypeAt(level, down);
        // 樹苗，只能种在泥土, 草方块, 灰化土上
        if (downBlock != BlockPrototype.PODZOL
                && downBlock != BlockPrototype.DIRT
                && downBlock != BlockPrototype.GRASS
                && downBlock != BlockPrototype.FARMLAND) {
            return false;
        }

        return true;
    }

    /**
     * 是否允许生长
     *
     * @param level
     * @param position
     * @param targetBlock
     * @return
     */
    private boolean isAllowGrowUp(Level level, Vector3 position, Block targetBlock, int treeTop) {
        // 光照不足
        if (!this.isBrightEnough(level, position)) {
            return false;
        }

        // 5 格以下免說
        if (treeTop < 5) {
            return false;
        }

        // 檢查目標格以下的高度
        boolean canGrow = saplingService.checkTreeSize(level, position, treeTop, 1);
        if (!canGrow) {
            return false;
        }

        // 單格樺樹或叢林或合金歡
        if (targetBlock.getMeta() == SaplingType.BIRCH_SAPLING.getMeta()
                || targetBlock.getMeta() == SaplingType.JUNGLE_SAPLING.getMeta()
                || targetBlock.getMeta() == SaplingType.ACACIA_SAPLING.getMeta()) {
            canGrow = saplingService.checkTreeSize(level, position, treeTop, 3);
            return canGrow;
        }


        // 若是深色橡树
        if (targetBlock.getMeta() == SaplingType.DARK_OAK_SAPLING.getMeta()) {
            // 檢查周邊樹苗
            return saplingService.hasAroundSapling(level, position, SaplingType.DARK_OAK_SAPLING);
        }

        // 叢林大樹柱型空間
        if (targetBlock.getMeta() == SaplingType.JUNGLE_SAPLING.getMeta()) {
            if (saplingService.hasAroundSapling(level, position, SaplingType.JUNGLE_SAPLING)) {
                canGrow = saplingService.checkTreeSize(level, position, treeTop, 5);
                return canGrow;
            }
        }

        // 雲杉樹柱型空間
        if (targetBlock.getMeta() == SaplingType.SPRUCE_SAPLING.getMeta()) {
            canGrow = saplingService.checkTreeSize(level, position, treeTop, 5);
            return canGrow;
        }

        return true;
    }

    /**
     * 判断周边的亮度
     *
     * @param level
     * @param position
     * @return
     */
    private boolean isBrightEnough(Level level, Vector3 position) {
        boolean isDay = this.levelService.isDay(level);
        if (isDay) {
            return true;
        }

        // TODO: 2019/6/21 临时关闭扫描操作，该操作会占用服务端约8%的性能
        /*
        // 火把光照等级为14
        for (int x = position.getX() - 5; x <= position.getX() + 5; x++) {
            for (int z = position.getZ() - 5; z < position.getZ() + 5; z++) {
                for (int y = position.getY() - 5; y <= position.getY() + 5; y++) {
                    if(y < 0)
                    {
                        y = 0;
                    }
                    if (x == position.getX() && z == position.getZ() && y == position.getY()) {
                        continue;
                    }
                    Block aroundBlock = levelService.getBlockAt(level, x, y, z);
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


    /**
     * 生成對應樹
     *
     * @param level
     * @param position
     * @param treeTop
     * @param targetBlock
     * @return
     */
    private void growTree(Level level, Vector3 position, int treeTop, Block targetBlock) {
        // 橡树
        if (targetBlock.getMeta() == SaplingType.OAK_SAPLING.getMeta()) {
            saplingService.growOakTree(level, position, treeTop, targetBlock);
        }

        // 云杉
        if (targetBlock.getMeta() == SaplingType.SPRUCE_SAPLING.getMeta()) {
            saplingService.growSpruceTree(level, position, treeTop, targetBlock);
        }

        // 白桦
        if (targetBlock.getMeta() == SaplingType.BIRCH_SAPLING.getMeta()) {
            saplingService.growOakTree(level, position, treeTop, targetBlock);
        }

        // 丛林
        if (targetBlock.getMeta() == SaplingType.JUNGLE_SAPLING.getMeta()) {
            saplingService.growOakTree(level, position, treeTop, targetBlock);
        }

        // 金合欢
        if (targetBlock.getMeta() == SaplingType.ACACIA_SAPLING.getMeta()) {
            saplingService.growAcaciaTree(level, position, treeTop, targetBlock);
        }

        // 深色橡树
        if (targetBlock.getMeta() == SaplingType.DARK_OAK_SAPLING.getMeta()) {
            saplingService.growOakTree(level, position, treeTop, targetBlock);
        }
    }


    /**
     * 根據樹苗決定大小
     *
     * @param targetBlock
     * @return
     */
    private int chanceSize(Level level, Vector3 targetPosition, Block targetBlock) {
        // 橡树
        if (targetBlock.getMeta() == SaplingType.OAK_SAPLING.getMeta()) {
            return random.nextInt(3) + 5;
        }

        // 云杉
        boolean isBigTree = saplingService.hasAroundSapling(level, targetPosition, SaplingType.SPRUCE_SAPLING);
        if (targetBlock.getMeta() == SaplingType.SPRUCE_SAPLING.getMeta()) {
            if (isBigTree) {
                return random.nextInt(4) + 8;
            } else {
                return random.nextInt(3) + 15;
            }
        }

        // 白桦
        if (targetBlock.getMeta() == SaplingType.BIRCH_SAPLING.getMeta()) {
            return random.nextInt(3) + 6;
        }

        // 丛林
        isBigTree = saplingService.hasAroundSapling(level, targetPosition, SaplingType.SPRUCE_SAPLING);
        if (targetBlock.getMeta() == SaplingType.JUNGLE_SAPLING.getMeta()) {
            if (isBigTree) {
                return random.nextInt(3) + 11;
            } else {
                return random.nextInt(3) + 7;
            }
        }

        // 金合欢
        if (targetBlock.getMeta() == SaplingType.ACACIA_SAPLING.getMeta()) {
            return random.nextInt(6) + 5;
        }

        // 深色橡树
        if (targetBlock.getMeta() == SaplingType.DARK_OAK_SAPLING.getMeta()) {
            return random.nextInt(3) + 7;
        }

        return -1;
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{
                SaplingModule.class,
                PlantGrowUpProgressModule.class,
                TransformModule.class
        };
    }

    @Override
    public SaplingSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new SaplingSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<SaplingSystem> getSystemClass() {
        return SaplingSystem.class;
    }
}
