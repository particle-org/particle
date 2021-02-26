package com.particle.model.events.level.player;

import com.particle.model.level.Level;
import com.particle.model.player.Player;

public class PlayerQuitMiniGameEvent extends LevelPlayerEvent {

    public PlayerQuitMiniGameEvent(Player player, Level level) {
        super(player, level);
    }
}
