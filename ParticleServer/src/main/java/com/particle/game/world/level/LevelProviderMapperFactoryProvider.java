package com.particle.game.world.level;

import com.particle.api.level.LevelProviderMapperFactory;
import com.particle.api.level.LevelProviderMapperFactoryProviderApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LevelProviderMapperFactoryProvider implements LevelProviderMapperFactoryProviderApi {
    @Inject
    private DefaultLevelProviderMapperFactory defaultLevelProviderMapperFactory;

    private LevelProviderMapperFactory levelProviderMapperFactory;

    @Override
    public void registerLevelProviderMapperFactory(LevelProviderMapperFactory levelProviderMapperFactory) {
        this.levelProviderMapperFactory = levelProviderMapperFactory;
    }

    @Override
    public LevelProviderMapperFactory get() {
        return levelProviderMapperFactory == null ? defaultLevelProviderMapperFactory : levelProviderMapperFactory;
    }
}
