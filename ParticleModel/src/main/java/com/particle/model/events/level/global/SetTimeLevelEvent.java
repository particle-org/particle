package com.particle.model.events.level.global;

import com.particle.model.events.level.LevelEvent;
import com.particle.model.level.Level;

public class SetTimeLevelEvent extends LevelEvent {

    private long time;

    private boolean notify;

    public SetTimeLevelEvent(Level level) {
        super(level);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
