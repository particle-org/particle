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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleLevel extends Level {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLevel.class);

    /**
     * chunk名称
     */
    private String levelName;

    /**
     * 世界中的区块
     */
    private Map<Long, Chunk> chunks = new ConcurrentHashMap<>();

    /**
     * 天气情况
     */
    private Weather weather;

    private Dimension dimension;

    /**
     * 上次Tick时间
     */
    private long lastTickTime = -1;

    /**
     * 上次Level同步时间
     */
    private long lastSyncTime = 0;

    /**
     * level的数据源
     */
    private LevelProviderMapper levelProviderMapper;

    /**
     * AOI触发接口
     */
    private SceneDataProvider<GameObject> sceneDataProvider;

    /**
     * 当前时间
     */
    private long time = 1;

    /**
     * Tick次数
     */
    private long tickCount = 0;

    /**
     * 世界参数
     */
    private LevelSettings levelSettings;

    /**
     * 世界生成器
     */
    private IChunkGenerate chunkGenerate;


    /**
     * 世界的状态，如果世界处于销毁状态，无法被交互
     */
    private LevelStatus levelStatus;

    /**
     * 世界的人数容量
     */
    private Capacity capacity = new Capacity();

    /**
     * 世界中的玩家缓存
     */
    private Map<String, Player> players = new ConcurrentHashMap<>();

    /**
     * Level的主运行线程
     */
    private IScheduleThread levelThread;

    /**
     * 生物数量统计
     */
    private int entityCount;


    /**
     * 计分板的整体信息
     */
    private ScoreObjective scoreObjective = new ScoreObjective();


    /**
     * 计分板详情
     */
    private List<ScorePacketInfo> allScorePacketInfos = new ArrayList<>();

    /**
     * 是否计分板
     */
    private boolean isScoreboardDisplay = false;

    /**
     * Level所属的scene
     */
    private Scene scene;

    public SimpleLevel(Scene scene, String levelName, IScheduleThread levelThread, LevelSettings levelSettings, LevelProviderMapper levelProviderMapper, IChunkGenerate chunkGenerate) {
        this.scene = scene;
        this.levelName = levelName;
        this.levelThread = levelThread;
        this.levelSettings = levelSettings;
        this.levelProviderMapper = levelProviderMapper;
        this.chunkGenerate = chunkGenerate;
        this.levelStatus = LevelStatus.INITIALIZATION;
        this.capacity.setMiniNum(levelSettings.getMiniPlayerNum());
        this.capacity.setMaxNum(levelSettings.getMaxPlayerNum());
        this.dimension = Dimension.Overworld;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    /**
     * 获取Level的名称
     *
     * @return
     */
    @Override
    public String getLevelName() {
        return levelName;
    }

    @Override
    public Chunk getChunk(long index) {
        return this.chunks.get(index);
    }

    @Override
    public Chunk removeChunk(long index) {
        return this.chunks.remove(index);
    }

    @Override
    public Collection<Chunk> getChunks() {
        return Collections.unmodifiableCollection(this.chunks.values());
    }

    @Override
    public void putChunk(long index, Chunk chunk) {
        this.chunks.put(index, chunk);
    }

    @Override
    public Weather getWeather() {
        return weather;
    }

    @Override
    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public void addTime(int add) {
        this.time += add;
    }

    @Override
    public long getTickCount() {
        return tickCount;
    }

    @Override
    public void increaseTickCount() {
        this.tickCount++;
    }

    @Override
    public long getLastTickTime() {
        return lastTickTime;
    }

    @Override
    public void setLastTickTime(long lastTickTime) {
        this.lastTickTime = lastTickTime;
    }

    @Override
    public long getLastSyncTime() {
        return lastSyncTime;
    }

    @Override
    public void setLastSyncTime(long lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    @Override
    public LevelProviderMapper getLevelProviderMapper() {
        return levelProviderMapper;
    }

    @Override
    public void setLevelProviderMapper(LevelProviderMapper levelProviderMapper) {
        this.levelProviderMapper = levelProviderMapper;
    }

    @Override
    public SceneDataProvider<GameObject> getSceneDataProvider() {
        return sceneDataProvider;
    }

    @Override
    public void setSceneDataProvider(SceneDataProvider<GameObject> sceneDataProvider) {
        this.sceneDataProvider = sceneDataProvider;
    }

    @Override
    public IChunkGenerate getChunkGenerate() {
        return chunkGenerate;
    }

    @Override
    public void setChunkGenerate(IChunkGenerate chunkGenerate) {
        this.chunkGenerate = chunkGenerate;
    }

    @Override
    public LevelSettings getLevelSettings() {
        return levelSettings;
    }

    @Override
    public void setLevelSettings(LevelSettings levelSettings) {
        this.levelSettings = levelSettings;
    }

    @Override
    public LevelStatus getLevelStatus() {
        return levelStatus;
    }

    @Override
    public void setLevelStatus(LevelStatus levelStatus) {
        this.levelStatus = levelStatus;
    }

    @Override
    public Capacity getCapacity() {
        return capacity;
    }

    @Override
    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    @Override
    public IScheduleThread getLevelSchedule() {
        return levelThread;
    }

    @Override
    public ScoreObjective getScoreObjective() {
        return scoreObjective;
    }

    @Override
    public void setScoreObjective(ScoreObjective scoreObjective) {
        this.scoreObjective = scoreObjective;
    }

    @Override
    public List<ScorePacketInfo> getAllScorePacketInfos() {
        return new ArrayList<>(allScorePacketInfos);
    }

    @Override
    public void setAllScorePacketInfos(List<ScorePacketInfo> allScorePacketInfos) {
        this.allScorePacketInfos = allScorePacketInfos;
    }

    @Override
    public boolean isScoreboardDisplay() {
        return isScoreboardDisplay;
    }

    @Override
    public void setScoreboardDisplay(boolean scoreboardDisplay) {
        isScoreboardDisplay = scoreboardDisplay;
    }

    @Override
    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    /**
     * 世界中增加人
     *
     * @return 玩家是否成功加入世界
     */
    @Override
    public Player addPlayer(Player player) {
        Player previousPlayer = this.players.put(player.getIdentifiedStr(), player);
        if (previousPlayer == null) {
            this.capacity.setCurrentNum(this.capacity.getCurrentNum() + 1);
        }

        return previousPlayer;
    }

    /**
     * 世界中减少人
     *
     * @return 玩家是否移除世界
     */
    @Override
    public Player removePlayer(Player player) {
        Player removedPlayer = this.players.remove(player.getIdentifiedStr());

        if (removedPlayer != null) {
            int left = this.capacity.getCurrentNum() - 1;
            if (left < 0) {
                left = 0;
            }
            this.capacity.setCurrentNum(left);
        }

        return removedPlayer;
    }

    @Override
    public Map<String, Player> getPlayers() {
        return this.players;
    }

    @Override
    public int getEntityCount() {
        return entityCount;
    }

    @Override
    public void setEntityCount(int entityCount) {
        this.entityCount = entityCount;
    }
}
