package com.particle.api.utils;

public interface UuidHelperApi {
    String getPlayerName(String playerUuid);

    String getPlayerUuid(String playerName);

    long getPlayerRoleId(String playerUuid);

    String getPlayerUuid(long roleId);
}
