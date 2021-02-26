package com.particle.game.block.interactor;

import com.particle.game.block.interactor.processor.updater.*;
import com.particle.game.block.interactor.processor.updater.planting.*;
import com.particle.game.block.interactor.processor.updater.switches.BlockButtonUpdater;
import com.particle.game.block.interactor.processor.updater.switches.BlockLeverUpdater;
import com.particle.game.block.interactor.processor.world.*;
import com.particle.game.block.interactor.processor.world.container.*;
import com.particle.game.block.interactor.processor.world.direction.*;
import com.particle.game.block.interactor.processor.world.planting.*;
import com.particle.game.block.interactor.processor.world.tool.BlockCauldronWorldProcessor;
import com.particle.game.block.interactor.processor.world.tool.BlockLeavesWorldProcessor;
import com.particle.game.block.interactor.processor.world.tool.BlockSnowWorldProcessor;
import com.particle.game.block.interactor.processor.world.tool.BlockVineWorldProcessor;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class BlockWorldService implements IBlockWorldProcessor {

    private Map<BlockPrototype, IBlockWorldProcessor> handlers = new ConcurrentHashMap<>();

    private Map<BlockPrototype, List<IBlockWorldUpdater>> blockUpdaterHandlers = new ConcurrentHashMap<>();

    @Inject
    private LevelService levelService;

    @Inject
    private DefaultBlockProcessor defaultBlockProcessor;

    @Inject
    private BlockBedRockWorldProcessor blockBedRockWorldProcessor;

    @Inject
    private BlockInvisibleBedrockWorldProcessor blockInvisibleBedrockWorldProcessor;


    @Inject
    private BlockPortalWorldProcessor blockPortalWorldProcessor;

    @Inject
    private BlockStoneWorldProcessor blockStoneWorldProcessor;

    @Inject
    private BlockStairsWorldProcessor blockStairsWorldProcessor;

    @Inject
    private BlockDoorWorldProcessor blockDoorWorldProcessor;

    @Inject
    private BlockTorchWorldProcessor blockTorchWorldProcessor;

    @Inject
    private BlockLadderProcessor blockLadderProcessor;

    @Inject
    private BlockButtonWorldProcessor blockButtonWorldProcessor;

    @Inject
    private BlockLeverWorldProcessor blockLeverWorldProcessor;

    @Inject
    private BlockChestWorldProcessor blockChestWorldProcessor;

    @Inject
    private BlockBrewingStandWorldProcessor blockBrewingStandWorldProcessor;

    @Inject
    private BlockAnvilWorldProcessor blockAnvilWorldProcessor;

    @Inject
    private BlockFarmlandWorldProcessor blockFarmlandWorldProcessor;

    @Inject
    private BlockFurnaceWorldProcessor blockFurnaceWorldProcessor;

    @Inject
    private BlockEnderChestWorldProcessor blockEnderChestWorldProcessor;

    @Inject
    private BlockCactusWorldProcessor blockCactusWorldProcessor;

    @Inject
    private BlockCakePlaced blockCakePlaced;

    @Inject
    private BlockCocoaPlaced blockCocoaPlaced;

    @Inject
    private BlockNetherWartWorldProcessor blockNetherWartWorldProcessor;

    @Inject
    private BlockReedsWorldProcessor blockReedsWorldProcessor;

    @Inject
    private BlockWaterlilyWorldProcessor blockWaterlilyWorldProcessor;

    @Inject
    private BlockMushroomWorldProcessor blockMushroomWorldProcessor;

    @Inject
    private LuckyEnchantAmountBlockProcessor luckyEnchantAmountBlockProcessor;
    @Inject
    private LuckyEnchantAmountLapisProcessor luckyEnchantAmountLapisProcessor;

    @Inject
    private BlockCropsWorldProcessor blockCropsWorldProcessor;

    @Inject
    private BlockSaplingWorldProcessor blockSaplingWorldProcessor;

    @Inject
    private BlockDoubleFlowerWorldProcessor blockDoubleFlowerWorldProcessor;

    @Inject
    private BlockCauldronWorldProcessor blockCauldronWorldProcessor;

    @Inject
    private BlockLeavesWorldProcessor blockLeavesWorldProcessor;

    @Inject
    private BlockSnowWorldProcessor blockSnowWorldProcessor;

    @Inject
    private BlockVineWorldProcessor blockVineWorldProcessor;

    @Inject
    private BlockDirtPlaceProcessor blockDirtPlaceProcessor;

    @Inject
    private BlockSlabsWorldProcessor blockSlabsWorldProcessor;

    @Inject
    private BlockFireWorldProcessor blockFireWorldProcessor;

    @Inject
    private BlockLogWorldProcessor blockLogWorldProcessor;

    @Inject
    private BlockSignWorldProcessor blockSignWorldProcessor;

    // 家具相關
    @Inject
    private BlockFurnitureProcessor blockFurnitureProcessor;

    @Inject
    private BlockBannerWorldProcessor blockBannerWorldProcessor;

    @Inject
    private BlockLoomWorldProcessor blockLoomWorldProcessor;

    @Inject
    private BlockSkullWorldProcessor blockSkullWorldProcessor;


    //------------ Updater 相关处理逻辑 ----------------------
    @Inject
    private DestoryOnHangingProcessor destoryOnHangingProcessor;

    @Inject
    private FallingBlockUpdater fallingBlockUpdater;

    @Inject
    private BlockCactusUpdater blockCactusUpdater;

    @Inject
    private BlockCakeUpdater blockCakeUpdater;

    @Inject
    private BlockCocoaUpdater blockCocoaUpdater;

    @Inject
    private BlockDoorUpdater blockDoorUpdater;

    // 花朵
    @Inject
    private BlockFlowerUpdater blockFlowerUpdater;

    @Inject
    private BlockMushroomWorldUpdater blockMushroomWorldUpdater;

    @Inject
    private BlockNetherWartUpdater blockNetherWartUpdater;

    @Inject
    private BlockReedsUpdater blockReedsUpdater;

    @Inject
    private BlockSandUpdater blockSandUpdater;

    @Inject
    private BlockWaterUpdater blockWaterUpdater;

    @Inject
    private BlockLavaUpdater blockLavaUpdater;

    @Inject
    private BlockStemUpdater blockStemUpdater;

    @Inject
    private BlockDestroyUpdater blockDestroyUpdater;

    @Inject
    private BlockIceUpdater blockIceUpdater;

    @Inject
    private BlockFireUpdater blockFireUpdater;

    @Inject
    private BlockDirtUpdater blockDirtUpdater;

    @Inject
    private BlockBreakOnHangUpdater blockBreakOnHangUpdater;

    @Inject
    private BlockButtonUpdater blockButtonUpdater;

    @Inject
    private BlockSignUpdater blockSignUpdater;

    @Inject
    private BlockLeverUpdater blockLeverUpdater;

    @Inject
    private BlockTouchUpdater blockTouchUpdater;

    @Inject
    private BlockLadderUpdater blockLadderUpdater;

    @Inject
    public void init() {
        // 箱子
        this.handlers.put(BlockPrototype.CHEST, blockChestWorldProcessor);

        // 石頭
        this.handlers.put(BlockPrototype.STONE, blockStoneWorldProcessor);

        // 基岩
        this.handlers.put(BlockPrototype.BEDROCK, blockBedRockWorldProcessor);
        this.handlers.put(BlockPrototype.INVISIBLEBEDROCK, blockInvisibleBedrockWorldProcessor);

        // 傳送門
        this.handlers.put(BlockPrototype.PORTAL, blockPortalWorldProcessor);

        // 酿造台
        this.handlers.put(BlockPrototype.BREWING_STAND, blockBrewingStandWorldProcessor);
        // 铁砧
        this.handlers.put(BlockPrototype.ANVIL, blockAnvilWorldProcessor);
        // 熔炉
        this.handlers.put(BlockPrototype.FURNACE, blockFurnaceWorldProcessor);
        // 燃烧的熔炉
        this.handlers.put(BlockPrototype.LIT_FURNACE, blockFurnaceWorldProcessor);
        // 末影箱
        this.handlers.put(BlockPrototype.ENDER_CHEST, blockEnderChestWorldProcessor);
        // 耕地
        this.handlers.put(BlockPrototype.FARMLAND, blockFarmlandWorldProcessor);

        // 仙人掌
        this.handlers.put(BlockPrototype.CACTUS, blockCactusWorldProcessor);

        // 蛋糕
        this.handlers.put(BlockPrototype.CAKE, blockCakePlaced);

        this.handlers.put(BlockPrototype.COCOA, blockCocoaPlaced);

        // 地獄疙瘩
        this.handlers.put(BlockPrototype.NETHER_WART, blockNetherWartWorldProcessor);

        // 蘆薈
        this.handlers.put(BlockPrototype.REEDS, blockReedsWorldProcessor);

        // 睡蓮
        this.handlers.put(BlockPrototype.WATERLILY, blockWaterlilyWorldProcessor);

        // 概率矿物
        this.handlers.put(BlockPrototype.GLOWSTONE, luckyEnchantAmountBlockProcessor);
        this.handlers.put(BlockPrototype.DIAMOND_ORE, luckyEnchantAmountBlockProcessor);
        this.handlers.put(BlockPrototype.COAL_ORE, luckyEnchantAmountBlockProcessor);
        this.handlers.put(BlockPrototype.LAPIS_ORE, luckyEnchantAmountLapisProcessor);

        // 小麦
        this.handlers.put(BlockPrototype.WHEAT, blockCropsWorldProcessor);
        // 马铃薯
        this.handlers.put(BlockPrototype.POTATOES, blockCropsWorldProcessor);
        // 胡萝卜
        this.handlers.put(BlockPrototype.CARROTS, blockCropsWorldProcessor);
        // 甜菜根
        this.handlers.put(BlockPrototype.BEETROOT, blockCropsWorldProcessor);
        // 西瓜
        this.handlers.put(BlockPrototype.MELON_STEM, blockCropsWorldProcessor);
        // 南瓜
        this.handlers.put(BlockPrototype.PUMPKIN_STEM, blockCropsWorldProcessor);

        // 樹苗
        this.handlers.put(BlockPrototype.SAPLING, blockSaplingWorldProcessor);

        // 蘑菇
        this.handlers.put(BlockPrototype.RED_MUSHROOM, blockMushroomWorldProcessor);
        this.handlers.put(BlockPrototype.BROWN_MUSHROOM, blockMushroomWorldProcessor);

        // 双层花
        this.handlers.put(BlockPrototype.DOUBLE_PLANT, blockDoubleFlowerWorldProcessor);

        // 炼药锅
        this.handlers.put(BlockPrototype.CAULDRON, this.blockCauldronWorldProcessor);

        // 雪
        this.handlers.put(BlockPrototype.SNOW_LAYER, this.blockSnowWorldProcessor);
        this.handlers.put(BlockPrototype.SNOW, this.blockSnowWorldProcessor);

        // 樹葉
        this.handlers.put(BlockPrototype.LEAVES, this.blockLeavesWorldProcessor);
        this.handlers.put(BlockPrototype.LEAVES2, this.blockLeavesWorldProcessor);

        // 藤蔓
        this.handlers.put(BlockPrototype.VINE, this.blockVineWorldProcessor);

        this.handlers.put(BlockPrototype.DIRT, this.blockDirtPlaceProcessor);

        // 半磚
        this.handlers.put(BlockPrototype.STONE_SLAB, this.blockSlabsWorldProcessor);
        this.handlers.put(BlockPrototype.WOODEN_SLAB, this.blockSlabsWorldProcessor);
        this.handlers.put(BlockPrototype.STONE_SLAB2, this.blockSlabsWorldProcessor);
        this.handlers.put(BlockPrototype.DOUBLE_STONE_SLAB, this.blockSlabsWorldProcessor);
        this.handlers.put(BlockPrototype.DOUBLE_WOODEN_SLAB, this.blockSlabsWorldProcessor);
        this.handlers.put(BlockPrototype.DOUBLE_STONE_SLAB2, this.blockSlabsWorldProcessor);

        // 木頭
        this.handlers.put(BlockPrototype.LOG, this.blockLogWorldProcessor);
        this.handlers.put(BlockPrototype.LOG2, this.blockLogWorldProcessor);

        // 告示牌
        this.handlers.put(BlockPrototype.STANDING_SIGN, this.blockSignWorldProcessor);
        this.handlers.put(BlockPrototype.WALL_SIGN, this.blockSignWorldProcessor);
        this.handlers.put(BlockPrototype.SPRUCE_STANDING_SIGN, this.blockSignWorldProcessor);
        this.handlers.put(BlockPrototype.SPRUCE_WALL_SIGN, this.blockSignWorldProcessor);
        this.handlers.put(BlockPrototype.BIRCH_STANDING_SIGN, this.blockSignWorldProcessor);
        this.handlers.put(BlockPrototype.BIRCH_WALL_SIGN, this.blockSignWorldProcessor);
        this.handlers.put(BlockPrototype.JUNGLE_STANDING_SIGN, this.blockSignWorldProcessor);
        this.handlers.put(BlockPrototype.JUNGLE_WALL_SIGN, this.blockSignWorldProcessor);
        this.handlers.put(BlockPrototype.ACACIA_STANDING_SIGN, this.blockSignWorldProcessor);
        this.handlers.put(BlockPrototype.ACACIA_WALL_SIGN, this.blockSignWorldProcessor);
        this.handlers.put(BlockPrototype.DARKOAK_STANDING_SIGN, this.blockSignWorldProcessor);
        this.handlers.put(BlockPrototype.DARKOAK_WALL_SIGN, this.blockSignWorldProcessor);

        // 旗幟
        this.handlers.put(BlockPrototype.WALL_BANNER, this.blockBannerWorldProcessor);
        this.handlers.put(BlockPrototype.STANDING_BANNER, this.blockBannerWorldProcessor);

        // 織布機
        this.handlers.put(BlockPrototype.LOOM, this.blockLoomWorldProcessor);

        // 頭顱
        this.handlers.put(BlockPrototype.SKULL, this.blockSkullWorldProcessor);

        // 火
        this.handlers.put(BlockPrototype.FIRE, this.blockFireWorldProcessor);


        // 樓梯
        this.handlers.put(BlockPrototype.OAK_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.STONE_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.BRICK_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.STONE_BRICK_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.NETHER_BRICK_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.SANDSTONE_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.SPRUCE_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.BIRCH_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.JUNGLE_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.QUARTZ_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.ACACIA_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.DARK_OAK_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.PURPUR_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.PRISMARINE_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.DARK_PRISMARINE_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.PRISMARINE_BRICKS_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.RED_SANDSTONE_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.RED_NETHER_BRICKS_STAIRS, blockStairsWorldProcessor);
        this.handlers.put(BlockPrototype.SMOOTH_BRICKS_STAIRS, blockStairsWorldProcessor);

        // 門
        this.handlers.put(BlockPrototype.ACACIA_DOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.BIRCH_DOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.DARK_OAK_DOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.IRON_DOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.JUNGLE_DOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.SPRUCE_DOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.WOODEN_DOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.ACACIA_TRAPDOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.TRAPDOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.BIRCH_TRAPDOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.DARK_OAK_TRAPDOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.IRON_TRAPDOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.JUNGLE_TRAPDOOR, blockDoorWorldProcessor);
        this.handlers.put(BlockPrototype.SPRUCE_TRAPDOOR, blockDoorWorldProcessor);

        // 火把
        this.handlers.put(BlockPrototype.TORCH, blockTorchWorldProcessor);
        this.handlers.put(BlockPrototype.REDSTONE_TORCH, blockTorchWorldProcessor);
        this.handlers.put(BlockPrototype.UNDERWATER_TORCH, blockTorchWorldProcessor);
        this.handlers.put(BlockPrototype.UNLIT_REDSTONE_TORCH, blockTorchWorldProcessor);
        this.handlers.put(BlockPrototype.COLORED_TORCH_BP, blockTorchWorldProcessor);
        this.handlers.put(BlockPrototype.COLORED_TORCH_RG, blockTorchWorldProcessor);

        // 梯子
        this.handlers.put(BlockPrototype.LADDER, blockLadderProcessor);

        // 按鈕
        this.handlers.put(BlockPrototype.ACACIA_BUTTON, blockButtonWorldProcessor);
        this.handlers.put(BlockPrototype.BIRCH_BUTTON, blockButtonWorldProcessor);
        this.handlers.put(BlockPrototype.DARK_OAK_BUTTON, blockButtonWorldProcessor);
        this.handlers.put(BlockPrototype.JUNGLE_BUTTON, blockButtonWorldProcessor);
        this.handlers.put(BlockPrototype.STONE_BUTTON, blockButtonWorldProcessor);
        this.handlers.put(BlockPrototype.SPRUCE_BUTTON, blockButtonWorldProcessor);
        this.handlers.put(BlockPrototype.WOODEN_BUTTON, blockButtonWorldProcessor);

        // 拉桿
        this.handlers.put(BlockPrototype.LEVER, blockLeverWorldProcessor);


        //---------- 更新处理器 -----------
        this.addBlockUpdaterHandler(BlockPrototype.SNOW_LAYER, this.destoryOnHangingProcessor);
        this.addBlockUpdaterHandler(BlockPrototype.BROWN_MUSHROOM, this.blockMushroomWorldUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.RED_MUSHROOM, this.blockMushroomWorldUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.NETHER_WART, this.blockNetherWartUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.REEDS, this.blockReedsUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.SAND, this.fallingBlockUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.SAND, this.blockSandUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.CACTUS, this.blockCactusUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.CAKE, this.blockCakeUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.COCOA, this.blockCocoaUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.YELLOW_FLOWER, this.blockFlowerUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.RED_FLOWER, this.blockFlowerUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.WATER, this.blockWaterUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.FLOWING_WATER, this.blockWaterUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.DOUBLE_PLANT, this.destoryOnHangingProcessor);
        this.addBlockUpdaterHandler(BlockPrototype.MELON_STEM, this.blockStemUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.PUMPKIN_STEM, this.blockStemUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.TALLGRASS, this.blockDestroyUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.ICE, this.blockIceUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.FIRE, this.blockFireUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.DIRT, this.blockDirtUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.WHEAT, this.blockBreakOnHangUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.POTATOES, this.blockBreakOnHangUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.CARROTS, this.blockBreakOnHangUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.TORCH, this.blockTouchUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.REDSTONE_TORCH, this.blockTouchUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.UNDERWATER_TORCH, this.blockTouchUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.UNLIT_REDSTONE_TORCH, this.blockTouchUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.COLORED_TORCH_BP, this.blockTouchUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.COLORED_TORCH_RG, this.blockTouchUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.LEVER, this.blockLeverUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.ACACIA_BUTTON, this.blockButtonUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.BIRCH_BUTTON, this.blockButtonUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.DARK_OAK_BUTTON, this.blockButtonUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.JUNGLE_BUTTON, this.blockButtonUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.SPRUCE_BUTTON, this.blockButtonUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.STONE_BUTTON, this.blockButtonUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.WOODEN_BUTTON, this.blockButtonUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.WALL_SIGN, this.blockSignUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.SPRUCE_WALL_SIGN, this.blockSignUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.BIRCH_WALL_SIGN, this.blockSignUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.JUNGLE_WALL_SIGN, this.blockSignUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.ACACIA_WALL_SIGN, this.blockSignUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.DARKOAK_WALL_SIGN, this.blockSignUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.LADDER, this.blockLadderUpdater);
        // 門
        this.addBlockUpdaterHandler(BlockPrototype.ACACIA_DOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.BIRCH_DOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.DARK_OAK_DOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.IRON_DOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.JUNGLE_DOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.SPRUCE_DOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.WOODEN_DOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.ACACIA_TRAPDOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.TRAPDOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.BIRCH_TRAPDOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.DARK_OAK_TRAPDOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.IRON_TRAPDOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.JUNGLE_TRAPDOOR, blockDoorUpdater);
        this.addBlockUpdaterHandler(BlockPrototype.SPRUCE_TRAPDOOR, blockDoorUpdater);
    }

    /**
     * 添加方块更新器
     *
     * @param blockPrototype
     * @param updater
     */
    private void addBlockUpdaterHandler(BlockPrototype blockPrototype, IBlockWorldUpdater updater) {
        if (!this.blockUpdaterHandlers.containsKey(blockPrototype)) {
            this.blockUpdaterHandlers.put(blockPrototype, new ArrayList<>());
        }

        this.blockUpdaterHandlers.get(blockPrototype).add(updater);
    }

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        // 检查目标方块的合法性
        BlockPrototype block = this.levelService.getBlockTypeAt(level, targetPosition);
        if (!block.getBlockGeometry().canPassThrow()) {
            return false;
        }

        IBlockWorldProcessor processor = this.handlers.get(targetBlock.getType());
        if (processor != null) {
            return processor.onBlockPrePlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
        }

        return this.defaultBlockProcessor.onBlockPrePlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
    }

    @Override
    public boolean handleBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        IBlockWorldProcessor processor = this.handlers.get(targetBlock.getType());
        if (processor != null) {
            return processor.handleBlockPlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
        }

        return this.defaultBlockProcessor.handleBlockPlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
    }

    @Override
    public boolean onBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        IBlockWorldProcessor processor = this.handlers.get(targetBlock.getType());

        boolean state;
        if (processor != null) {
            state = processor.onBlockPlaced(level, player, targetBlock, targetPosition);
        } else {
            state = this.defaultBlockProcessor.onBlockPlaced(level, player, targetBlock, targetPosition);
        }

        this.updateAround(level, player, targetPosition, BlockOperationType.PLACE);

        return state;
    }

    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        IBlockWorldProcessor processor = this.handlers.get(targetBlock.getType());

        boolean state;
        if (processor != null) {
            state = processor.onBlockPreDestroy(level, player, targetBlock, targetPosition);
        } else {
            state = this.defaultBlockProcessor.onBlockPreDestroy(level, player, targetBlock, targetPosition);
        }

        return state;
    }

    @Override
    public boolean onBlockDestroyed(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        IBlockWorldProcessor processor = this.handlers.get(targetBlock.getType());

        boolean state;
        if (processor != null) {
            state = processor.onBlockDestroyed(level, player, targetBlock, targetPosition);
        } else {
            state = this.defaultBlockProcessor.onBlockDestroyed(level, player, targetBlock, targetPosition);
        }

        this.updateAround(level, player, targetPosition, BlockOperationType.DESTORY);

        return state;
    }

    private boolean updateAround(Level level, Player player, Vector3 targetPosition, BlockOperationType blockOperationType) {
        this.doUpdate(level, player, targetPosition.add(0, 1, 0), blockOperationType);
        this.doUpdate(level, player, targetPosition.add(0, -1, 0), blockOperationType);
        this.doUpdate(level, player, targetPosition.add(1, 0, 0), blockOperationType);
        this.doUpdate(level, player, targetPosition.add(-1, 0, 0), blockOperationType);
        this.doUpdate(level, player, targetPosition.add(0, 0, 1), blockOperationType);
        this.doUpdate(level, player, targetPosition.add(0, 0, -1), blockOperationType);

        return true;
    }

    public void doUpdate(Level level, Player player, Vector3 updatePosition, BlockOperationType blockOperationType) {
        Block block = this.levelService.getBlockAt(level, updatePosition);
        if (block.getType() != BlockPrototype.AIR) {
            List<IBlockWorldUpdater> blockWorldUpdaters = this.blockUpdaterHandlers.get(block.getType());
            if (blockWorldUpdaters != null) {
                for (IBlockWorldUpdater iBlockWorldUpdater : blockWorldUpdaters) {
                    iBlockWorldUpdater.onBlockUpdated(level, player, block, updatePosition, blockOperationType);
                }
            }
        }
    }
}
