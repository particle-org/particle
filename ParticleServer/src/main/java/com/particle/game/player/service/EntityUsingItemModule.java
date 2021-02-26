package com.particle.game.player.service;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.inventory.Inventory;

public class EntityUsingItemModule extends BehaviorModule {

    /**
     * 注意！！！
     * <p>
     * 这个类暂时只用于食物操作，等产品1.14版本确定 CompletedUsingItem包 的用法再更新
     */


    private Inventory inventory;

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
