package com.particle.game.player.inventory.modules;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerUIInventoryModule extends BehaviorModule {

    private List<ItemStack> craftItems = new ArrayList<>();

    public void addItem(ItemStack item) {
        this.craftItems.add(item);
        // 避免玩家故意刷爆内存
        if (this.craftItems.size() > 20) {
            this.craftItems.clear();
        }
    }

    public boolean removeItem(ItemStack item) {
        Iterator<ItemStack> iterator = this.craftItems.iterator();
        while (iterator.hasNext()) {
            ItemStack next = iterator.next();

            if (item.equalsAll(next)) {
                if (next.getCount() > item.getCount()) {
                    next.setCount(next.getCount() - item.getCount());
                } else {
                    iterator.remove();
                }
                return true;
            }
        }

        return false;
    }

    public boolean hasItem(ItemStack item) {
        Iterator<ItemStack> iterator = this.craftItems.iterator();
        while (iterator.hasNext()) {
            ItemStack next = iterator.next();

            if (item.equalsAll(next)) {
                return true;
            }
        }

        return false;
    }

}
