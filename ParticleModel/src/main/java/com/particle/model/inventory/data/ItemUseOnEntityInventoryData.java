package com.particle.model.inventory.data;

import com.particle.model.inventory.common.ItemUseOnEntityInventoryType;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Vector3f;

public class ItemUseOnEntityInventoryData extends ComplexInventoryTransaction {

    private long entityRuntimeId;

    private ItemUseOnEntityInventoryType actionType;

    private int slot;

    private ItemStack item;

    private Vector3f fromPosition;

    private Vector3f hitPosition;

    public long getEntityRuntimeId() {
        return entityRuntimeId;
    }

    public void setEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

    public ItemUseOnEntityInventoryType getActionType() {
        return actionType;
    }

    public void setActionType(ItemUseOnEntityInventoryType actionType) {
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

    public Vector3f getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(Vector3f fromPosition) {
        this.fromPosition = fromPosition;
    }

    public Vector3f getHitPosition() {
        return hitPosition;
    }

    public void setHitPosition(Vector3f hitPosition) {
        this.hitPosition = hitPosition;
    }

    @Override
    public String toString() {
        return "ItemUseOnEntityInventoryData{" +
                "entityRuntimeId=" + entityRuntimeId +
                ", actionType=" + actionType +
                ", slot=" + slot +
                ", item=" + item +
                ", fromPosition=" + fromPosition +
                ", hitPosition=" + hitPosition +
                '}';
    }
}
