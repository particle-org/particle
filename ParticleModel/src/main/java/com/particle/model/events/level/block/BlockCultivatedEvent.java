package com.particle.model.events.level.block;

import com.particle.model.level.Level;
import com.particle.model.player.Player;

public class BlockCultivatedEvent extends LevelBlockEvent {

    private Player player;

    public BlockCultivatedEvent(Level level) {
        super(level);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
