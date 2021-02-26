package com.particle.game.world.level;

import com.particle.api.level.LevelProviderMapperFactory;
import com.particle.model.level.LevelProviderMapper;

import javax.inject.Singleton;

@Singleton
public class DefaultLevelProviderMapperFactory implements LevelProviderMapperFactory {

    @Override
    public LevelProviderMapper getProvider(String levelName) {
        return null;
    }
}
