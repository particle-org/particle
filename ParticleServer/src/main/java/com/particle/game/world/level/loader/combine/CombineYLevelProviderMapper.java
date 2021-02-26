package com.particle.game.world.level.loader.combine;

import com.particle.model.level.LevelProvider;
import com.particle.model.level.LevelProviderMapper;

/**
 * 生成按照Y轴高度分别存储的LevelProvider
 */
public class CombineYLevelProviderMapper implements LevelProviderMapper {


    private LevelProviderMapper levelProviderHeight;
    private LevelProviderMapper levelProviderLow;

    /**
     * 分界线，分界线处属于Height部分
     */
    private int separation;


    public CombineYLevelProviderMapper(LevelProviderMapper levelProviderLow, LevelProviderMapper levelProviderHeight, int separation) {
        this.levelProviderHeight = levelProviderHeight;
        this.levelProviderLow = levelProviderLow;
        this.separation = separation;
    }

    @Override
    public LevelProvider getLevelProvider(int x, int z) {
        return new CombineLevelProvider(this.levelProviderLow.getLevelProvider(x, z), this.levelProviderHeight.getLevelProvider(x, z), this.separation);
    }
}
