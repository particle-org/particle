package com.particle.api.netease;

import com.particle.model.player.Player;

public interface IPayServiceApi {
    void displayShopButton(Player player);

    void hideShopButton(Player player);

    void openShopMenu(Player player);

    void openShopMenu(Player player, String category);

    void overrideOnPaySuccessCallback(OnPaySuccessCallback onPaySuccessCallback);
}
