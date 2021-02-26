package com.particle.api.proxy.goods;

import com.particle.model.player.Player;

@FunctionalInterface
public interface GoodsClickHandler {
    /**
     * 当玩家选择商品时触发
     *
     * @param player       玩家
     * @param providerName 商品标识,可能为null
     * @param goodsId      商品ID,可能为null
     */
    void onClick(Player player, String providerName, String goodsId);
}
