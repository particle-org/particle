package com.particle.model.events.level.player;

import com.particle.model.player.Player;

public class PlayerStopSwimmingEvent extends LevelPlayerEvent {
    public PlayerStopSwimmingEvent(Player player) {
        super(player);
    }
}
