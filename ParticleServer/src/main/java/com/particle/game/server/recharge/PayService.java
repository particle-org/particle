package com.particle.game.server.recharge;

import com.alibaba.fastjson.JSONObject;
import com.particle.api.netease.OnPaySuccessCallback;
import com.particle.model.network.packets.data.NeteaseJsonPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;

public class PayService {

    @Inject
    private static NetworkManager networkManager;

    private static OnPaySuccessCallback onPaySuccessCallback;

    /**
     * 显示商店按钮
     *
     * @param player
     */
    public static void displayShopButton(Player player) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventName", NeteastJsonEvent.ON_CONTROL_STORE_ENTRANCE.getEvent());
        jsonObject.put("value", true);

        invokeOperation(player, jsonObject);
    }

    /**
     * 隐藏商店按钮
     *
     * @param player
     */
    public static void hideShopButton(Player player) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventName", NeteastJsonEvent.ON_CONTROL_STORE_ENTRANCE.getEvent());
        jsonObject.put("value", false);

        invokeOperation(player, jsonObject);
    }

    /**
     * 打开商店界面
     *
     * @param player
     */
    public static void openShopMenu(Player player) {
        openShopMenu(player, null);
    }

    /**
     * 打开商店界面
     *
     * @param player
     */
    public static void openShopMenu(Player player, String category) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventName", NeteastJsonEvent.ON_OPEN_STORE.getEvent());
        jsonObject.put("category", category);

        invokeOperation(player, jsonObject);
    }

    /**
     * 设置回调接口
     *
     * @param onPaySuccessCallback
     */
    public static void overrideOnPaySuccessCallback(OnPaySuccessCallback onPaySuccessCallback) {
        PayService.onPaySuccessCallback = onPaySuccessCallback;
    }

    /**
     * 执行回调
     *
     * @param player
     * @param content
     */
    public static void handleBuySuccessOperation(Player player, String content) {
        if (onPaySuccessCallback != null) {
            onPaySuccessCallback.handle(player);
        }
    }

    private static void invokeOperation(Player player, JSONObject operation) {
        NeteaseJsonPacket neteaseJsonPacket = new NeteaseJsonPacket();
        neteaseJsonPacket.setContent(operation.toJSONString());

        networkManager.sendMessage(player.getClientAddress(), neteaseJsonPacket);
    }

}
