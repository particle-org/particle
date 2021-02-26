package com.particle.api.level;

import com.particle.model.level.Level;
import com.particle.model.level.Weather;
import com.particle.model.math.Vector3f;

public interface WeatherServiceApi {
    void setWeather(Level level, Weather weather, int rainFall, Vector3f position, boolean notify);
}
