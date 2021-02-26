package com.particle.model.events.level.player;

import com.particle.model.player.Player;

public class PlayerQuitGameEvent extends LevelPlayerEvent {
    public PlayerQuitGameEvent(Player player) {
        super(player);
    }
}
