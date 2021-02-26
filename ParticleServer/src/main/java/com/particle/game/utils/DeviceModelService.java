package com.particle.game.utils;

import com.particle.api.utils.DeviceModelServiceApi;
import com.particle.model.player.Player;
import com.particle.model.utils.DeviceModelLevel;

import javax.inject.Singleton;

@Singleton
public class DeviceModelService implements DeviceModelServiceApi {

    @Override
    public DeviceModelLevel getDeviceModelLevel(Player player) {
        return DeviceModelLevel.LOW;
    }

    @Override
    public void setDeviceModelLevel(Player player, DeviceModelLevel deviceModelLevel) {

    }
}
