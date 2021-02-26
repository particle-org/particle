package com.particle.game.player.save.loader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.particle.api.player.PlayerDatabaseApi;
import com.particle.api.player.PlayerDatabaseProviderApi;

@Singleton
public class PlayerDatabaseProvider implements PlayerDatabaseProviderApi {

    @Inject
    private PlayerFileDatabase playerFileDatabase;

    private PlayerDatabaseApi playerDatabase = null;

    @Override
    public PlayerDatabaseApi get() {
        return playerDatabase == null ? playerFileDatabase : playerDatabase;
    }

    @Override
    public void registerPlayerDataBase(PlayerDatabaseApi playerDatabase) {
        this.playerDatabase = playerDatabase;
    }
}
