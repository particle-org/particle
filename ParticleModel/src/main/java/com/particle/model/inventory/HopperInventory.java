package com.particle.model.inventory;

import com.particle.model.inventory.common.ContainerType;

public class HopperInventory extends Inventory {

    public HopperInventory() {
        this.setContainerType(ContainerType.HOPPER);
        this.setSize(ContainerType.HOPPER.getDefaultSize());
    }
}
