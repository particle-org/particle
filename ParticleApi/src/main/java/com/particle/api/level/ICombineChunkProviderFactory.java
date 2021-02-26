package com.particle.api.level;

import com.particle.model.level.LevelProviderMapper;
import com.particle.model.math.Rect4d;

public interface ICombineChunkProviderFactory {
    /**
     * @param levelProviderLow
     * @param levelProviderHeight
     * @param separation
     * @return
     */
    LevelProviderMapper getYProviderMapper(LevelProviderMapper levelProviderLow, LevelProviderMapper levelProviderHeight, int separation);


    /**
     * @param levelProviderInner
     * @param levelProviderOuter
     * @param rect4d             注意是闭区间
     * @return
     */
    LevelProviderMapper getXZProviderMapper(LevelProviderMapper levelProviderInner, LevelProviderMapper levelProviderOuter, Rect4d rect4d);
}
