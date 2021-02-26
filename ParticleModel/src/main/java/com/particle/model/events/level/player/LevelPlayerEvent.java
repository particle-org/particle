package com.particle.model.events.level.player;

import com.particle.model.events.level.LevelEvent;
import com.particle.model.level.Level;
import com.particle.model.player.Player;

public abstract class LevelPlayerEvent extends LevelEvent {
    private Player player;

    public LevelPlayerEvent(Player player) {
        super(player.getLevel());

        this.player = player;
    }

    public LevelPlayerEvent(Player player, Level level) {
        super(level);

        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
