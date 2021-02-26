package com.particle.model.events.level.container;

import com.particle.model.events.level.player.LevelPlayerEvent;
import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.player.Player;

public class ContainerOpenLevelEvent extends LevelPlayerEvent {

    private InventoryHolder inventoryHolder;

    public ContainerOpenLevelEvent(Player player) {
        super(player);
    }

    public InventoryHolder getInventoryHolder() {
        return inventoryHolder;
    }

    public void setInventoryHolder(InventoryHolder inventoryHolder) {
        this.inventoryHolder = inventoryHolder;
    }
}
