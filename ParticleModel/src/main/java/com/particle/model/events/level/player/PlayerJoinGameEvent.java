package com.particle.model.events.level.player;

import com.particle.model.level.Level;
import com.particle.model.player.Player;

public class PlayerJoinGameEvent extends LevelPlayerEvent {
    private Level changedLevel;

    private String cancelReason;

    public PlayerJoinGameEvent(Player player, Level level) {
        super(player, level);
    }

    public Level getChangedLevel() {
        return changedLevel;
    }

    public void setChangedLevel(Level changedLevel) {
        this.changedLevel = changedLevel;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
