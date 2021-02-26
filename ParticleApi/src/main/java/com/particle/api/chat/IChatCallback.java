package com.particle.api.chat;

import com.particle.model.player.Player;

@FunctionalInterface
public interface IChatCallback {
    void handle(Player player, String message);
}
