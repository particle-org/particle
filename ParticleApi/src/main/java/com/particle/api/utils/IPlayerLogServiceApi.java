package com.particle.api.utils;

import com.particle.model.player.Player;

import java.util.Map;

public interface IPlayerLogServiceApi {
    void log(Player player, String tag, Map.Entry<String, Object>... logData);

    void log(Player player, String tag, Map<String, Object> logData);

    <T extends IPlayerLogData> void log(Player player, String tag, T objectBean);
}
