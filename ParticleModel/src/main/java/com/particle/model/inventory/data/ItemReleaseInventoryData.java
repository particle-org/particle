package com.particle.model.inventory.data;

import com.particle.model.inventory.common.ItemReleaseInventoryType;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;

public class ItemReleaseInventoryData extends ComplexInventoryTransaction {

    private ItemReleaseInventoryType actionType;

    private int slot;

    private ItemStack item;

    private Vector3f formPosition;

    public ItemReleaseInventoryType getActionType() {
        return actionType;
    }

    public void setActionType(ItemReleaseInventoryType actionType) {
        this.actionType = actionType;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Vector3f getFormPosition() {
        return formPosition;
    }

    public void setFormPosition(Vector3f formPosition) {
        this.formPosition = formPosition;
    }

    @Override
    public String toString() {
        return "ItemReleaseInventoryData{" +
                "actionType=" + actionType +
                ", slot=" + slot +
                ", item=" + item +
                ", formPosition=" + formPosition +
                '}';
    }
}
