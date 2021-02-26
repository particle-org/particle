package com.particle.model.events.level.player;

import com.particle.model.player.Player;

public class PlayerStartSneakEvent extends LevelPlayerEvent {
    public PlayerStartSneakEvent(Player player) {
        super(player);
    }
}
