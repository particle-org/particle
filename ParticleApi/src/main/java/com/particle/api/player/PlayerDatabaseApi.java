package com.particle.api.player;

import java.util.Map;

public interface PlayerDatabaseApi {

    void savePlayerData(String uuid, String name, Map<String, String> values, Map<String, String> ecsData, boolean release);

    Map<String, String> loadPlayerDataByUUID(String uuid);

    Map<String, String> loadPlayerECSDataByUUID(String uuid);

    void removePlayerData(String uuid, String key);

    void removePlayerDataByUUID(String uuid);

    default void releaseLock(String uuid) {
    }
}
