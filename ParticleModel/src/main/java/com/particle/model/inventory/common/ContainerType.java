package com.particle.model.inventory.common;

import java.util.HashMap;
import java.util.Map;

public enum ContainerType {
    CHEST(27, "Chest", InventoryConstants.CONTAINER_TYPE_CONTAINER, 0, false, -1), //箱子
    ENDER_CHEST(27, "EnderChest", InventoryConstants.CONTAINER_TYPE_CONTAINER, 1, true, InventoryConstants.CONTAINER_ID_ENDER), // 末影箱
    DOUBLE_CHEST(54, "DoubleChest", InventoryConstants.CONTAINER_TYPE_CONTAINER, 2, false, -1), // 大箱子
    PLAYER(36, "Player", InventoryConstants.CONTAINER_TYPE_INVENTORY, 3, true, InventoryConstants.CONTAINER_ID_PLAYER), // 玩家背包
    FURNACE(3, "Furnace", InventoryConstants.CONTAINER_TYPE_FURNACE, 4, false, -1), // 熔炉
    WORKBENCH(5, "Crafting", InventoryConstants.CONTAINER_TYPE_WORKBENCH, 5, true, -1), // 小合成台 4 CRAFTING slots, 1 RESULT
    BIG_WORKBENCH(10, "Workbench", InventoryConstants.CONTAINER_TYPE_WORKBENCH, 6, true, InventoryConstants.CONTAINER_ID_WORKBENCH), // 大合成台 9 CRAFTING slots, 1 RESULT
    BREWING(5, "BrewingStand", InventoryConstants.CONTAINER_TYPE_BREWING_STAND, 7, false, -1), // 酿造台 1 INPUT, 3 POTION, 1 fuel
    ANVIL(3, "Anvil", InventoryConstants.CONTAINER_TYPE_ANVIL, 8, true, InventoryConstants.CONTAINER_ID_ANVIL), // 铁砧 2 INPUT, 1 OUTPUT
    ENCHANT_TABLE(2, "EnchantTable", InventoryConstants.CONTAINER_TYPE_ENCHANTMENT, 9, true, InventoryConstants.CONTAINER_ID_ENCHANT), // 附魔台 1 INPUT/OUTPUT, 1 LAPIS
    DISPENSER(0, "Dispenser", InventoryConstants.CONTAINER_TYPE_DISPENSER, 10, false, -1), // 未知
    DROPPER(9, "Dropper", InventoryConstants.CONTAINER_TYPE_DROPPER, 11, false, -1), //未知
    HOPPER(5, "Hopper", InventoryConstants.CONTAINER_TYPE_HOPPER, 12, false, -1), // 未知
    CURSOR(1, "Cursor", InventoryConstants.CONTAINER_TYPE_INVENTORY, 13, true, InventoryConstants.CONTAINER_ID_PLAYER_UI), // 单格背包
    ARMOR(4, "armor", InventoryConstants.CONTAINER_TYPE_INVENTORY, 14, true, InventoryConstants.CONTAINER_ID_ARMOR), // 装备
    DEPUTY(1, "Deputy", InventoryConstants.CONTAINER_TYPE_INVENTORY, 15, true, InventoryConstants.CONTAINER_ID_OFFHAND), // 副手
    LOOM(4, "Loom", InventoryConstants.CONTAINER_TYPE_LOOM, 16, true, -1); // 織布機


    /**
     * 最大排序数据
     */
    public static final int MAX_SORT_ORDER = 32;


    /**
     * 箱子大小
     */
    private final int size;
    /**
     * 箱子名称
     */
    private final String title;

    /**
     * 箱子类型
     */
    private final int typeId;

    /**
     * 管理顺序
     */
    private final int sortIndex;

    /**
     * 玩家占有的
     */
    private boolean playerOwned;

    /**
     * 是否是固定ID，如果默认是-1，表示随机
     */
    private int constantId;

    private static final Map<Integer, ContainerType> allContainerType = new HashMap<>();

    static {
        for (ContainerType containerType : ContainerType.values()) {
            allContainerType.put(containerType.getSortIndex(), containerType);
        }
    }

    public static ContainerType valueOfBySortIndex(int sortIndex) {
        return allContainerType.get(sortIndex);
    }

    ContainerType(int defaultSize, String title, int typeId, int sortIndex, boolean playerOwned, int constantId) {
        this.size = defaultSize;
        this.title = title;
        this.typeId = typeId;
        this.sortIndex = sortIndex;
        this.playerOwned = playerOwned;
        this.constantId = constantId;
    }

    public int getDefaultSize() {
        return size;
    }

    public String getDefaultTitle() {
        return title;
    }

    public int getType() {
        return typeId;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public boolean isPlayerOwned() {
        return playerOwned;
    }

    public int getConstantId() {
        return constantId;
    }
}
