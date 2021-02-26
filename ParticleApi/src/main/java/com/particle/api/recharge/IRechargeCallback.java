package com.particle.api.recharge;

import com.particle.model.player.Player;

@FunctionalInterface
public interface IRechargeCallback {
    void handle(Player player, String message);
}
