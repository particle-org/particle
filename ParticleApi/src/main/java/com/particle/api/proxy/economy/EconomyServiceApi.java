package com.particle.api.proxy.economy;

import com.particle.model.player.Player;

import java.math.BigDecimal;

public interface EconomyServiceApi {
    /**
     * @param player
     * @return 獲得對應貨幣
     */
    public boolean addCurrency(Player player, String currencyId, BigDecimal currencyAmount, String source, String reason);

    /**
     * @param player
     * @return 獲得對應貨幣
     */
    public boolean addCurrency(Player player, String currencyId, BigDecimal currencyAmount, String reason);

    /**
     * @param player
     * @return 扣除對應貨幣
     */
    public boolean subtractCurrency(Player player, String currencyId, BigDecimal currencyAmount, String reason);

    /**
     * @param player
     * @return 取得指定貨幣數量
     */
    public BigDecimal getCurrencyAmount(Player player, String currencyId);
}
