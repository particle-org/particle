package com.particle.model.inventory;

import com.particle.model.inventory.common.ContainerType;

public class PlayerEnderChestInventory extends Inventory {

    public PlayerEnderChestInventory() {
        this.setContainerType(ContainerType.ENDER_CHEST);
        this.setSize(ContainerType.ENDER_CHEST.getDefaultSize());
    }
}
