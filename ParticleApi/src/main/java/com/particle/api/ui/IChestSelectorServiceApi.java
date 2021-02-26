package com.particle.api.ui;

import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

public interface IChestSelectorServiceApi {
    void open(Player player, IChestSelectorCallback callback);

    interface IChestSelectorCallback {
        void handle(Player player, ItemStack itemStack);
    }
}
