package com.particle.api.utils;

import com.particle.model.player.Player;
import com.particle.model.utils.DeviceModelLevel;

public interface DeviceModelServiceApi {

    /**
     * 获取设备类型
     *
     * @param player
     * @return
     */
    DeviceModelLevel getDeviceModelLevel(Player player);

    /**
     * 设置设备类型
     *
     * @param player
     * @param deviceModelLevel
     */
    void setDeviceModelLevel(Player player, DeviceModelLevel deviceModelLevel);
}
