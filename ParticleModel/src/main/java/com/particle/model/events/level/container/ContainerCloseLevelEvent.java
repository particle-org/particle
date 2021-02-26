package com.particle.model.events.level.container;

import com.particle.model.events.level.player.LevelPlayerEvent;
import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.player.Player;

public class ContainerCloseLevelEvent extends LevelPlayerEvent {

    private InventoryHolder inventoryHolder;

    public ContainerCloseLevelEvent(Player player) {
        super(player);
    }

    public InventoryHolder getInventoryHolder() {
        return inventoryHolder;
    }

    public void setInventoryHolder(InventoryHolder inventoryHolder) {
        this.inventoryHolder = inventoryHolder;
    }
}
