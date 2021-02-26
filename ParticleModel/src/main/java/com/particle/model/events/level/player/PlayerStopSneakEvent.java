package com.particle.model.events.level.player;

import com.particle.model.player.Player;

public class PlayerStopSneakEvent extends LevelPlayerEvent {
    public PlayerStopSneakEvent(Player player) {
        super(player);
    }
}
