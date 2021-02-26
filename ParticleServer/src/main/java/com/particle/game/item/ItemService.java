package com.particle.game.item;

import com.particle.api.item.IItemPlaceProcessor;
import com.particle.api.item.IItemProcessor;
import com.particle.api.item.IItemReleaseProcessor;
import com.particle.api.item.IItemUseProcessor;
import com.particle.game.item.release.release.ItemBowReleaseProcessor;
import com.particle.game.item.release.release.ItemCrossbowReleaseProcessor;
import com.particle.game.item.release.release.ItemTridentReleaseProcessor;
import com.particle.game.item.release.use.ItemCrossbowPostUseProcessor;
import com.particle.game.item.release.use.ItemCrossbowUseProcessor;
import com.particle.game.item.release.use.ItemFoodPostUsed;
import com.particle.game.item.release.use.ItemPotionPostUseProcessor;
import com.particle.game.item.use.place.*;
import com.particle.game.item.use.place.planting.ItemCocoaPlaced;
import com.particle.game.item.use.place.planting.ItemDoubleFlowerPlaced;
import com.particle.game.item.use.place.planting.ItemFlowerPlaced;
import com.particle.game.item.use.place.planting.ItemSeedPlaced;
import com.particle.game.item.use.use.*;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemReleaseInventoryData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.item.types.ItemPrototypeDictionary;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 物品放置操作处理业务
 */
@Singleton
public class ItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);

    private Map<ItemPrototype, IItemProcessor> itemPlaceHandlerMap = new ConcurrentHashMap<>();
    /**
     * 在{@code InventoryTransactionType.USE}下的use调用
     */
    private Map<ItemPrototype, IItemProcessor> itemUseHandlerMap = new ConcurrentHashMap<>();

    private Map<ItemPrototype, IItemReleaseProcessor> itemReleaseHandlerMap = new ConcurrentHashMap<>();

    /**
     * 在{@code InventoryTransactionType.ITEM_RELEASE}下的use调用
     */
    private Map<ItemPrototype, IItemReleaseProcessor> itemReleaseUseHandlerMap = new ConcurrentHashMap<>();


    //放置方块的处理器
    @Inject
    private ItemPlaceBlockProcessor itemPlaceBlockProcessor;

    // 骨粉
    @Inject
    private ItemDyeProcessor itemDyeProcessor;

    // 刷怪物
    @Inject
    private ItemSpawnEggProcessor itemSpawnEggProcessor;

    //使用物品的处理器
    @Inject
    private ItemEnderPearlPreUseProcessor itemEnderPearlPreUseProcessor;
    @Inject
    private ItemSplashPotionPreUseProcessor itemSplashPotionPreUseProcessor;
    // 锄头
    @Inject
    private ItemHoeProcessor itemHoeProcessor;

    @Inject
    private ItemEggPreUseProcessor itemEggPreUseProcessor;

    // 告示牌
    @Inject
    private ItemSignProcessor itemSignProcessor;

    @Inject
    private ItemBucketProcessor itemBucketProcessor;

    @Inject
    private GlassBottleProcessor glassBottleProcessor;

    // 小麦种子、马铃薯种子、胡萝卜种子、甜菜根种子、西瓜种子
    @Inject
    private ItemSeedPlaced itemSeedPlaced;
    // 可口豆
    @Inject
    private ItemCocoaPlaced itemCocoaPlaced;
    // 花朵
    @Inject
    private ItemFlowerPlaced itemFlowerPlaced;
    @Inject
    private ItemDoubleFlowerPlaced itemDoubleFlowerPlaced;

    //药水相关
    @Inject
    private ItemPotionPostUseProcessor itemPotionPostUseProcessor;

    // 弓
    @Inject
    private ItemBowPreUseProcessor itemBowPreUseProcessor;
    @Inject
    private ItemBowReleaseProcessor itemBowReleaseProcessor;

    // 弩
    @Inject
    private ItemCrossbowPreUseProcessor itemCrossbowPreUseProcessor;
    @Inject
    private ItemCrossbowPostUseProcessor itemCrossbowPostUseProcessor;
    @Inject
    private ItemCrossbowUseProcessor itemCrossbowUseProcessor;
    @Inject
    private ItemCrossbowReleaseProcessor itemCrossbowReleaseProcessor;

    // 三叉戟
    @Inject
    private ItemTridentPreUseProcessor itemTridentPreUseProcessor;
    @Inject
    private ItemTridentReleaseProcessor itemTridentReleaseProcessor;

    // 食物
    @Inject
    private ItemFoodPostUsed itemFoodPostUsed;

    // 地图
    @Inject
    private ItemMapPreUseProcessor itemMapPreUseProcessor;

    // 旗幟
    @Inject
    private ItemBannerProcessor itemBannerProcessor;

    @Inject
    public void init() {
        //初始化所有可放置的物品
        ItemPrototypeDictionary itemPrototypeDictionary = ItemPrototypeDictionary.getDictionary();

        for (BlockPrototype blockPrototype : BlockPrototype.values()) {
            // 空氣 或 小麥 或 甜根菜 跳過
            if (blockPrototype == BlockPrototype.AIR
                    || blockPrototype == BlockPrototype.WHEAT
                    || blockPrototype == BlockPrototype.BEETROOT) {
                continue;
            }

            ItemPrototype itemPrototype = itemPrototypeDictionary.map(blockPrototype.getName());

            if (itemPrototype != null) {
                this.itemPlaceHandlerMap.put(itemPrototype, itemPlaceBlockProcessor);
            }
        }

        // 告示牌
        this.itemPlaceHandlerMap.put(ItemPrototype.SIGN, itemSignProcessor);
        this.itemPlaceHandlerMap.put(ItemPrototype.SPRUCE_SIGN, itemSignProcessor);
        this.itemPlaceHandlerMap.put(ItemPrototype.BIRCH_SIGN, itemSignProcessor);
        this.itemPlaceHandlerMap.put(ItemPrototype.JUNGLE_SIGN, itemSignProcessor);
        this.itemPlaceHandlerMap.put(ItemPrototype.ACACIA_SIGN, itemSignProcessor);
        this.itemPlaceHandlerMap.put(ItemPrototype.DARKOAK_SIGN, itemSignProcessor);

        // 旗幟
        this.itemPlaceHandlerMap.put(ItemPrototype.BANNER, itemBannerProcessor);
        this.itemPlaceHandlerMap.put(ItemPrototype.STANDING_BANNER, itemBannerProcessor);
        this.itemPlaceHandlerMap.put(ItemPrototype.WALL_BANNER, itemBannerProcessor);

        //骨粉
        this.itemPlaceHandlerMap.put(ItemPrototype.DYE, itemDyeProcessor);

        // 刷蛋
        this.itemPlaceHandlerMap.put(ItemPrototype.SPAWN_EGG, itemSpawnEggProcessor);

        //末影珍珠
        this.itemPlaceHandlerMap.put(ItemPrototype.ENDER_PEARL, itemEnderPearlPreUseProcessor);
        this.itemUseHandlerMap.put(ItemPrototype.ENDER_PEARL, itemEnderPearlPreUseProcessor);
        this.itemPlaceHandlerMap.put(ItemPrototype.SPLASH_POTION, itemSplashPotionPreUseProcessor);
        this.itemUseHandlerMap.put(ItemPrototype.SPLASH_POTION, itemSplashPotionPreUseProcessor);

        //雞蛋
        this.itemPlaceHandlerMap.put(ItemPrototype.EGG, itemEggPreUseProcessor);
        this.itemUseHandlerMap.put(ItemPrototype.EGG, itemEggPreUseProcessor);

        // 水桶
        this.itemPlaceHandlerMap.put(ItemPrototype.BUCKET, itemBucketProcessor);

        // 药水瓶
        this.itemPlaceHandlerMap.put(ItemPrototype.GLASS_BOTTLE, glassBottleProcessor);

        // 红石
        this.itemPlaceHandlerMap.put(ItemPrototype.REDSTONE, itemPlaceBlockProcessor);

        //---------------------------------种植相关----------------------------------------------------------------------
        //锄头
        this.itemPlaceHandlerMap.put(ItemPrototype.WOODEN_HOE, itemHoeProcessor);
        this.itemPlaceHandlerMap.put(ItemPrototype.DIAMOND_HOE, itemHoeProcessor);
        this.itemPlaceHandlerMap.put(ItemPrototype.GOLDEN_HOE, itemHoeProcessor);
        this.itemPlaceHandlerMap.put(ItemPrototype.IRON_HOE, itemHoeProcessor);
        this.itemPlaceHandlerMap.put(ItemPrototype.STONE_HOE, itemHoeProcessor);
        // 小麦种子
        this.itemPlaceHandlerMap.put(ItemPrototype.WHEAT_SEEDS, itemSeedPlaced);
        // 胡萝卜种子
        this.itemPlaceHandlerMap.put(ItemPrototype.CARROT, itemSeedPlaced);
        // 马铃薯种子
        this.itemPlaceHandlerMap.put(ItemPrototype.POTATO, itemSeedPlaced);
        // 甜菜根种子
        this.itemPlaceHandlerMap.put(ItemPrototype.BEETROOT_SEEDS, itemSeedPlaced);
        // 西瓜种子
        this.itemPlaceHandlerMap.put(ItemPrototype.MELON_SEEDS, itemSeedPlaced);
        // 南瓜种子
        this.itemPlaceHandlerMap.put(ItemPrototype.PUMPKIN_SEEDS, itemSeedPlaced);

        // 花朵
        this.itemPlaceHandlerMap.put(ItemPrototype.YELLOW_FLOWER, itemFlowerPlaced);
        this.itemPlaceHandlerMap.put(ItemPrototype.RED_FLOWER, itemFlowerPlaced);
        this.itemPlaceHandlerMap.put(ItemPrototype.DOUBLE_PLANT, itemDoubleFlowerPlaced);


        //药水相关
        this.itemUseHandlerMap.put(ItemPrototype.POTION, itemPotionPostUseProcessor);
        this.itemReleaseHandlerMap.put(ItemPrototype.POTION, itemPotionPostUseProcessor);

        // 弓
        this.itemUseHandlerMap.put(ItemPrototype.BOW, itemBowPreUseProcessor);
        this.itemReleaseHandlerMap.put(ItemPrototype.BOW, itemBowReleaseProcessor);

        // 弩
        this.itemUseHandlerMap.put(ItemPrototype.CROSSBOW, itemCrossbowUseProcessor);
        this.itemReleaseHandlerMap.put(ItemPrototype.CROSSBOW, itemCrossbowReleaseProcessor);

        // 三叉戟
        this.itemUseHandlerMap.put(ItemPrototype.TRIDENT, itemTridentPreUseProcessor);
        this.itemReleaseHandlerMap.put(ItemPrototype.TRIDENT, itemTridentReleaseProcessor);

        // --------------------------------- 食物 ----------------------------------------------------------------------
        this.itemUseHandlerMap.put(ItemPrototype.APPLE, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.BAKED_POTATO, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.BEETROOT, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.BEETROOT_SOUP, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.BREAD, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.CARROT, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.CHORUS_FRUIT, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.COOKED_CHICKEN, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.COOKED_FISH, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.MUTTONCOOKED, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.COOKED_PORKCHOP, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.COOKED_RABBIT, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.COOKED_SALMON, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.COOKIE, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.DRIED_KELP, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.GOLDEN_APPLE, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.APPLEENCHANTED, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.GOLDEN_CARROT, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.MELON, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.MUSHROOM_STEW, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.POISONOUS_POTATO, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.POTATO, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.PUFFERFISH, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.PUMPKIN_PIE, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.RABBIT_STEW, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.BEEF, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.PORKCHOP, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.CHICKEN, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.MUTTONRAW, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.ROTTEN_FLESH, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.SPIDER_EYE, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.COOKED_BEEF, this.itemFoodPostUsed);
        this.itemUseHandlerMap.put(ItemPrototype.CLOWNFISH, this.itemFoodPostUsed);

        this.itemReleaseHandlerMap.put(ItemPrototype.APPLE, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.BAKED_POTATO, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.BEETROOT, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.BEETROOT_SOUP, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.BREAD, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.CARROT, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.CHORUS_FRUIT, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.COOKED_CHICKEN, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.COOKED_FISH, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.MUTTONCOOKED, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.COOKED_PORKCHOP, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.COOKED_RABBIT, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.COOKED_SALMON, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.COOKIE, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.DRIED_KELP, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.GOLDEN_APPLE, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.APPLEENCHANTED, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.GOLDEN_CARROT, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.MELON, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.MUSHROOM_STEW, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.POISONOUS_POTATO, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.POTATO, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.PUFFERFISH, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.PUMPKIN_PIE, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.RABBIT_STEW, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.BEEF, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.PORKCHOP, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.CHICKEN, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.MUTTONRAW, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.ROTTEN_FLESH, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.SPIDER_EYE, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.COOKED_BEEF, this.itemFoodPostUsed);
        this.itemReleaseHandlerMap.put(ItemPrototype.CLOWNFISH, this.itemFoodPostUsed);

        // 地图
        this.itemUseHandlerMap.put(ItemPrototype.EMPTYMAP, this.itemMapPreUseProcessor);
    }

    /**
     * 注册物品放置处理器
     *
     * @param itemPrototype
     * @param itemPlaceProcessor
     */
    public void registerItemPlacedHandler(ItemPrototype itemPrototype, IItemPlaceProcessor itemPlaceProcessor) {
        this.itemPlaceHandlerMap.put(itemPrototype, itemPlaceProcessor);
    }

    /**
     * 注册物品使用处理器
     *
     * @param itemPrototype
     * @param itemUseProcessor
     */
    public void registerItemUseHandler(ItemPrototype itemPrototype, IItemUseProcessor itemUseProcessor) {
        this.itemUseHandlerMap.put(itemPrototype, itemUseProcessor);
    }

    /**
     * 注册物品释放处理器
     *
     * @param itemPrototype
     * @param itemUseProcessor
     */
    public void registerItemReleaseHandler(ItemPrototype itemPrototype, IItemReleaseProcessor itemUseProcessor) {
        this.itemReleaseHandlerMap.put(itemPrototype, itemUseProcessor);
    }

    /**
     * 注册物品释放使用处理器（客户端定的，某些按住不动释放时触发）
     *
     * @param itemPrototype
     * @param itemUseProcessor
     */
    public void registerItemReleaseUseHandler(ItemPrototype itemPrototype, IItemReleaseProcessor itemUseProcessor) {
        this.itemReleaseUseHandlerMap.put(itemPrototype, itemUseProcessor);
    }

    public void onItemPlaced(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        IItemProcessor itemPlaceHandler = this.itemPlaceHandlerMap.get(itemUseInventoryData.getItem().getItemType());

        if (itemPlaceHandler != null) {
            itemPlaceHandler.process(player, itemUseInventoryData, inventoryActionData);
        }
    }

    /**
     * 在{@code InventoryTransactionType.USE}下的use调用
     *
     * @param player
     * @param itemUseInventoryData
     * @param inventoryActionData
     */
    public void onPreItemUsed(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        IItemProcessor itemPlaceHandler = this.itemUseHandlerMap.get(itemUseInventoryData.getItem().getItemType());

        if (itemPlaceHandler != null) {
            itemPlaceHandler.process(player, itemUseInventoryData, inventoryActionData);
        }
    }

    /**
     * 在{@code InventoryTransactionType.ITEM_RELEASE}下的use调用
     *
     * @param player
     * @param itemReleaseInventoryData
     * @param inventoryActionData
     */
    public void onPostItemUsed(Player player, ItemReleaseInventoryData itemReleaseInventoryData, InventoryActionData[] inventoryActionData) {
        IItemReleaseProcessor itemProcessor = this.itemReleaseUseHandlerMap.get(itemReleaseInventoryData.getItem().getItemType());

        if (itemProcessor != null) {
            itemProcessor.process(player, itemReleaseInventoryData, inventoryActionData);
        }
    }

    public void onItemRelease(Player player, ItemReleaseInventoryData itemReleaseInventoryData, InventoryActionData[] inventoryActionData) {
        IItemReleaseProcessor itemProcessor = this.itemReleaseHandlerMap.get(itemReleaseInventoryData.getItem().getItemType());

        if (itemProcessor != null) {
            itemProcessor.process(player, itemReleaseInventoryData, inventoryActionData);
        }
    }
}
