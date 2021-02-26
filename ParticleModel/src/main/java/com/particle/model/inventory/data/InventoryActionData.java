package com.particle.model.inventory.data;

import com.particle.model.item.ItemStack;

public class InventoryActionData {

    private InventorySourceData source;

    private int slot;

    private ItemStack fromItem;

    private ItemStack toItem;

    // 1.16
    private int stackNetworkId;

    public InventorySourceData getSource() {
        return source;
    }

    public void setSource(InventorySourceData source) {
        this.source = source;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getFromItem() {
        return fromItem;
    }

    public void setFromItem(ItemStack fromItem) {
        this.fromItem = fromItem;
    }

    public ItemStack getToItem() {
        return toItem;
    }

    public void setToItem(ItemStack toItem) {
        this.toItem = toItem;
    }

    public int getStackNetworkId() {
        return stackNetworkId;
    }

    public void setStackNetworkId(int stackNetworkId) {
        this.stackNetworkId = stackNetworkId;
    }

    @Override
    public String toString() {
        return "InventoryActionData{" +
                "source=" + source +
                ", slot=" + slot +
                ", fromItem=" + fromItem +
                ", toItem=" + toItem +
                ", stackNetworkId=" + stackNetworkId +
                '}';
    }

    public InventoryActionData shallowCopy() {
        InventoryActionData inventoryActionData = new InventoryActionData();
        inventoryActionData.setSource(this.source);
        inventoryActionData.setSlot(this.slot);
        inventoryActionData.setFromItem(this.fromItem);
        inventoryActionData.setToItem(this.toItem);

        return inventoryActionData;
    }
}
