package com.particle.model.events.level.player;

import com.particle.model.player.Player;

public class PlayerRideEvent extends LevelPlayerEvent {
    public PlayerRideEvent(Player player) {
        super(player);
    }
}
