package com.particle.game.player.inventory.service;

import com.particle.api.inventory.InventoryAPI;
import com.particle.game.player.inventory.service.impl.*;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

/**
 * 用于代理各个inventoryService
 */
@Singleton
public class InventoryAPIProxy implements InventoryAPI {

    // ---------------------------- service集合 -----------------------------------------------------
    // 缓存处理的service
    private InventoryAPI[] inventoryApis = new InventoryAPI[ContainerType.MAX_SORT_ORDER];

    // 处理 ender(末影箱背包)
    @Inject
    private EnderChestInventoryAPI enderChestInventoryService;

    // 处理 玩家cursor（单格背包）-- win10版本
    @Inject
    private PlayerCursorInventoryAPI playerCursorInventoryService;


    // 处理 玩家背包
    @Inject
    private PlayerInventoryAPI playerInventoryService;

    // 处理盔甲
    @Inject
    private ArmorInventoryAPI armorInventoryService;

    // 处理副手
    @Inject
    private DeputyInventoryAPI deputyInventoryService;

    // 铁砧
    @Inject
    private AnvilInventoryAPI anvilInventoryService;

    // 酿造台
    @Inject
    private BrewingInventoryAPI brewingInventoryService;

    // 箱子
    @Inject
    private ChestInventoryAPI chestInventoryService;

    // 附魔台
    @Inject
    private EnchantInventoryAPI enchantInventoryService;

    // 熔炉
    @Inject
    private FurnaceInventoryAPI furnaceInventoryService;

    // 基础背包service
    @Inject
    private ContainerInventoryAPI containerInventoryService;

    // 是否初始化service
    private boolean isInitService = false;

    /**
     * 获取inventory service
     * <p>
     * 对于一些非common接口，需要转换成对应的service
     *
     * @param inventory
     * @return
     */
    private InventoryAPI getInventoryService(Inventory inventory) {
        if (!isInitService) {
            this.initService();
        }
        ContainerType type = inventory.getContainerType();
        if (type != null) {
            return this.inventoryApis[type.getSortIndex()];
        }
        return containerInventoryService;
    }

    /**
     * 初始化service
     */
    private void initService() {
        for (ContainerType containerType : ContainerType.values()) {
            switch (containerType) {
                case PLAYER:
                    this.inventoryApis[containerType.getSortIndex()] = playerInventoryService;
                    break;
                case DEPUTY:
                    this.inventoryApis[containerType.getSortIndex()] = deputyInventoryService;
                    break;
                case ENCHANT_TABLE:
                    this.inventoryApis[containerType.getSortIndex()] = enchantInventoryService;
                    break;
                case ARMOR:
                    this.inventoryApis[containerType.getSortIndex()] = armorInventoryService;
                    break;
                case BREWING:
                    this.inventoryApis[containerType.getSortIndex()] = brewingInventoryService;
                    break;
                case CHEST:
                    this.inventoryApis[containerType.getSortIndex()] = chestInventoryService;
                    break;
                case ANVIL:
                    this.inventoryApis[containerType.getSortIndex()] = anvilInventoryService;
                    break;
                case FURNACE:
                    this.inventoryApis[containerType.getSortIndex()] = furnaceInventoryService;
                    break;
                case ENDER_CHEST:
                    this.inventoryApis[containerType.getSortIndex()] = enderChestInventoryService;
                    break;
                case CURSOR:
                    this.inventoryApis[containerType.getSortIndex()] = playerCursorInventoryService;
                    break;
                default:
                    this.inventoryApis[containerType.getSortIndex()] = containerInventoryService;
                    break;
            }
        }
        isInitService = true;
    }

    @Override
    public ItemStack getItem(Inventory inventory, int index) {
        return this.getInventoryService(inventory).getItem(inventory, index);
    }

    @Override
    public boolean setItem(Inventory inventory, int index, ItemStack itemStack) {
        return this.getInventoryService(inventory).setItem(inventory, index, itemStack);
    }

    @Override
    public boolean setItem(Inventory inventory, int index, ItemStack itemStack, boolean notify) {
        return this.getInventoryService(inventory).setItem(inventory, index, itemStack, notify);
    }

    @Override
    public List<ItemStack> addItem(Inventory inventory, List<ItemStack> slots) {
        return this.getInventoryService(inventory).addItem(inventory, slots);
    }

    @Override
    public boolean isAllowAdd(Inventory inventory, ItemStack itemStack) {
        return this.getInventoryService(inventory).isAllowAdd(inventory, itemStack);
    }

    @Override
    public List<ItemStack> removeItem(Inventory inventory, List<ItemStack> slots) {
        return this.getInventoryService(inventory).removeItem(inventory, slots);
    }

    @Override
    public void updateAllSlots(Inventory inventory, Map<Integer, ItemStack> items) {
        this.getInventoryService(inventory).updateAllSlots(inventory, items);
    }

    @Override
    public void notifyPlayerContentChanged(Inventory inventory) {
        this.getInventoryService(inventory).notifyPlayerContentChanged(inventory);
    }

    @Override
    public void notifyPlayerSlotChanged(Inventory inventory, int index) {
        this.getInventoryService(inventory).notifyPlayerSlotChanged(inventory, index);
    }

    @Override
    public boolean contain(Inventory inventory, ItemStack itemStack) {
        return this.getInventoryService(inventory).contain(inventory, itemStack);
    }

    @Override
    public Map<Integer, ItemStack> findAll(Inventory inventory, ItemStack itemStack) {
        return this.getInventoryService(inventory).findAll(inventory, itemStack);
    }

    @Override
    public int first(Inventory inventory, ItemStack itemStack, boolean strictMode) {
        return this.getInventoryService(inventory).first(inventory, itemStack, strictMode);
    }

    @Override
    public int firstEmpty(Inventory inventory) {
        return this.getInventoryService(inventory).firstEmpty(inventory);
    }

    @Override
    public boolean clear(Inventory inventory, int index, boolean notify) {
        return this.getInventoryService(inventory).clear(inventory, index, notify);
    }

    @Override
    public boolean clearAll(Inventory inventory) {
        return this.getInventoryService(inventory).clearAll(inventory);
    }

    @Override
    public boolean clearAll(Inventory inventory, boolean notify) {
        return this.getInventoryService(inventory).clearAll(inventory, notify);
    }

    @Override
    public boolean isEmpty(Inventory inventory) {
        return this.getInventoryService(inventory).isEmpty(inventory);
    }

    @Override
    public boolean isFull(Inventory inventory) {
        return this.getInventoryService(inventory).isFull(inventory);
    }

    @Override
    public void addView(Player player, Inventory inventory) {
        this.getInventoryService(inventory).addView(player, inventory);
    }

    @Override
    public void removeView(Player player, Inventory inventory, boolean serverSide) {
        this.getInventoryService(inventory).removeView(player, inventory, serverSide);
    }

    @Override
    public DataPacket constructEquipmentPacket(Inventory inventory) {
        return this.getInventoryService(inventory).constructEquipmentPacket(inventory);
    }

    @Override
    public boolean equipItem(Inventory inventory, int slot, ItemStack toItem, boolean notify) {
        return this.getInventoryService(inventory).equipItem(inventory, slot, toItem, notify);
    }
}
