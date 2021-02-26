package com.particle.model.events.level.container;

import com.particle.model.events.level.player.LevelPlayerEvent;
import com.particle.model.inventory.Inventory;
import com.particle.model.player.Player;

public class InventorySlotChangedLevelEvent extends LevelPlayerEvent {

    private Inventory inventory;

    private int updateSlot;

    public InventorySlotChangedLevelEvent(Player player) {
        super(player);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getUpdateSlot() {
        return updateSlot;
    }

    public void setUpdateSlot(int updateSlot) {
        this.updateSlot = updateSlot;
    }
}
