package com.particle.api.level;

import com.particle.model.level.Level;

public interface TimeServiceApi {
    void setTime(Level level, long time, boolean notify);
}
