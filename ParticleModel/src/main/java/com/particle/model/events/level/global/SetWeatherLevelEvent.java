package com.particle.model.events.level.global;

import com.particle.model.events.level.LevelEvent;
import com.particle.model.level.Level;
import com.particle.model.level.Weather;
import com.particle.model.math.Vector3f;

public class SetWeatherLevelEvent extends LevelEvent {

    private Weather weather;

    private int rainfall;

    private Vector3f position;

    private boolean notify;

    public SetWeatherLevelEvent(Level level) {
        super(level);
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public int getRainfall() {
        return rainfall;
    }

    public void setRainfall(int rainfall) {
        this.rainfall = rainfall;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
