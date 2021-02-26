package com.particle.api.level;

import com.particle.model.level.LevelProviderMapper;

public interface ICustomFileChunkProviderFactory {
    LevelProviderMapper getProvider(String location, String levelName);
}
