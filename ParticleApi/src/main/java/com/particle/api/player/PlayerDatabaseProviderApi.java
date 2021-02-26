package com.particle.api.player;

import com.google.inject.Provider;

public interface PlayerDatabaseProviderApi extends Provider<PlayerDatabaseApi> {

    void registerPlayerDataBase(PlayerDatabaseApi playerDatabase);
}
