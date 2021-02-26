package com.particle.game.block.liquid;

import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.events.level.block.BlockBreakEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;

import javax.inject.Inject;

public class LiquidService {
    private int waterTick = 0;
    private int lavaTick = 0;
    private final int dryWaterMeta = 7;
    private final int dryLavaMeta = 7;

    @Inject
    private LevelService levelService;

    @Inject
    private BlockService blockService;

    public boolean isFlowWaterTime(int tick) {
        if (waterTick % tick == 0) {
            return true;
        }

        return false;
    }

    public boolean isFlowLavaTime(int tick) {
        if (lavaTick % tick == 0) {
            return true;
        }

        return false;
    }

    public void addFlowWaterTick() {
        waterTick++;
    }

    public void addFlowLavaTick() {
        lavaTick++;
    }

    /**
     * 取得最低水位 meta
     */
    public int getDryWaterMeta() {
        return dryWaterMeta;
    }

    /**
     * 取得最低岩漿位 meta
     */
    public int getDryLavaMeta() {
        return dryLavaMeta;
    }

    /**
     * 四周是否有水
     *
     * @param level
     * @param targetPosition
     */
    public boolean isAroundWater(Level level, Vector3 targetPosition) {
        for (int i = 0; i < 4; i++) {
            Vector3 position = getPositionByNumber(targetPosition, i);
            BlockPrototype block = levelService.getBlockTypeAt(level, position);
            if (block == BlockPrototype.WATER || block == BlockPrototype.FLOWING_WATER) {
                return true;
            }
        }

        return false;
    }

    /**
     * 四周是否有岩漿
     *
     * @param level
     * @param targetPosition
     */
    public boolean isAroundLava(Level level, Vector3 targetPosition) {
        for (int i = 0; i < 4; i++) {
            Vector3 position = getPositionByNumber(targetPosition, i);
            BlockPrototype block = levelService.getBlockTypeAt(level, position);
            if (block == BlockPrototype.LAVA || block == BlockPrototype.FLOWING_LAVA) {
                return true;
            }
        }

        return false;
    }

    /**
     * 周圍有多少指定液體
     *
     * @param level
     * @param targetPosition
     */
    public int getAroundLiquidCount(Level level, Vector3 targetPosition, BlockPrototype blockPrototype) {
        int count = 0;

        for (int i = 0; i < 4; i++) {
            Vector3 position = getPositionByNumber(targetPosition, i);
            BlockPrototype block = levelService.getBlockTypeAt(level, position);
            if (block == blockPrototype) {
                count++;
            }
        }

        return count;
    }

    /**
     * 檢查周圍是否有比自己高階的水
     *
     * @param level
     * @param targetPosition
     */
    public boolean checkAroundHighLevelWater(Level level, Block targetBlock, Vector3 targetPosition) {
        for (int i = 0; i < 4; i++) {
            Vector3 position = getPositionByNumber(targetPosition, i);
            Block block = levelService.getBlockAt(level, position);
            if (isHigherLevelWater(block, targetBlock)) {
                return true;
            }
        }

        BlockPrototype upBlock = levelService.getBlockTypeAt(level, targetPosition.up());
        // 上方有水的話
        if (upBlock == BlockPrototype.FLOWING_WATER || upBlock == BlockPrototype.WATER) {
            return true;
        }

        return false;
    }

    /**
     * 檢查周圍是否有比自己高階的岩漿
     *
     * @param level
     * @param targetPosition
     */
    public boolean checkAroundHighLevelLava(Level level, Block targetBlock, Vector3 targetPosition) {
        for (int i = 0; i < 4; i++) {
            Vector3 position = getPositionByNumber(targetPosition, i);
            Block block = levelService.getBlockAt(level, position);
            if (isHigherLevelLava(block, targetBlock)) {
                return true;
            }
        }

        BlockPrototype upBlock = levelService.getBlockTypeAt(level, targetPosition.up());
        // 上方有岩漿的話
        if (upBlock == BlockPrototype.FLOWING_LAVA || upBlock == BlockPrototype.LAVA) {
            return true;
        }

        return false;
    }

    /**
     * 判斷目標是比自己高階的水
     *
     * @param block       周圍指定方塊
     * @param targetBlock 當前方塊
     */
    private boolean isHigherLevelWater(Block block, Block targetBlock) {
        if ((block.getType() == BlockPrototype.FLOWING_WATER || block.getType() == BlockPrototype.WATER)
                && block.getMeta() < targetBlock.getMeta()) {
            return true;
        }

        return false;
    }

    /**
     * 判斷目標是比自己高階的岩漿
     *
     * @param block       周圍指定方塊
     * @param targetBlock 當前方塊
     */
    private boolean isHigherLevelLava(Block block, Block targetBlock) {
        if ((block.getType() == BlockPrototype.FLOWING_LAVA || block.getType() == BlockPrototype.LAVA)
                && block.getMeta() < targetBlock.getMeta()) {
            return true;
        }

        return false;
    }

    /**
     * 有東西則破壞
     *
     * @param level
     * @param position
     */
    public void breakBlock(Level level, Block block, Vector3 position) {
        if (block.getType() != BlockPrototype.AIR) {
            this.blockService.breakBlockByLevel(level, position, BlockBreakEvent.Caused.LEVEL);
        }
    }

    /**
     * 開始乾涸
     *
     * @param level
     * @param position
     * @param meta            當前 meta 值
     * @param dryMeta         乾涸的 meta 值
     * @param targetBlockType 變成目標 blockType
     */
    public void changeAir(Level level, Block block, Vector3 position, int meta, int dryMeta, BlockPrototype targetBlockType, int drySpeed) {
        Block changeBlock;
        boolean isAroundLiquid = (block.getType() == BlockPrototype.FLOWING_LAVA && !isAroundLava(level, position))
                || (block.getType() == BlockPrototype.FLOWING_WATER && !isAroundWater(level, position));

        if (block.getMeta() >= dryMeta || isAroundLiquid) {
            changeBlock = Block.getBlock(BlockPrototype.AIR);
            this.levelService.setBlockAt(level, changeBlock, position);
            blockService.placeBlockByLevel(level, changeBlock, null, position, position);
        }
        // 源是 0, 源不滅
        else if (block.getMeta() > 0) {
            changeBlock = Block.getBlock(targetBlockType);
            meta = meta + drySpeed;
            if (meta > dryMeta) {
                meta = dryMeta;
            }
            changeBlock.setMeta(meta);
            this.levelService.setBlockAt(level, changeBlock, position);
            // 初始化对应的blockEntity
            blockService.placeBlockByLevel(level, changeBlock, null, position, position);
        }
    }


    /**
     * 變成流體
     *
     * @param level
     * @param position
     * @param meta            當前 meta 值
     * @param targetBlockType 變成目標 blockType
     */
    public Block changeFlowingBlock(Level level, Block block, Vector3 position, int meta, BlockPrototype targetBlockType) {
        breakBlock(level, block, position);
        //更新方块
        Block changeBlock = Block.getBlock(targetBlockType);
        changeBlock.setMeta(meta + 1);
        this.levelService.setBlockAt(level, changeBlock, position);
        // 初始化对应的blockEntity
        blockService.placeBlockByLevel(level, changeBlock, null, position, position);

        return changeBlock;
    }

    /**
     * 變成石頭
     *
     * @param level
     * @param targetPosition
     * @param blockPrototype 變成目標 blockType
     */
    public Block changeStoneBlock(Level level, Vector3 targetPosition, BlockPrototype blockPrototype) {
        Block changeBlock = Block.getBlock(blockPrototype);
        levelService.setBlockAt(level, changeBlock, targetPosition);
        blockService.placeBlockByLevel(level, changeBlock, null, targetPosition, targetPosition);

        return changeBlock;
    }

    /**
     * 用數字取得方位
     *
     * @param targetPosition
     * @param number
     */
    public Vector3 getPositionByNumber(Vector3 targetPosition, int number) {
        switch (number) {
            case 0:
                return targetPosition.east();
            case 1:
                return targetPosition.north();
            case 2:
                return targetPosition.west();
            case 3:
                return targetPosition.south();
        }

        return targetPosition;
    }
}
