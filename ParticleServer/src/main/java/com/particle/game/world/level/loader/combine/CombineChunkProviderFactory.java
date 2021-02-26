package com.particle.game.world.level.loader.combine;

import com.particle.api.level.ICombineChunkProviderFactory;
import com.particle.model.level.LevelProviderMapper;
import com.particle.model.math.Rect4d;

import javax.inject.Singleton;

@Singleton
public class CombineChunkProviderFactory implements ICombineChunkProviderFactory {

    @Override
    public LevelProviderMapper getYProviderMapper(LevelProviderMapper levelProviderLow, LevelProviderMapper levelProviderHeight, int separation) {
        return new CombineYLevelProviderMapper(levelProviderLow, levelProviderHeight, separation);
    }

    @Override
    public LevelProviderMapper getXZProviderMapper(LevelProviderMapper levelProviderInner, LevelProviderMapper levelProviderOuter, Rect4d rect4d) {
        return new CombinedXZLevelProviderMapper(levelProviderInner, levelProviderOuter, rect4d);
    }

}
