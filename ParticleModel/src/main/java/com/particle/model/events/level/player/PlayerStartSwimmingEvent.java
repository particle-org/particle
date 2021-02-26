package com.particle.model.events.level.player;

import com.particle.model.player.Player;

public class PlayerStartSwimmingEvent extends LevelPlayerEvent {
    public PlayerStartSwimmingEvent(Player player) {
        super(player);
    }
}
