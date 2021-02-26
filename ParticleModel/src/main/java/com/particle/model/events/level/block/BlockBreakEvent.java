package com.particle.model.events.level.block;

import com.particle.model.level.Level;
import com.particle.model.player.Player;

public class BlockBreakEvent extends BlockExpEvent {

    /**
     * 来源
     */
    private Caused caused;

    /**
     * 玩家
     */
    private Player player;

    public BlockBreakEvent(Level level) {
        super(level);
    }

    public Caused getCaused() {
        return caused;
    }

    public void setCaused(Caused caused) {
        this.caused = caused;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public enum Caused {
        UNSET,
        PLAYER,
        LEVEL
    }
}
