package com.particle.game.block.planting;

import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.item.types.LeavesType;
import com.particle.model.item.types.LogType;
import com.particle.model.item.types.SaplingType;
import com.particle.model.level.Level;
import com.particle.model.math.BlockFace;
import com.particle.model.math.Vector3;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;

/**
 * 樹苗
 */
@Singleton
public class SaplingService {
    @Inject
    private Random random;

    @Inject
    private LevelService levelService;

    /**
     * 檢查成長? 抄 nukkit 的
     * key：药水id
     * val：药水记录
     */
    public boolean canGrowInto(BlockPrototype block) {
        return block == BlockPrototype.AIR
                || block == BlockPrototype.LEAVES
                || block == BlockPrototype.GRASS
                || block == BlockPrototype.DIRT
                || block == BlockPrototype.LOG
                || block == BlockPrototype.LOG2
                || block == BlockPrototype.SAPLING
                || block == BlockPrototype.VINE;
    }

    /**
     * 是否周邊4格有對應樹苗
     * key：药水id
     * val：药水记录
     */
    public boolean hasAroundSapling(Level level, Vector3 position, SaplingType saplingType) {
        // 若東邊有樹苗
        Block block = levelService.getBlockAt(level, position.east());
        if (block.getType() == BlockPrototype.SAPLING
                && block.getMeta() == saplingType.getMeta()) {
            block = levelService.getBlockAt(level, position.east().south());
            // 若東邊的南邊有樹苗
            if ((block.getType() == BlockPrototype.SAPLING
                    && block.getMeta() == saplingType.getMeta())) {
                // 若南邊有樹苗
                block = levelService.getBlockAt(level, position.south());
                if ((block.getType() == BlockPrototype.SAPLING
                        && block.getMeta() == saplingType.getMeta())) {
                    // 四周樹苗條件符合
                    return true;
                }
            }

            // 找東邊的北邊
            block = levelService.getBlockAt(level, position.east().north());
            // 若東邊的北邊有樹苗
            if ((block.getType() == BlockPrototype.SAPLING
                    && block.getMeta() == saplingType.getMeta())) {
                // 若北邊有樹苗
                block = levelService.getBlockAt(level, position.north());
                if ((block.getType() == BlockPrototype.SAPLING
                        && block.getMeta() == saplingType.getMeta())) {
                    // 四周樹苗條件符合
                    return true;
                }
            }
        }

        // 若西邊有樹苗
        block = levelService.getBlockAt(level, position.west());
        if (block.getType() == BlockPrototype.SAPLING
                && block.getMeta() == saplingType.getMeta()) {
            block = levelService.getBlockAt(level, position.west().south());
            // 若西邊的南邊有樹苗
            if ((block.getType() == BlockPrototype.SAPLING
                    && block.getMeta() == saplingType.getMeta())) {
                // 若南邊有樹苗
                block = levelService.getBlockAt(level, position.south());
                if ((block.getType() == BlockPrototype.SAPLING
                        && block.getMeta() == saplingType.getMeta())) {
                    // 四周樹苗條件符合
                    return true;
                }
            }

            // 找西邊的北邊
            block = levelService.getBlockAt(level, position.west().north());
            // 若西邊的北邊有樹苗
            if ((block.getType() == BlockPrototype.SAPLING
                    && block.getMeta() == saplingType.getMeta())) {
                // 若北邊有樹苗
                block = levelService.getBlockAt(level, position.north());
                if ((block.getType() == BlockPrototype.SAPLING
                        && block.getMeta() == saplingType.getMeta())) {
                    // 四周樹苗條件符合
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 取得對應的樹木方塊
     *
     * @param targetBlock
     * @return
     */
    public Block getTargetLogBlock(Block targetBlock) {
        Block block = Block.getBlock(BlockPrototype.AIR);
        // 橡树
        if (targetBlock.getMeta() == SaplingType.OAK_SAPLING.getMeta()) {
            block = Block.getBlock(BlockPrototype.LOG);
            block.setMeta(LogType.OAK_LOG.getMeta());
        }

        // 云杉
        if (targetBlock.getMeta() == SaplingType.SPRUCE_SAPLING.getMeta()) {
            block = Block.getBlock(BlockPrototype.LOG);
            block.setMeta(LogType.SPRUCE_LOG.getMeta());
        }

        // 白桦
        if (targetBlock.getMeta() == SaplingType.BIRCH_SAPLING.getMeta()) {
            block = Block.getBlock(BlockPrototype.LOG);
            block.setMeta(LogType.BIRCH_LOG.getMeta());
        }

        // 丛林
        if (targetBlock.getMeta() == SaplingType.JUNGLE_SAPLING.getMeta()) {
            block = Block.getBlock(BlockPrototype.LOG);
            block.setMeta(LogType.JUNGLE_LOG.getMeta());
        }

        // 金合欢
        if (targetBlock.getMeta() == SaplingType.ACACIA_SAPLING.getMeta()) {
            block = Block.getBlock(BlockPrototype.LOG2);
            block.setMeta(LogType.ACACIA_LOG.getMeta());
        }

        // 深色橡树
        if (targetBlock.getMeta() == SaplingType.DARK_OAK_SAPLING.getMeta()) {
            block = Block.getBlock(BlockPrototype.LOG2);
            block.setMeta(LogType.DARK_OAK_LOG.getMeta());
        }

        return block;
    }

    /**
     * 取得對應的樹葉方塊
     *
     * @param targetBlock
     * @return
     */
    public Block getTargetLeavesBlock(Block targetBlock) {
        Block block = Block.getBlock(BlockPrototype.AIR);
        // 橡树
        if (targetBlock.getMeta() == SaplingType.OAK_SAPLING.getMeta()) {
            block = Block.getBlock(BlockPrototype.LEAVES);
            block.setMeta(LeavesType.OAK_LEAVES.getMeta());
        }

        // 云杉
        if (targetBlock.getMeta() == SaplingType.SPRUCE_SAPLING.getMeta()) {
            block = Block.getBlock(BlockPrototype.LEAVES);
            block.setMeta(LeavesType.SPRUCE_LEAVES.getMeta());
        }

        // 白桦
        if (targetBlock.getMeta() == SaplingType.BIRCH_SAPLING.getMeta()) {
            block = Block.getBlock(BlockPrototype.LEAVES);
            block.setMeta(LeavesType.BIRCH_LEAVES.getMeta());
        }

        // 丛林
        if (targetBlock.getMeta() == SaplingType.JUNGLE_SAPLING.getMeta()) {
            block = Block.getBlock(BlockPrototype.LEAVES);
            block.setMeta(LeavesType.JUNGLE_LEAVES.getMeta());
        }

        // 金合欢
        if (targetBlock.getMeta() == SaplingType.ACACIA_SAPLING.getMeta()) {
            block = Block.getBlock(BlockPrototype.LEAVES2);
            block.setMeta(LeavesType.ACACIA_LEAVES.getMeta());
        }

        // 深色橡树
        if (targetBlock.getMeta() == SaplingType.DARK_OAK_SAPLING.getMeta()) {
            block = Block.getBlock(BlockPrototype.LEAVES2);
            block.setMeta(LeavesType.DARK_OAK_LEAVES.getMeta());
        }

        return block;
    }

    /**
     * 檢查生成樹的高度與寬度
     *
     * @return
     */
    public boolean checkTreeSize(Level level, Vector3 position, int treeTop, int range) {
        // treeTop 格以下有除了空氣及樹葉之外的東西, 所有都無法成長
        for (int i = 0; i < treeTop; i++) {
            BlockPrototype block = levelService.getBlockTypeAt(level, position.up(i + 1));
            if (block != BlockPrototype.AIR && block != BlockPrototype.LEAVES && block != BlockPrototype.LEAVES2) {
                return false;
            }
        }

        if (range > 1) {
            // 柱型範圍檢查
            for (int j = 0; j < range; j++) {
                for (int k = 0; k < range; k++) {
                    for (int i = 0; i < treeTop; i++) {
                        BlockPrototype block = levelService.getBlockTypeAt(level, position.west((int) (range * (-0.5)) + j).north((int) (range * (-0.5)) + k).up(i + 1));
                        if (block != BlockPrototype.AIR && block != BlockPrototype.LEAVES && block != BlockPrototype.LEAVES2) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * 生長成正常樹
     *
     * @param level
     * @param position
     * @param targetBlock
     * @return
     */
    private void growNormalTree(Level level, Vector3 position, Block targetBlock, int treeTop, int leavesFloor) {
        Block logBlock = getTargetLogBlock(targetBlock);

        // 先長樹幹
        for (int i = 0; i < treeTop; i++) {
            levelService.setBlockAt(level, logBlock, position.up(i));
        }

        Block leavesBlock = getTargetLeavesBlock(targetBlock);
        // 最頂部的葉子
        levelService.setBlockAt(level, leavesBlock, position.up(treeTop));

        BlockPrototype block;
        // 再往下長葉子
        for (int i = 0; i < leavesFloor; i++) {
            for (int j = 0; j < leavesFloor; j++) {
                // 各種嘗試計算得出這樣結果目前最好, 不要問為什麼
                for (int k = 0; k < leavesFloor; k++) {
                    // i-j 為取一象限, k+j 為控制葉子最低處
                    if (i - j <= 0 && k + j < leavesFloor) {
                        block = levelService.getBlockTypeAt(level, position.up(treeTop - k - 1 - j).east(i + 1).north(i - j));
                        if (block == BlockPrototype.AIR) {
                            levelService.setBlockAt(level, leavesBlock, position.up(treeTop - k - 1 - j).east(i + 1).north(i - j));
                        }

                        block = levelService.getBlockTypeAt(level, position.up(treeTop - k - 1 - j).west(i + 1).south(i - j));
                        if (block == BlockPrototype.AIR) {
                            levelService.setBlockAt(level, leavesBlock, position.up(treeTop - k - 1 - j).west(i + 1).south(i - j));
                        }

                        block = levelService.getBlockTypeAt(level, position.up(treeTop - k - 1 - j).south(i + 1).east(i - j));
                        if (block == BlockPrototype.AIR) {
                            levelService.setBlockAt(level, leavesBlock, position.up(treeTop - k - 1 - j).south(i + 1).east(i - j));
                        }

                        block = levelService.getBlockTypeAt(level, position.up(treeTop - k - 1 - j).north(i + 1).west(i - j));
                        if (block == BlockPrototype.AIR) {
                            levelService.setBlockAt(level, leavesBlock, position.up(treeTop - k - 1 - j).north(i + 1).west(i - j));
                        }
                    }
                }
            }
        }
    }

    /**
     * 生成橡樹
     *
     * @return
     */
    public void growOakTree(Level level, Vector3 position, int treeTop, Block targetBlock) {
        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        Block logBlock = getTargetLogBlock(targetBlock);
        Block leavesBlock = getTargetLeavesBlock(targetBlock);

        // 先長樹幹
        for (int i = 0; i < treeTop; i++) {
            levelService.setBlockAt(level, logBlock, position.up(i));
        }

        // 抄了 nukkit ObjectTree
        for (int yy = y - 3 + treeTop; yy <= y + treeTop; ++yy) {
            double yOff = yy - (y + treeTop);
            int mid = (int) (1 - yOff / 2);
            for (int xx = x - mid; xx <= x + mid; ++xx) {
                int xOff = Math.abs(xx - x);
                for (int zz = z - mid; zz <= z + mid; ++zz) {
                    int zOff = Math.abs(zz - z);
                    if (xOff == mid && zOff == mid && (yOff == 0 || random.nextInt(2) == 0)) {
                        continue;
                    }

                    BlockPrototype block = levelService.getBlockTypeAt(level, new Vector3(xx, yy, zz));
                    if (block == BlockPrototype.AIR) {
                        levelService.setBlockAt(level, leavesBlock, new Vector3(xx, yy, zz));
                    }
                }
            }
        }
    }

    /**
     * 生成樺樹
     *
     * @return
     */
    public void growSpruceTree(Level level, Vector3 position, int treeTop, Block targetBlock) {
        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        Block logBlock = getTargetLogBlock(targetBlock);
        Block leavesBlock = getTargetLeavesBlock(targetBlock);

        // 先長樹幹
        int top = treeTop - random.nextInt(3);
        for (int i = 0; i < top; i++) {
            levelService.setBlockAt(level, logBlock, position.up(i));
        }

        // 抄了 nukkit SpruceTree
        int topSize = treeTop - (1 + random.nextInt(2));
        int lRadius = 2 + random.nextInt(2);

        int radius = random.nextInt(2);
        int maxR = 1;
        int minR = 0;

        for (int yy = 0; yy <= topSize; ++yy) {
            int yyy = y + treeTop - yy;

            for (int xx = x - radius; xx <= x + radius; ++xx) {
                int xOff = Math.abs(xx - x);
                for (int zz = z - radius; zz <= z + radius; ++zz) {
                    int zOff = Math.abs(zz - z);
                    if (xOff == radius && zOff == radius && radius > 0) {
                        continue;
                    }

                    BlockPrototype block = levelService.getBlockTypeAt(level, new Vector3(xx, yyy, zz));
                    if (block == BlockPrototype.AIR) {
                        levelService.setBlockAt(level, leavesBlock, new Vector3(xx, yyy, zz));
                    }
                }
            }

            if (radius >= maxR) {
                radius = minR;
                minR = 1;
                if (++maxR > lRadius) {
                    maxR = lRadius;
                }
            } else {
                ++radius;
            }
        }
    }


    /**
     * 生成金合歡樹
     *
     * @return
     */
    public void growAcaciaTree(Level level, Vector3 position, int treeTop, Block targetBlock) {
        boolean flag = true;
        Block leavesBlock = getTargetLeavesBlock(targetBlock);
        Block logBlock = getTargetLogBlock(targetBlock);

        // 本身變成樹幹
        levelService.setBlockAt(level, logBlock, position);

        if (position.getY() >= 1 && position.getY() + treeTop + 1 <= 256) {
            for (int j = position.getY(); j <= position.getY() + 1 + treeTop; ++j) {
                int k = 1;

                if (j == position.getY()) {
                    k = 0;
                }

                if (j >= position.getY() + 1 + treeTop - 2) {
                    k = 2;
                }

                Vector3 vector3 = new Vector3(0, 0, 0);

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
                        if (j >= 0 && j < 256) {
                            vector3.setX(l);
                            vector3.setY(j);
                            vector3.setZ(i1);

                            if (!this.canGrowInto(levelService.getBlockTypeAt(level, vector3))) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return;
            } else {
                Vector3 down = position.down();
                BlockPrototype block = levelService.getBlockTypeAt(level, down);

                if ((block == BlockPrototype.GRASS || block == BlockPrototype.DIRT) && position.getY() < 256 - treeTop - 1) {
                    BlockFace face = BlockFace.Plane.HORIZONTAL.random(random);
                    int k2 = treeTop - random.nextInt(4) - 1;
                    int l2 = 3 - random.nextInt(3);
                    int i3 = position.getX();
                    int j1 = position.getZ();
                    int k1 = 0;

                    for (int l1 = 0; l1 < treeTop; ++l1) {
                        int i2 = position.getY() + l1;

                        if (l1 >= k2 && l2 > 0) {
                            i3 += face.getXOffset();
                            j1 += face.getZOffset();
                            --l2;
                        }

                        Vector3 blockpos = new Vector3(i3, i2, j1);
                        BlockPrototype material = levelService.getBlockTypeAt(level, blockpos);

                        if (material == BlockPrototype.AIR || material == BlockPrototype.LEAVES) {
                            levelService.setBlockAt(level, logBlock, blockpos);
                            k1 = i2;
                        }
                    }

                    Vector3 blockpos2 = new Vector3(i3, k1, j1);

                    for (int j3 = -3; j3 <= 3; ++j3) {
                        for (int i4 = -3; i4 <= 3; ++i4) {
                            if (Math.abs(j3) != 3 || Math.abs(i4) != 3) {
                                levelService.setBlockAt(level, leavesBlock, blockpos2.add(j3, 0, i4));
                            }
                        }
                    }

                    blockpos2 = blockpos2.up();

                    for (int k3 = -1; k3 <= 1; ++k3) {
                        for (int j4 = -1; j4 <= 1; ++j4) {
                            levelService.setBlockAt(level, leavesBlock, blockpos2.add(k3, 0, j4));
                        }
                    }

                    levelService.setBlockAt(level, leavesBlock, blockpos2.east(2));
                    levelService.setBlockAt(level, leavesBlock, blockpos2.west(2));
                    levelService.setBlockAt(level, leavesBlock, blockpos2.south(2));
                    levelService.setBlockAt(level, leavesBlock, blockpos2.north(2));
                    i3 = position.getX();
                    j1 = position.getZ();
                    BlockFace face1 = BlockFace.Plane.HORIZONTAL.random(random);

                    if (face1 != face) {
                        int l3 = k2 - random.nextInt(2) - 1;
                        int k4 = 1 + random.nextInt(3);
                        k1 = 0;

                        for (int l4 = l3; l4 < treeTop && k4 > 0; --k4) {
                            if (l4 >= 1) {
                                int j2 = position.getY() + l4;
                                i3 += face1.getXOffset();
                                j1 += face1.getZOffset();
                                Vector3 blockpos1 = new Vector3(i3, j2, j1);
                                BlockPrototype material = levelService.getBlockTypeAt(level, blockpos1);

                                if (material == BlockPrototype.AIR || material == BlockPrototype.LEAVES) {
                                    levelService.setBlockAt(level, logBlock, blockpos1);
                                    k1 = j2;
                                }
                            }

                            ++l4;
                        }

                        if (k1 > 0) {
                            Vector3 blockpos3 = new Vector3(i3, k1, j1);

                            for (int i5 = -2; i5 <= 2; ++i5) {
                                for (int k5 = -2; k5 <= 2; ++k5) {
                                    if (Math.abs(i5) != 2 || Math.abs(k5) != 2) {
                                        levelService.setBlockAt(level, leavesBlock, blockpos3.add(i5, 0, k5));
                                    }
                                }
                            }

                            blockpos3 = blockpos3.up();

                            for (int j5 = -1; j5 <= 1; ++j5) {
                                for (int l5 = -1; l5 <= 1; ++l5) {

                                    BlockPrototype blockPrototype = levelService.getBlockTypeAt(level, blockpos3.add(j5, 0, l5));
                                    if (blockPrototype == BlockPrototype.AIR || blockPrototype == BlockPrototype.LEAVES) {
                                        levelService.setBlockAt(level, leavesBlock, blockpos3.add(j5, 0, l5));
                                    }
                                }
                            }
                        }
                    }

                    return;
                }
            }
        }
    }
}
