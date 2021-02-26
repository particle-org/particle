package com.particle.model.level;

import com.particle.core.aoi.container.SceneDataProvider;
import com.particle.core.aoi.model.Scene;
import com.particle.core.ecs.GameObject;
import com.particle.executor.thread.IScheduleThread;
import com.particle.model.level.settings.Capacity;
import com.particle.model.level.settings.Dimension;
import com.particle.model.level.settings.LevelSettings;
import com.particle.model.player.Player;
import com.particle.model.ui.score.ScoreObjective;
import com.particle.model.ui.score.ScorePacketInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class Level extends GameObject {
    public abstract Scene getScene();

    public abstract String getLevelName();

    public abstract Chunk getChunk(long index);

    public abstract Chunk removeChunk(long index);

    public abstract Collection<Chunk> getChunks();

    public abstract void putChunk(long index, Chunk chunk);

    public abstract Weather getWeather();

    public abstract void setWeather(Weather weather);

    public abstract long getTime();

    public abstract void setTime(long time);

    public abstract void addTime(int add);

    public abstract long getTickCount();

    public abstract void increaseTickCount();

    public abstract long getLastTickTime();

    public abstract void setLastTickTime(long lastTickTime);

    public abstract long getLastSyncTime();

    public abstract void setLastSyncTime(long lastSyncTime);

    public abstract LevelProviderMapper getLevelProviderMapper();

    public abstract void setLevelProviderMapper(LevelProviderMapper levelProviderMapper);

    public abstract SceneDataProvider<GameObject> getSceneDataProvider();

    public abstract void setSceneDataProvider(SceneDataProvider<GameObject> sceneDataProvider);

    public abstract IChunkGenerate getChunkGenerate();

    public abstract void setChunkGenerate(IChunkGenerate chunkGenerate);

    public abstract LevelSettings getLevelSettings();

    public abstract void setLevelSettings(LevelSettings levelSettings);

    public abstract LevelStatus getLevelStatus();

    public abstract void setLevelStatus(LevelStatus levelStatus);

    public abstract Capacity getCapacity();

    public abstract void setCapacity(Capacity capacity);

    public abstract IScheduleThread getLevelSchedule();

    public abstract ScoreObjective getScoreObjective();

    public abstract void setScoreObjective(ScoreObjective scoreObjective);

    public abstract List<ScorePacketInfo> getAllScorePacketInfos();

    public abstract void setAllScorePacketInfos(List<ScorePacketInfo> allScorePacketInfos);

    public abstract boolean isScoreboardDisplay();

    public abstract void setScoreboardDisplay(boolean scoreboardDisplay);

    public abstract Dimension getDimension();

    public abstract void setDimension(Dimension dimension);

    public abstract Player addPlayer(Player player);

    public abstract Player removePlayer(Player player);

    public abstract Map<String, Player> getPlayers();

    public abstract int getEntityCount();

    public abstract void setEntityCount(int entityCount);
}
