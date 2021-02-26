package com.particle.game.world.level;

import com.particle.api.level.WeatherServiceApi;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.events.level.global.SetWeatherLevelEvent;
import com.particle.model.level.Level;
import com.particle.model.level.LevelEventType;
import com.particle.model.level.Weather;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.LevelEventPacket;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WeatherService implements WeatherServiceApi {

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Override
    public void setWeather(Level level, Weather weather, int rainFall, Vector3f position, boolean notify) {
        SetWeatherLevelEvent setWeatherLevelEvent = new SetWeatherLevelEvent(level);
        setWeatherLevelEvent.setWeather(weather);
        setWeatherLevelEvent.setRainfall(rainFall);
        setWeatherLevelEvent.setPosition(position);
        setWeatherLevelEvent.setNotify(notify);

        this.eventDispatcher.dispatchEvent(setWeatherLevelEvent);

        if (setWeatherLevelEvent.isCancelled()) {
            return;
        }

        level.setWeather(weather);
        if (notify) {
            LevelEventPacket levelEventPacket = new LevelEventPacket();

            LevelEventType levelEventType = LevelEventType.StopRaining;
            if (level.getWeather() == Weather.RAIN) {
                levelEventType = LevelEventType.StartRaining;
            }
            levelEventPacket.setEventType(levelEventType.getType());
            levelEventPacket.setData(rainFall);
            levelEventPacket.setPosition(position);
            this.broadcastServiceProxy.broadcast(level, levelEventPacket);
        }
    }
}
