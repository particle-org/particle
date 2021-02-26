package com.particle.model.inventory;

import com.particle.model.inventory.common.ContainerType;

public class DeputyInventory extends Inventory {

    /**
     * 副手
     */
    public static final int DEPUTY = 0;

    public DeputyInventory() {
        this.setContainerType(ContainerType.DEPUTY);
        this.setSize(ContainerType.DEPUTY.getDefaultSize());
    }
}
