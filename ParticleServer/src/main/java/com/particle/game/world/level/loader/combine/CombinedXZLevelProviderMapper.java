package com.particle.game.world.level.loader.combine;

import com.particle.model.level.LevelProvider;
import com.particle.model.level.LevelProviderMapper;
import com.particle.model.math.Rect4d;

public class CombinedXZLevelProviderMapper implements LevelProviderMapper {

    private LevelProviderMapper inArea;
    private LevelProviderMapper outAres;

    /**
     * 使用拼接方案的区域
     */
    private Rect4d area = null;

    public CombinedXZLevelProviderMapper(LevelProviderMapper inArea, LevelProviderMapper outAres, Rect4d area) {
        this.inArea = inArea;
        this.outAres = outAres;
        this.area = area;
    }

    @Override
    public LevelProvider getLevelProvider(int x, int z) {
        if (area != null && this.area.isInner(x, z)) {
            return inArea.getLevelProvider(x, z);
        } else {
            return outAres.getLevelProvider(x, z);
        }
    }
}
