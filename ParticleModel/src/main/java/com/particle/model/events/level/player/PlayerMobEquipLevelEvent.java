package com.particle.model.events.level.player;

import com.particle.model.inventory.Inventory;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

public class PlayerMobEquipLevelEvent extends LevelPlayerEvent {

    private long targetRuntimeId;

    private ItemStack itemStack;

    private byte slot;

    private byte selectedSlot;

    private Inventory inventory;


    public PlayerMobEquipLevelEvent(Player player, long targetRuntimeId, ItemStack itemStack,
                                    byte slot, byte selectedSlot, Inventory inventory) {
        super(player);
        this.targetRuntimeId = targetRuntimeId;
        this.itemStack = itemStack;
        this.slot = slot;
        this.selectedSlot = selectedSlot;
        this.inventory = inventory;
    }


    public PlayerMobEquipLevelEvent(Player player) {
        super(player);
    }


    public long getTargetRuntimeId() {
        return targetRuntimeId;
    }

    public void setTargetRuntimeId(long targetRuntimeId) {
        this.targetRuntimeId = targetRuntimeId;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public byte getSlot() {
        return slot;
    }

    public void setSlot(byte slot) {
        this.slot = slot;
    }

    public byte getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(byte selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
