package com.particle.api.proxy.economy;


import java.util.UUID;

public interface AccountService {
    /**
     * 获取指定玩家的帐户
     *
     * @param playerUuid
     * @return
     */
    Account getAccount(UUID playerUuid);

    /**
     * 获取貨幣名
     *
     * @return
     */
    String getCurrencyName();
}
