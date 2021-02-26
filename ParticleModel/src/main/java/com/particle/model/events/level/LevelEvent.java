package com.particle.model.events.level;

import com.particle.model.events.CancellableEvent;
import com.particle.model.level.Level;

public abstract class LevelEvent extends CancellableEvent {
    private Level level;

    public LevelEvent(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
