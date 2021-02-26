package com.particle.model.events.level.container;

import com.particle.model.events.level.player.LevelPlayerEvent;
import com.particle.model.inventory.Inventory;
import com.particle.model.level.Level;
import com.particle.model.player.Player;

public class InventoryLoadLevelEvent extends LevelPlayerEvent {

    private Inventory inventory;

    public InventoryLoadLevelEvent(Player player, Level level) {
        super(player, level);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
