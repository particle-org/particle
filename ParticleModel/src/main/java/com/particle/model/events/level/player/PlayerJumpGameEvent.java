package com.particle.model.events.level.player;

import com.particle.model.player.Player;

public class PlayerJumpGameEvent extends LevelPlayerEvent {

    public PlayerJumpGameEvent(Player player) {
        super(player);
    }
}
