package com.particle.game.server.recharge;

import com.particle.api.recharge.IRechargeCallback;
import com.particle.api.recharge.RechargeServiceApi;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RechargeService implements RechargeServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(RechargeService.class);

    private IRechargeCallback defaultRechargeCallback = new DefaultRechargeCallback();

    @Inject
    public void init() {
    }

    @Override
    public void handleRecharge(Player player, String message) {
        this.defaultRechargeCallback.handle(player, message);
    }

    @Override
    public void setCallback(IRechargeCallback callback) {
        this.defaultRechargeCallback = callback;
    }


    private class DefaultRechargeCallback implements IRechargeCallback {
        @Override
        public void handle(Player player, String message) {

        }
    }
}
