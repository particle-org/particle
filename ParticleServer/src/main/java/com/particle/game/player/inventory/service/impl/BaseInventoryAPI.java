package com.particle.game.player.inventory.service.impl;

import com.particle.api.inventory.InventoryAPI;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.brewing.BrewingFuelModule;
import com.particle.game.block.common.components.CookComponent;
import com.particle.game.block.common.modules.CookModule;
import com.particle.game.block.furnace.FurnaceFuelModule;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.player.craft.RecipeManager;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.events.level.container.InventorySlotChangedLevelEvent;
import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.inventory.BrewingInventory;
import com.particle.model.inventory.FurnaceInventory;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.InventoryContentPacket;
import com.particle.model.network.packets.data.InventorySlotPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

/**
 * 基础类
 */
public abstract class BaseInventoryAPI implements InventoryAPI {

    private static final Logger logger = LoggerFactory.getLogger(BaseInventoryAPI.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<BrewingFuelModule> BREWING_FUEL_MODULE_HANDLER = ECSModuleHandler.buildHandler(BrewingFuelModule.class);
    private static final ECSModuleHandler<CookModule> COOK_MODULE_HANDLER = ECSModuleHandler.buildHandler(CookModule.class);
    private static final ECSModuleHandler<FurnaceFuelModule> FURNACE_FUEL_MODULE_HANDLER = ECSModuleHandler.buildHandler(FurnaceFuelModule.class);


    @Inject
    protected NetworkManager networkManager;

    protected EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    protected InventoryManager inventoryManager;

    @Inject
    private RecipeManager recipeManager;

    @Inject
    private LevelService levelService;

    @Inject
    private TileEntityService tileEntityService;

    @Override
    public ItemStack getItem(Inventory inventory, int index) {
        return inventory.getAllSlots().containsKey(index) ? inventory.getAllSlots().get(index).clone() :
                ItemStack.getItem(ItemPrototype.AIR, 0);
    }

    @Override
    public boolean setItem(Inventory inventory, int index, ItemStack itemStack) {
        return this.setItem(inventory, index, itemStack, true);
    }

    @Override
    public boolean setItem(Inventory inventory, int index, ItemStack itemStack, boolean notify) {
        if (index < 0 || index > inventory.getSize()) {
            return false;
        } else if (itemStack.getItemType().getId() == 0 || itemStack.getCount() <= 0) {
            return this.clear(inventory, index);
        }
        ItemStack old = this.getItem(inventory, index);
        inventory.putSlot(index, itemStack.clone());
        this.onSlotChange(inventory, index, old, itemStack, notify);
        return true;
    }

    @Override
    public List<ItemStack> addItem(Inventory inventory, List<ItemStack> slots) {
        // 清除无效物品&合并物品
        List<ItemStack> mergeSlots = new ArrayList<>();
        Iterator<ItemStack> itemStackIterator = slots.iterator();
        while (itemStackIterator.hasNext()) {
            ItemStack itemStack = itemStackIterator.next();
            if (itemStack == null || itemStack.getItemType().getId() == ItemPrototype.AIR.getId() || itemStack.getCount() <= 0) {
                logger.error("add item failed!, item[{}]", itemStack);
                itemStackIterator.remove();
                continue;
            }
            boolean isFind = false;
            for (ItemStack mergeItem : mergeSlots) {
                if (mergeItem.equalsAll(itemStack)) {
                    mergeItem.setCount(mergeItem.getCount() + itemStack.getCount());
                    isFind = true;
                    break;
                }
            }
            if (!isFind) {
                mergeSlots.add(itemStack);
            }
        }

        if (mergeSlots.isEmpty()) {
            return mergeSlots;
        }
        List<Integer> emptySlots = new ArrayList<>();

        // 寻找已有的物品，并填充其数量
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = this.getItem(inventory, i);
            if (itemStack.getItemType().getId() == ItemPrototype.AIR.getId() || itemStack.getCount() <= 0) {
                emptySlots.add(i);
                continue;
            }
            itemStackIterator = mergeSlots.iterator();
            while (itemStackIterator.hasNext()) {
                ItemStack itemIterator = itemStackIterator.next();
                if (itemIterator.equalsAll(itemStack) && itemStack.getCount() < itemStack.getMaxStackSize()) {
                    int amount = Math.min(itemStack.getMaxStackSize() - itemStack.getCount(), itemIterator.getCount());
                    amount = Math.min(amount, inventory.getMaxStackSize() - itemStack.getCount());
                    if (amount > 0) {
                        itemIterator.setCount(itemIterator.getCount() - amount);
                        itemStack.setCount(itemStack.getCount() + amount);
                        this.setItem(inventory, i, itemStack);
                        if (itemIterator.getCount() <= 0) {
                            itemStackIterator.remove();
                        }
                    }
                }
            }
            if (mergeSlots.isEmpty()) {
                break;
            }

        }
        // 将多余的物品填充到空槽中
        if (!mergeSlots.isEmpty() && !emptySlots.isEmpty()) {
            for (int slotIndex : emptySlots) {
                ItemStack itemStack = mergeSlots.get(0);
                int amount = Math.min(itemStack.getMaxStackSize(), itemStack.getCount());
                amount = Math.min(amount, inventory.getMaxStackSize());
                itemStack.setCount(itemStack.getCount() - amount);
                // 构造一个新对象
                ItemStack newItem = itemStack.clone();
                newItem.setCount(amount);
                this.setItem(inventory, slotIndex, newItem);
                if (itemStack.getCount() <= 0) {
                    mergeSlots.remove(0);
                }
                if (mergeSlots.isEmpty()) {
                    break;
                }
            }
        }
        // 返回未填充成功的物品
        return mergeSlots;
    }

    @Override
    public boolean isAllowAdd(Inventory inventory, ItemStack itemStack) {
        ItemStack newItemStack = itemStack.clone();
        Map<Integer, ItemStack> allSlots = inventory.getAllSlots();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack localItem = this.getItem(inventory, i);
            // 存在该物品
            if (localItem.equalsAll(newItemStack)) {
                int diff = localItem.getMaxStackSize() - localItem.getCount();
                if (diff > 0) {
                    newItemStack.setCount(newItemStack.getCount() - diff);
                }
            } else if (localItem.getItemType().getId() == ItemPrototype.AIR.getId()) {
                // 空槽
                newItemStack.setCount(newItemStack.getCount() - inventory.getMaxStackSize());
            }
            if (newItemStack.getCount() <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ItemStack> removeItem(Inventory inventory, List<ItemStack> slots) {
        // 清除无效物品
        Iterator<ItemStack> itemStackIterator = slots.iterator();
        while (itemStackIterator.hasNext()) {
            ItemStack itemStack = itemStackIterator.next();
            if (itemStack.getItemType().getId() == ItemPrototype.AIR.getId() || itemStack.getCount() <= 0) {
                itemStackIterator.remove();
            }
        }
        if (slots.isEmpty()) {
            return slots;
        }

        // 寻找已有的物品
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = this.getItem(inventory, i);
            if (itemStack.getItemType().getId() == ItemPrototype.AIR.getId() || itemStack.getCount() <= 0) {
                continue;
            }
            itemStackIterator = slots.iterator();
            while (itemStackIterator.hasNext()) {
                ItemStack itemIterator = itemStackIterator.next();
                if (itemIterator.equalsAll(itemStack)) {
                    int amount = Math.min(itemStack.getCount(), itemIterator.getCount());
                    itemIterator.setCount(itemIterator.getCount() - amount);
                    itemStack.setCount(itemStack.getCount() - amount);
                    this.setItem(inventory, i, itemStack);
                    if (itemIterator.getCount() <= 0) {
                        itemStackIterator.remove();
                    }
                }
            }
            if (slots.isEmpty()) {
                break;
            }
        }
        // 返回未被删除的item
        return slots;
    }

    @Override
    public void updateAllSlots(Inventory inventory, Map<Integer, ItemStack> items) {
        if (items.size() > inventory.getSize()) {
            // items变为有序的map
            TreeMap<Integer, ItemStack> newItems = new TreeMap<>();
            for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                newItems.put(entry.getKey(), entry.getValue());
            }
            // 截断， note: 跟先前设计不一样,有可能不会取满所有的物品
            items = newItems.subMap(0, true, inventory.getSize(), false);
        }
        for (int i = 0; i < inventory.getSize(); i++) {
            if (!items.containsKey(i)) {
                // 不包含，清空槽
                if (inventory.getAllSlots().containsKey(i)) {
                    this.clear(inventory, i);
                }
            } else {
                // 设置槽
                if (!this.setItem(inventory, i, items.get(i))) {
                    this.clear(inventory, i);
                }
            }
        }
    }

    @Override
    public void notifyPlayerContentChanged(Inventory inventory) {
        ItemStack[] itemStacks = new ItemStack[inventory.getSize()];
        for (int i = 0; i < inventory.getSize(); i++) {
            itemStacks[i] = this.getItem(inventory, i);
        }
        Set<Player> views = inventory.getViewers();
        for (Player player : views) {
            int containId = this.inventoryManager.getMapIdFromMultiOwned(player, inventory);
            if (containId == -1) {
                this.removeView(player, inventory, true);
            }
            InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
            inventoryContentPacket.setSlots(itemStacks);
            inventoryContentPacket.setContainerId(containId);
            networkManager.sendMessage(player.getClientAddress(), inventoryContentPacket);
        }
    }

    @Override
    public void notifyPlayerSlotChanged(Inventory inventory, int index) {
        Set<Player> views = inventory.getViewers();
        for (Player player : views) {
            int containId = this.inventoryManager.getMapIdFromMultiOwned(player, inventory);
            if (containId == -1) {
                this.removeView(player, inventory, true);
            }
            InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
            inventorySlotPacket.setItemStack(this.getItem(inventory, index));
            inventorySlotPacket.setSlot(index);
            inventorySlotPacket.setContainerId(containId);
            networkManager.sendMessage(player.getClientAddress(), inventorySlotPacket);
        }
    }

    @Override
    public boolean contain(Inventory inventory, ItemStack itemStack) {
        int count = Math.max(1, itemStack.getCount());
        for (ItemStack itemStack1 : inventory.getAllSlots().values()) {
            if (itemStack.equalsAll(itemStack1)) {
                count -= itemStack1.getCount();
                if (count <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Map<Integer, ItemStack> findAll(Inventory inventory, ItemStack itemStack) {
        Map<Integer, ItemStack> slots = new HashMap<>();
        for (Map.Entry<Integer, ItemStack> entry : inventory.getAllSlots().entrySet()) {
            if (itemStack.equalsAll(entry.getValue())) {
                slots.put(entry.getKey(), entry.getValue().clone());
            }
        }
        return slots;
    }

    @Override
    public int first(Inventory inventory, ItemStack itemStack) {
        return this.first(inventory, itemStack, true);
    }

    @Override
    public int first(Inventory inventory, ItemStack itemStack, boolean strictMode) {
        int count = Math.max(1, itemStack.getCount());
        for (Map.Entry<Integer, ItemStack> entry : inventory.getAllSlots().entrySet()) {
            if (strictMode) {
                if (itemStack.equalsAll(entry.getValue()) && entry.getValue().getCount() == count) {
                    return entry.getKey();
                }
            } else {
                if (itemStack.equalsAll(entry.getValue()) && entry.getValue().getCount() > count) {
                    return entry.getKey();
                }
            }
        }
        return -1;
    }

    @Override
    public int firstEmpty(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (this.getItem(inventory, i).getItemType() == ItemPrototype.AIR) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean clear(Inventory inventory, int index) {
        return this.clear(inventory, index, true);
    }

    @Override
    public boolean clear(Inventory inventory, int index, boolean notify) {
        if (inventory.getAllSlots().containsKey(index)) {
            ItemStack oldClone = this.getItem(inventory, index);
            inventory.getAllSlots().remove(index);
            this.onSlotChange(inventory, index, oldClone, null, notify);
        }
        return true;
    }

    @Override
    public boolean clearAll(Inventory inventory) {
        for (Integer index : inventory.getAllSlots().keySet()) {
            this.clear(inventory, index);
        }
        return true;
    }

    @Override
    public boolean clearAll(Inventory inventory, boolean notify) {
        for (Integer index : inventory.getAllSlots().keySet()) {
            this.clear(inventory, index, notify);
        }
        return true;
    }

    @Override
    public boolean isEmpty(Inventory inventory) {
        for (ItemStack itemStack : inventory.getAllSlots().values()) {
            if (itemStack != null && itemStack.getItemType().getId() > 0 && itemStack.getCount() > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isFull(Inventory inventory) {
        if (inventory.getAllSlots().size() < inventory.getSize()) {
            return false;
        }
        for (ItemStack itemStack : inventory.getAllSlots().values()) {
            if (itemStack == null || itemStack.getItemType().getId() == 0
                    || itemStack.getCount() < inventory.getMaxStackSize()
                    || itemStack.getCount() < itemStack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void addView(Player player, Inventory inventory) {
        inventory.addView(player);
    }

    protected void onClose(Player player, Inventory inventory) {
        inventory.removeView(player);
    }

    @Override
    public void removeView(Player player, Inventory inventory, boolean serverSide) {
        this.onClose(player, inventory);
    }

    @Override
    public DataPacket constructEquipmentPacket(Inventory inventory) {
        throw new ProphetException(ErrorCode.PARAM_ERROR, "未实现该方法调用");
    }

    @Override
    public boolean equipItem(Inventory inventory, int slot, ItemStack toItem, boolean notify) {
        throw new ProphetException(ErrorCode.PARAM_ERROR, "未实现该方法调用");
    }

    /**
     * 统一处理槽更改
     */
    protected void onSlotChange(Inventory inventory, int index, ItemStack old, ItemStack newItem, boolean notify) {
        if (notify) {
            this.notifyPlayerSlotChanged(inventory, index);
        }
        Set<Player> views = inventory.getViewers();
        for (Player player : views) {
            // 发送事件给level，通知变化
            InventorySlotChangedLevelEvent inventorySlotChangedLevelEvent = new InventorySlotChangedLevelEvent(player);
            inventorySlotChangedLevelEvent.setUpdateSlot(index);
            inventorySlotChangedLevelEvent.setInventory(inventory);
            eventDispatcher.dispatchEvent(inventorySlotChangedLevelEvent);

            if (!inventorySlotChangedLevelEvent.isCancelled()) {
                InventoryHolder inventoryHolder = inventory.getInventoryHolder();
                switch (inventory.getContainerType()) {
                    case BREWING:
                        this.handleBrewing(player.getLevel(), player, inventoryHolder, index);
                        break;
                    case FURNACE:
                        this.handleFurnace(player.getLevel(), player, inventoryHolder, index);
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 处理酿造台的槽变化
     *
     * @param player
     * @param inventoryHolder
     * @param updateSlot
     */
    private void handleBrewing(Level level, Player player, InventoryHolder inventoryHolder, int updateSlot) {
        Vector3f vector3f = inventoryHolder.getPosition();
        Vector3 position = new Vector3(vector3f.getFloorX(),
                vector3f.getFloorY(), vector3f.getFloorZ());


        Inventory inventory = inventoryHolder.getInventory();
        // 根据里面的药水数量，更改对应的属性
        if (updateSlot >= 1 && updateSlot <= 3) {
            Block block = this.levelService.getBlockAt(level, position);
            if (block == null) {
                return;
            }
            int meta = block.getMeta();
            ItemStack potion = this.getItem(inventoryHolder.getInventory(), updateSlot);
            if (potion.getItemType() == ItemPrototype.POTION && potion.getCount() > 0) {
                meta |= 1 << (updateSlot - 1);
            } else {
                meta &= ~(1 << (updateSlot - 1));
            }
            block.setMeta(meta);
            //更新方块
            this.levelService.setBlockAt(level, block, position);
        }
        TileEntity tileEntity = this.tileEntityService.getEntityAt(level, position);
        if (tileEntity == null) {
            logger.error("entity {} cant find tileEntity in position[{}]", player.getIdentifiedStr(), position);
            return;
        }

        // 填充燃料的时候，需要充能
        BrewingFuelModule brewingFuelModule = BREWING_FUEL_MODULE_HANDLER.getModule(tileEntity);
        if (updateSlot == BrewingInventory.FuelIndex) {
            ItemStack fuel = this.getItem(inventory, updateSlot);
            if (fuel.getItemType() == ItemPrototype.BLAZE_POWDER && fuel.getCount() > 0) {
                if (brewingFuelModule == null) {
                    brewingFuelModule = BREWING_FUEL_MODULE_HANDLER.bindModule(tileEntity);
                }
            }
        }

        ItemStack output = recipeManager.matchBrewingRecipe(inventory);
        CookModule cookModule = COOK_MODULE_HANDLER.getModule(tileEntity);
        if (output != null) {
            // 添加progressComponent
            if (cookModule == null) {
                cookModule = COOK_MODULE_HANDLER.bindModule(tileEntity);
            }

            cookModule.setStatus(CookComponent.STATUS_START);
        } else {
            // 去除progressComponent
            cookModule.setStatus(CookComponent.STATUS_INVALID);
        }
    }

    private void handleFurnace(Level level, Player player, InventoryHolder inventoryHolder, int updateSlot) {
        Vector3f vector3f = inventoryHolder.getPosition();
        Vector3 position = new Vector3(vector3f.getFloorX(),
                vector3f.getFloorY(), vector3f.getFloorZ());
        Inventory inventory = inventoryHolder.getInventory();

        TileEntity tileEntity = this.tileEntityService.getEntityAt(level, position);
        if (tileEntity == null) {
            logger.error("entity {} cant find tileEntity in position[{}]", player.getIdentifiedStr(), position);
            return;
        }
        // 燃料
        if (updateSlot == FurnaceInventory.FuelIndex && !FURNACE_FUEL_MODULE_HANDLER.hasModule(tileEntity)) {
            ItemStack fuel = this.getItem(inventory, updateSlot);
            if (fuel.getItemType() != ItemPrototype.AIR) {
                FURNACE_FUEL_MODULE_HANDLER.bindModule(tileEntity);
            }
        }
    }
}
