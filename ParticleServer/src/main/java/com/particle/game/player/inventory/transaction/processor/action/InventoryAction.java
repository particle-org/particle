package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.InventorySourceType;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

public abstract class InventoryAction {

    /**
     * action data的数据
     */
    private InventoryActionData inventoryActionData;

    /**
     * 所属的背包
     */
    private Inventory inventory;

    /**
     * 所更新的槽
     */
    private int slot;

    public InventoryAction(Inventory inventory, InventoryActionData inventoryActionData) {
        this(inventoryActionData);
        this.inventory = inventory;
        this.slot = inventoryActionData.getSlot();
    }

    public InventoryAction(InventoryActionData inventoryActionData) {
        this.inventoryActionData = inventoryActionData;
        this.slot = inventoryActionData.getSlot();
    }

    /**
     * @return
     */
    public ItemStack getFromItem() {
        return inventoryActionData.getFromItem().clone();
    }

    /**
     * 匹配源物品
     *
     * @param item
     * @return
     */
    public boolean matchFromItem(ItemStack item) {
        return this.inventoryActionData.getFromItem().equalsAll(item);
    }

    /**
     * @return
     */
    public ItemStack getToItem() {
        return inventoryActionData.getToItem().clone();
    }

    /**
     * 更新
     *
     * @param fromItem
     */
    public void setFromItem(ItemStack fromItem) {
        inventoryActionData.setFromItem(fromItem);
    }

    /**
     * 更新
     *
     * @param toItem
     */
    public void setToItem(ItemStack toItem) {
        inventoryActionData.setToItem(toItem);
    }

    protected InventoryActionData getInventoryActionData() {
        return inventoryActionData;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    /**
     * 返回类型
     *
     * @return
     */
    public InventorySourceType getSourceType() {
        return inventoryActionData.getSource().getSourceType();
    }

    abstract public boolean isValid(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player);

    abstract public boolean execute(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player);

    abstract public void onSuccess(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player);

    abstract public void onFailed(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player);
}
