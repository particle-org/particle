package com.particle.api.level;

import com.google.inject.Provider;

public interface LevelProviderMapperFactoryProviderApi extends Provider<LevelProviderMapperFactory> {

    void registerLevelProviderMapperFactory(LevelProviderMapperFactory levelProviderMapperFactory);

}
