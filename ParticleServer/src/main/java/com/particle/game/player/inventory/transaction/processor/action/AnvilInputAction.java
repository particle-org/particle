package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.data.InventoryActionData;

public class AnvilInputAction extends ContainerChangeAction {

    public AnvilInputAction(Inventory inventory, InventoryActionData actionData) {
        super(inventory, actionData);
    }
}
