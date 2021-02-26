package com.particle.game.player.inventory.service.impl;

import com.particle.model.inventory.Inventory;
import com.particle.model.player.Player;

import javax.inject.Singleton;

@Singleton
public class AnvilInventoryAPI extends ContainerInventoryAPI {

    @Override
    public void addView(Player player, Inventory inventory) {
        super.addView(player, inventory);
    }

    @Override
    public void onClose(Player player, Inventory inventory) {
        this.closeAnvil(player, inventory);
        super.onClose(player, inventory);
    }

    /**
     * 关闭铁砧
     *
     * @param player
     * @param inventory
     */
    private void closeAnvil(Player player, Inventory inventory) {
        // 在close包时服务端回收物品
    }
}
