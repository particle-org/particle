package com.particle.model.inventory.data;

import com.particle.model.inventory.common.ItemUseInventoryActionType;
import com.particle.model.item.ItemStack;
import com.particle.model.math.BlockFace;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

public class ItemUseInventoryData extends ComplexInventoryTransaction {

    private ItemUseInventoryActionType actionType;

    private Vector3 position;

    private BlockFace face;

    private int slot;

    private ItemStack item;

    private Vector3f fromPosition;

    private Vector3f clickPosition;

    public ItemUseInventoryActionType getActionType() {
        return actionType;
    }

    public void setActionType(ItemUseInventoryActionType actionType) {
        this.actionType = actionType;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public BlockFace getFace() {
        return face;
    }

    public void setFace(BlockFace face) {
        this.face = face;
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

    public Vector3f getClickPosition() {
        return clickPosition;
    }

    public void setClickPosition(Vector3f clickPosition) {
        this.clickPosition = clickPosition;
    }

    @Override
    public String toString() {
        return "ItemUseInventoryData{" +
                "actionType=" + actionType.toString() +
                ", position=" + position +
                ", face=" + face +
                ", slot=" + slot +
                ", item=" + item.toString() +
                ", fromPosition=" + fromPosition +
                ", clickPosition=" + clickPosition +
                '}';
    }
}
