package com.particle.api.recharge;

import com.particle.model.player.Player;

public interface RechargeServiceApi {
    void setCallback(IRechargeCallback callback);

    void handleRecharge(Player player, String message);
}
