package com.particle.game.server.recharge;

import com.particle.api.netease.IPayServiceApi;
import com.particle.api.netease.OnPaySuccessCallback;
import com.particle.model.player.Player;

import javax.inject.Singleton;

@Singleton
public class PayServiceProxy implements IPayServiceApi {

    /**
     * 显示商店按钮
     *
     * @param player
     */
    @Override
    public void displayShopButton(Player player) {
        PayService.displayShopButton(player);
    }

    /**
     * 隐藏商店按钮
     *
     * @param player
     */
    @Override
    public void hideShopButton(Player player) {
        PayService.hideShopButton(player);
    }

    /**
     * 打开商店界面
     *
     * @param player
     */
    @Override
    public void openShopMenu(Player player) {
        PayService.openShopMenu(player);
    }

    /**
     * 打开商店界面
     *
     * @param player
     */
    @Override
    public void openShopMenu(Player player, String category) {
        PayService.openShopMenu(player, category);
    }

    @Override
    public void overrideOnPaySuccessCallback(OnPaySuccessCallback onPaySuccessCallback) {
        PayService.overrideOnPaySuccessCallback(onPaySuccessCallback);
    }
}
