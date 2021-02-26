package com.particle.model.inventory;

import com.particle.model.inventory.common.ContainerType;

public class PlayerCursorInventory extends Inventory {

    public PlayerCursorInventory() {
        this.setContainerType(ContainerType.CURSOR);
        this.setSize(ContainerType.CURSOR.getDefaultSize());
    }
}
