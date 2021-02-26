package com.particle.model.inventory;

import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.player.Player;

public class WorkBenchInventory extends Inventory {

    public WorkBenchInventory() {
        this.setContainerType(ContainerType.WORKBENCH);
        this.setSize(ContainerType.WORKBENCH.getDefaultSize());
    }

    @Override
    public void setInventoryHolder(InventoryHolder inventoryHolder) {
        super.setInventoryHolder(inventoryHolder);
        if (inventoryHolder instanceof Player) {
            this.addView((Player) inventoryHolder);
        }
    }
}
