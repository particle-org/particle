package com.particle.game.player.inventory.modules;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.inventory.Inventory;

public class SingleContainerModule extends BehaviorModule {

    /**
     * 所属的inventory
     */
    protected Inventory inventory;

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
