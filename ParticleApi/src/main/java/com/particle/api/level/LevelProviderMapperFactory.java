package com.particle.api.level;

import com.particle.model.level.LevelProviderMapper;

public interface LevelProviderMapperFactory {
    LevelProviderMapper getProvider(String levelName);
}
