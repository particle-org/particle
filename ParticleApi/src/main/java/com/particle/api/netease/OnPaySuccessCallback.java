package com.particle.api.netease;

import com.particle.model.player.Player;

@FunctionalInterface
public interface OnPaySuccessCallback {

    void handle(Player player);

}
