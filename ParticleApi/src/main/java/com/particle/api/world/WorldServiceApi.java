package com.particle.api.world;

import com.particle.model.level.IChunkGenerate;
import com.particle.model.level.Level;
import com.particle.model.level.LevelProviderMapper;
import com.particle.model.level.settings.LevelSettings;

import java.util.Collection;

public interface WorldServiceApi {

    /**
     * 添加世界
     *
     * @param levelName     世界的名称
     * @param levelSettings 世界的设置
     * @param levelProvider
     * @param chunkGenerate
     * @return
     */
    Level registerLevel(String levelName, LevelSettings levelSettings, LevelProviderMapper levelProvider, IChunkGenerate chunkGenerate);

    /**
     * 删除世界
     * 会驱逐世界中的所有玩家，同时世界会停止运转
     *
     * @param levelName 世界的名称
     * @return
     */
    Level removeLevel(String levelName);

    /**
     * 根据名字获取世界
     *
     * @param levelName
     * @return
     */
    Level getLevel(String levelName);

    /**
     * 获取默认的世界
     *
     * @return
     */
    Level getDefaultLevel();

    /**
     * 获取改端的世界数量
     *
     * @return
     */
    public int getLevelCounts();

    /**
     * 获取所有的level
     *
     * @return
     */
    public Collection<Level> getAllLevels();

}

