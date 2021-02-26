package com.particle.api.level;

import com.particle.model.level.LevelProviderMapper;

public interface IFileChunkProviderFactory {
    LevelProviderMapper getProvider(String levelName);
}
