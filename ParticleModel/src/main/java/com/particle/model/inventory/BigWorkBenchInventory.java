package com.particle.model.inventory;

import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.player.Player;

public class BigWorkBenchInventory extends Inventory {

    public BigWorkBenchInventory() {
        this.setContainerType(ContainerType.BIG_WORKBENCH);
        this.setSize(ContainerType.BIG_WORKBENCH.getDefaultSize());
    }

    @Override
    public void setInventoryHolder(InventoryHolder inventoryHolder) {
        super.setInventoryHolder(inventoryHolder);
        if (inventoryHolder instanceof Player) {
            this.addView((Player) inventoryHolder);
        }
    }

}
