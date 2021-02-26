package com.particle.game.block.planting;

import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.attribute.BlockAttributeService;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.geometry.BlockGeometry;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.events.level.block.BlockMushroomGrowEvent;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.DyeColor;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.MathUtils;
import com.particle.model.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;

@Singleton
public class MushroomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MushroomService.class);

    private static final int NORTH_WEST = 1;
    private static final int NORTH_EAST = 3;
    private static final int CENTER = 5;
    private static final int SOUTH_WEST = 7;
    private static final int SOUTH_EAST = 9;
    private static final int STEM = 10;
    private static final int ALL_INSIDE = 0;

    private final Random random = new Random();

    @Inject
    private LevelService levelService;

    @Inject
    private BlockService blockService;

    @Inject
    private BlockAttributeService blockAttributeService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    public void checkDyeAndGrow(Level level, ItemStack item, Vector3 targetPosition) {
        Block targetBlock = levelService.getBlockAt(level, targetPosition);
        if (isTargetDye(item) && isMushroom(targetBlock.getType())) {
            if (Math.random() < 0.4) {
                levelService.setBlockAt(level, Block.getBlock(BlockPrototype.AIR), targetPosition);

                Integer addedHeight = checkUnderBlockAndSpace(level, targetPosition);
                if (addedHeight != null) {
                    BlockMushroomGrowEvent event = new BlockMushroomGrowEvent(level);
                    event.setPosition(targetPosition);
                    event.setBlock(targetBlock);

                    eventDispatcher.dispatchEvent(event);

                    if (!event.isCancelled()) {
                        grow(level, targetBlock, targetPosition, addedHeight);
                    } else {
                        levelService.setBlockAt(level, targetBlock, targetPosition);
                    }
                } else {
                    levelService.setBlockAt(level, targetBlock, targetPosition);
                }
            }
        }

        addParticle();
    }

    public boolean checkEnvironment(Level level, Vector3 targetPosition) {
        Vector3 down = targetPosition.down(1);
        BlockPrototype downBlock = levelService.getBlockTypeAt(level, down);
        return downBlock == BlockPrototype.MYCELIUM || downBlock == BlockPrototype.PODZOL
                || (!downBlock.getBlockElement().isLiquid()
                && !this.blockAttributeService.isTransparency(downBlock) && levelService.getBlockLightAt(
                level, targetPosition.getX(), targetPosition.getY(), targetPosition.getZ()) < 13);
    }

    public boolean isTargetDye(ItemStack item) {
        return item.getItemType() == ItemPrototype.DYE && item.getMeta() == DyeColor.WHITE.getDyeData();
    }

    public boolean isMushroom(Level level, Vector3 position) {
        BlockPrototype block = levelService.getBlockTypeAt(level, position);
        return isMushroom(block);
    }

    private boolean isMushroom(BlockPrototype blockPrototype) {
        return blockPrototype == BlockPrototype.BROWN_MUSHROOM
                || blockPrototype == BlockPrototype.RED_MUSHROOM;
    }

    private Integer checkUnderBlockAndSpace(Level level, Vector3 position) {
        Vector3 pos2 = position.down();
        BlockPrototype underBlockType = levelService.getBlockTypeAt(level, pos2);
        if (underBlockType != BlockPrototype.DIRT
                && underBlockType != BlockPrototype.GRASS
                && underBlockType != BlockPrototype.MYCELIUM) {
            return null;
        }


        // 蘑菇长高的高度，长高前高度是1
        int addedHeight = MathUtils.getRandomNumberInRange(random, 4, 6);

        if (random.nextDouble() <= 1 / 12d) {
            // 有12分之1的几率会变成2倍高度
            addedHeight *= 2;
        }

        if (position.getY() < 1 || position.getY() + addedHeight + 1 >= 256) {
            // 超过高度限制了
            return null;
        }

        LOGGER.debug("target position is " + position);

        // 检测蘑菇生长的每个高度平面上的方块
        for (int checkedY = position.getY(); checkedY <= position.getY() + 1 + addedHeight; ++checkedY) {
            int checkedPlaneRange;

            if (checkedY <= position.getY() + 3) {
                // 茎的最下面四个方块（即生成在小蘑菇上方的三个方块）必须为空气或树叶
                checkedPlaneRange = 0;
            } else {
                // 在这之上的7×7×（高度−3）的区域同样不能有除了空气或树叶外的任何方块
                checkedPlaneRange = 3;
            }

            for (int checkedX = position.getX() - checkedPlaneRange; checkedX <= position.getX() + checkedPlaneRange; ++checkedX) {
                for (int checkedZ = position.getZ() - checkedPlaneRange; checkedZ <= position.getZ() + checkedPlaneRange; ++checkedZ) {
                    if (checkedY >= 0 && checkedY < 256) {
                        BlockPrototype block = levelService.getBlockTypeAt(level, checkedX, checkedY, checkedZ);

                        if (block != BlockPrototype.AIR && block != BlockPrototype.LEAVES) {
                            LOGGER.debug("Block type {} is not air or leaves, position {}",
                                    block, new Vector3(checkedX, checkedY, checkedZ));
                            return null;
                        } else {
                            LOGGER.debug("Checked block type {} is air or leaves, position {}",
                                    block, new Vector3(checkedX, checkedY, checkedZ));
                        }
                    } else {
                        LOGGER.debug("illegal checked y " + checkedY);
                        return null;
                    }
                }
            }
        }

        return addedHeight;
    }

    private void grow(Level level, Block mushroomBlock, Vector3 position, int addedHeight) {
        int realHeight;
        Block hugeMushroom;

        BlockPrototype mushroomBlockType = mushroomBlock.getType();
        if (mushroomBlockType == BlockPrototype.RED_MUSHROOM) {
            realHeight = position.getY() + addedHeight - 3;
            hugeMushroom = Block.getBlock(BlockPrototype.RED_MUSHROOM_BLOCK);
        } else {
            realHeight = position.getY() + addedHeight;
            hugeMushroom = Block.getBlock(BlockPrototype.BROWN_MUSHROOM_BLOCK);
        }

        for (int y = realHeight; y <= position.getY() + addedHeight; ++y) {
            int j3 = 1;

            if (y < position.getY() + addedHeight) {
                ++j3;
            }

            if (mushroomBlockType == BlockPrototype.BROWN_MUSHROOM) {
                j3 = 3;
            }

            int k3 = position.getX() - j3;
            int l3 = position.getX() + j3;
            int j1 = position.getZ() - j3;
            int k1 = position.getZ() + j3;

            for (int l1 = k3; l1 <= l3; ++l1) {
                for (int i2 = j1; i2 <= k1; ++i2) {
                    int j2 = 5;

                    if (l1 == k3) {
                        --j2;
                    } else if (l1 == l3) {
                        ++j2;
                    }

                    if (i2 == j1) {
                        j2 -= 3;
                    } else if (i2 == k1) {
                        j2 += 3;
                    }

                    int meta = j2;

                    if (mushroomBlockType == BlockPrototype.BROWN_MUSHROOM
                            || y < position.getY() + addedHeight) {
                        if ((l1 == k3 || l1 == l3) && (i2 == j1 || i2 == k1)) {
                            continue;
                        }

                        if (l1 == position.getX() - (j3 - 1) && i2 == j1) {
                            meta = NORTH_WEST;
                        }

                        if (l1 == k3 && i2 == position.getZ() - (j3 - 1)) {
                            meta = NORTH_WEST;
                        }

                        if (l1 == position.getX() + (j3 - 1) && i2 == j1) {
                            meta = NORTH_EAST;
                        }

                        if (l1 == l3 && i2 == position.getZ() - (j3 - 1)) {
                            meta = NORTH_EAST;
                        }

                        if (l1 == position.getX() - (j3 - 1) && i2 == k1) {
                            meta = SOUTH_WEST;
                        }

                        if (l1 == k3 && i2 == position.getZ() + (j3 - 1)) {
                            meta = SOUTH_WEST;
                        }

                        if (l1 == position.getX() + (j3 - 1) && i2 == k1) {
                            meta = SOUTH_EAST;
                        }

                        if (l1 == l3 && i2 == position.getZ() + (j3 - 1)) {
                            meta = SOUTH_EAST;
                        }
                    }

                    if (meta == CENTER && y < position.getY() + addedHeight) {
                        meta = ALL_INSIDE;
                    }

                    if (position.getY() >= position.getY() + addedHeight - 1 || meta != ALL_INSIDE) {
                        Vector3 blockPos = new Vector3(l1, y, i2);

                        if (levelService.getBlockTypeAt(level, blockPos).getBlockGeometry() != BlockGeometry.SOLID) {
                            hugeMushroom.setMeta(meta);
                            levelService.setBlockAt(level, hugeMushroom, blockPos);
                        }
                    }
                }
            }
        }

        for (int checkedHeight = 0; checkedHeight < addedHeight; ++checkedHeight) {
            Vector3 pos = position.up(checkedHeight);
            BlockPrototype block = levelService.getBlockTypeAt(level, pos);

            if (block.getBlockGeometry() != BlockGeometry.SOLID) {
                hugeMushroom.setMeta(STEM);
                levelService.setBlockAt(level, hugeMushroom, pos);
            }
        }

    }

    private void addParticle() {
        // TODO

    }
}
