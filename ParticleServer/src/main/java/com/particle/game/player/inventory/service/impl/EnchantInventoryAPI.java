package com.particle.game.player.inventory.service.impl;

import com.particle.model.inventory.Inventory;
import com.particle.model.player.Player;

import javax.inject.Singleton;

@Singleton
public class EnchantInventoryAPI extends ContainerInventoryAPI {

    @Override
    public void onClose(Player player, Inventory inventory) {
        this.closeEnchant(player, inventory);
        super.onClose(player, inventory);
    }

    /**
     * 关闭附魔台
     *
     * @param player
     * @param inventory
     */
    private void closeEnchant(Player player, Inventory inventory) {
        // 不用做任何事，客户端会发包通知收回物品
    }
}
