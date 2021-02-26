package com.particle.model.events.level.player;

import com.particle.model.level.Level;
import com.particle.model.player.Player;

public class PlayerLeaveLevelEvent extends LevelPlayerEvent {

    private LeaveCause leaveCause;

    public PlayerLeaveLevelEvent(Player player, Level level, LeaveCause leaveCause) {
        super(player, level);
        this.leaveCause = leaveCause;
    }

    public enum LeaveCause {
        QUIT, DEATH, SWITCH_LEVEL, LEVEL_DESTROY
    }

    public LeaveCause getLeaveCause() {
        return leaveCause;
    }
}
