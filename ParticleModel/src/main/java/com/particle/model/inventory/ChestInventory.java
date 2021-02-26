package com.particle.model.inventory;

import com.particle.model.inventory.common.ContainerType;

public class ChestInventory extends Inventory {

    public ChestInventory() {
        this.setContainerType(ContainerType.CHEST);
        this.setSize(ContainerType.CHEST.getDefaultSize());
    }
}
