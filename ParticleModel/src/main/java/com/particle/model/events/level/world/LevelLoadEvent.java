package com.particle.model.events.level.world;

import com.particle.model.events.level.LevelEvent;
import com.particle.model.level.Level;

public class LevelLoadEvent extends LevelEvent {

    public LevelLoadEvent(Level level) {
        super(level);
    }

}
