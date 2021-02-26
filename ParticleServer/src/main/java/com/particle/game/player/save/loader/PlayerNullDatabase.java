package com.particle.game.player.save.loader;


import com.particle.api.player.PlayerDatabaseApi;

import java.util.Map;

public class PlayerNullDatabase implements PlayerDatabaseApi {

    @Override
    public void savePlayerData(String uuid, String name, Map<String, String> values, Map<String, String> ecsData, boolean release) {

    }

    @Override
    public Map<String, String> loadPlayerDataByUUID(String uuid) {
        return null;
    }

    @Override
    public Map<String, String> loadPlayerECSDataByUUID(String uuid) {
        return null;
    }

    @Override
    public void removePlayerData(String uuid, String key) {

    }

    @Override
    public void removePlayerDataByUUID(String uuid) {

    }
}
