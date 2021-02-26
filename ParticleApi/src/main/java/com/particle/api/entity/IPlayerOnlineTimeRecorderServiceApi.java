package com.particle.api.entity;

import com.particle.model.player.Player;

public interface IPlayerOnlineTimeRecorderServiceApi {
    long getPlayerOnlineTime(Player player);
}
