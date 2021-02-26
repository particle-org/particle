package com.particle.api.server;

import com.particle.model.player.Player;

public interface PrisonServiceApi {

    void imprison(Player player);

    boolean isInJail(Player player);
}
