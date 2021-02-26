package com.particle.game.world.level;

import com.particle.api.world.WorldServiceApi;
import com.particle.core.aoi.SceneManager;
import com.particle.core.aoi.container.SceneDataProvider;
import com.particle.core.aoi.model.Grid;
import com.particle.core.aoi.model.Scene;
import com.particle.core.ecs.GameObject;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.executor.service.LevelScheduleService;
import com.particle.executor.thread.IScheduleThread;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.world.level.generate.FloatChunkGenerate;
import com.particle.game.world.level.generate.GrassChunkGenerate;
import com.particle.game.world.level.generate.SeaChunkGenerate;
import com.particle.game.world.level.grid.ChunkDataProvider;
import com.particle.game.world.level.loader.ChunkLoaderService;
import com.particle.game.world.level.loader.anvil.CustomFileChunkProviderFactory;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.entity.model.others.NpcEntity;
import com.particle.model.events.level.world.LevelLoadEvent;
import com.particle.model.level.*;
import com.particle.model.level.settings.*;
import com.particle.model.player.GameMode;
import com.particle.util.configer.ConfigServiceProvider;
import com.particle.util.configer.IConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

@Singleton
public class WorldService implements WorldServiceApi {

    public static final String DEFAULT_NAME = "default";

    private static final Logger logger = LoggerFactory.getLogger(WorldService.class);

    private static final int HALF_TICK_MASK = 1;
    private static final int QUARTER_TICK_OFFSET = 2;
    private static final int QUARTER_TICK_MASK = 3;

    @Inject
    private LevelService levelService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private TickService tickService;

    @Inject
    private ChunkLoaderService chunkLoaderService;

    @Inject
    private FloatChunkGenerate floatChunkGenerate;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private CustomFileChunkProviderFactory customFileChunkProviderFactory;

    private GrassChunkGenerate grassChunkGenerate = new GrassChunkGenerate(64);

    private IConfigService configService = ConfigServiceProvider.getConfigService();

    private SeaChunkGenerate seaChunkGenerate = new SeaChunkGenerate(64);

    /**
     * 存储了所有的level
     */
    private Map<String, Level> levels = new ConcurrentHashMap<>();
    private Map<String, Future> futureMap = new ConcurrentHashMap<>();

    @Inject
    public void init() {
        DefaultLevelConfig defaultLevelConfig = this.configService.loadConfigOrSaveDefault(DefaultLevelConfig.class);

        LevelSettings levelSettings = new LevelSettings();

        levelSettings.setSeed(-1);
        levelSettings.setDimension(Dimension.Overworld);
        levelSettings.setGameMode(GameMode.SURVIVE);
        levelSettings.setGameDifficulty(Difficulty.Normal);
        levelSettings.setSpawnX(defaultLevelConfig.getSpawnX());
        levelSettings.setSpawnY(defaultLevelConfig.getSpawnY());
        levelSettings.setSpawnZ(defaultLevelConfig.getSpawnZ());

        levelSettings.setHasAchievementsDisabled(true);
        levelSettings.setDayCycleStopTime(-1);
        levelSettings.setEduMode(false);
        levelSettings.setEduFeature(false);

        levelSettings.setRainLevel(0);
        levelSettings.setLightningLevel(0);
        levelSettings.setCommandsEnabled(true);

        levelSettings.setForceSpawn(defaultLevelConfig.isForceSpawn());

        File defaultMapFile = new File("maps/default");
        if (!defaultMapFile.exists()) {
            defaultMapFile.mkdirs();
        }

        LevelProviderMapper defaultWorld = this.customFileChunkProviderFactory.getProvider("maps/", "default");
        this.registerLevel(DEFAULT_NAME, levelSettings, defaultWorld, this.grassChunkGenerate);
    }

    @Override
    public Level registerLevel(String levelName, LevelSettings levelSettings, LevelProviderMapper levelProviderMapper, IChunkGenerate chunkGenerate) {
        //合法性检查
        if (StringUtils.isEmpty(levelName)) {
            logger.error("add level, the levelName is empty!");
            return null;
        }
        if (this.levels.containsKey(levelName)) {
            logger.error("add level, the levelName[{}] is existed!", levelName);
            return null;
        }
        if (levelSettings == null || levelProviderMapper == null || chunkGenerate == null) {
            return null;
        }

        //创建Level
        IScheduleThread scheduleThread = LevelScheduleService.getInstance().registerThread(levelName);

        Scene scene = SceneManager.getInstance().createScene(levelName, levelSettings.getxMin(), levelSettings.getxMax(), levelSettings.getzMin(), levelSettings.getzMax());
        Level level = new SimpleLevel(scene, levelName, scheduleThread, levelSettings, levelProviderMapper, chunkGenerate);
        level.setSceneDataProvider(new ChunkDataProvider(level, scene));

        //缓存Level
        this.levels.put(levelName, level);

        // 更新Level状态
        level.setLevelStatus(LevelStatus.RUNNING);

        //启动Level的Tick任务
        Future future = scheduleThread.scheduleRepeatingTask(
                String.format("%s_tickTask", levelName),
                () -> this.tickLevel(level, levelSettings),
                levelSettings.getTickInterval());

        // 紀錄 tick future
        futureMap.put(levelName, future);

        this.eventDispatcher.dispatchEvent(new LevelLoadEvent(level));

        return level;
    }

    @Override
    public Level getLevel(String name) {
        return this.levels.get(name);
    }

    /**
     * init
     *
     * @param level
     * @param levelSettings
     */
    private void tickLevel(Level level, LevelSettings levelSettings) {
        // 如果世界为预销毁状态，则销毁世界
        if (level.getLevelStatus() == LevelStatus.DESTROYING) {
            this.destroyLevel(level);

            return;
        }

        // 非运行状态的Level不执行tick
        if (level.getLevelStatus() != LevelStatus.RUNNING) {
            return;
        }

        // 跳过第一次tick
        if (level.getLastTickTime() == 0) {
            level.setLastTickTime(System.currentTimeMillis());
            return;
        }

        // 计算Tick间隔
        long timestamp = System.currentTimeMillis();
        long tickInterval = timestamp - level.getLastTickTime();
        level.setLastTickTime(timestamp);

        long quarterTickInterval = tickInterval << QUARTER_TICK_OFFSET;

        // 计数
        level.increaseTickCount();

        // Level 相关Tick操作
        if (timestamp - level.getLastSyncTime() > 10000) {
            this.levelService.syncTime(level);
            level.setLastSyncTime(timestamp);
        }

        try {
            SceneDataProvider<GameObject> sceneDataProvider = level.getSceneDataProvider();
            level.getChunks().forEach(chunk -> {
                // ItemEntity 每2tick计算一次
                if ((level.getTickCount() & HALF_TICK_MASK) == 0) {
                    if (levelSettings.isTickItemEntity()) {
                        this.tickService.tickItemEntities(level, chunk);
                    }
                }

                // TileEntity 每4tick计算一次
                if ((level.getTickCount() & QUARTER_TICK_MASK) == 0) {
                    if (levelSettings.isTickTileEntity()) {
                        this.tickService.tickTileEntities(level, chunk);
                    }
                }

                // MobEntity 每tick计算一次
                if (levelSettings.isTickMobEntity()) {
                    this.tickService.tickMobEntities(level, chunk);
                }

                // NpcEntity 每tick计算一次
                if (levelSettings.isTickNPCs()) {
                    this.tickService.tickNPCs(level, chunk);
                }

                // Monster 每tick计算一次
                if (levelSettings.isTickMonsters()) {
                    this.tickService.tickMonsters(level, chunk);
                }

                if (levelSettings.isTickProjectileEntity()) {
                    this.tickService.tickProjectileEntities(level, chunk);
                }
                if (levelSettings.isTickPlayers()) {
                    this.tickService.tickPlayers(level, chunk);
                }

                this.tickService.tickChunks(sceneDataProvider, chunk);
            });

            if ((level.getTickCount() & QUARTER_TICK_MASK) == 0) {
                for (Map.Entry<Long, Grid> gridEntry : level.getScene().getGridNodeMap().entrySet()) {
                    this.tickService.tickGrid(sceneDataProvider, gridEntry.getValue(), quarterTickInterval);
                }
            }

        } catch (Exception e) {
            logger.error("Tick entity exception", e);
        }

        GameRule gameRule = levelSettings.getRuleDatas().get(GameRuleName.DO_DAYLIGHT_CYCLE.getName());
        // 若非空 且 循環
        if (gameRule != null && gameRule.isBoolValue()) {
            // 添加时间
            level.addTime(1);
        }
    }

    @Override
    public Level removeLevel(String levelName) {
        logger.info("remove level[{}]", levelName);

        Level level = this.levels.get(levelName);
        if (level == null) {
            return null;
        }

        // 设置level状态为预销毁状态，此时世界会不可交互
        level.setLevelStatus(LevelStatus.DESTROYING);
        return level;
    }

    /**
     * 该方法得在tick中运行
     *
     * @param level
     * @return
     */
    private void destroyLevel(Level level) {
        logger.info("destroy level[{}]", level.getLevelName());

        // 设置level状态为销毁状态
        level.setLevelStatus(LevelStatus.CLOSED);

        // 移除缓存
        this.levels.remove(level.getLevelName());

        // remove世界中的玩家
        this.levelService.kickPlayerInLevel(level);

        // 保存数据
        for (Chunk chunk : level.getChunks()) {
            this.chunkLoaderService.saveChunk(level, chunk, true);
        }

        // 移除場上所有 entity
        for (Chunk chunk : level.getChunks()) {
            for (ItemEntity entity : chunk.getItemEntitiesCollection().getEntities().values()) {
                this.entitySpawnService.despawn(entity);
            }

            for (MobEntity entity : chunk.getMobEntitiesCollection().getEntities().values()) {
                this.entitySpawnService.despawn(entity);
            }

            for (MonsterEntity entity : chunk.getMonsterEntitiesCollection().getEntities().values()) {
                this.entitySpawnService.despawn(entity);
            }

            for (NpcEntity entity : chunk.getNPCCollection().getEntities().values()) {
                this.entitySpawnService.despawn(entity);
            }

            for (Entity entity : chunk.getProjectileEntitiesCollection().getEntities().values()) {
                this.entitySpawnService.despawn(entity);
            }
        }

        // 移除場景
        Scene scene = SceneManager.getInstance().getScene(level.getLevelName());
        if (scene != null) {
            SceneManager.getInstance().destroyScene(scene);
        }

        // 取消 tick
        Future future = futureMap.get(level.getLevelName());
        if (future != null) {
            future.cancel(false);
        }

        // level的tickTask停止
        this.levelService.shutdownSchedule(level);
    }

    @Override
    public Level getDefaultLevel() {
        return this.levels.get(WorldService.DEFAULT_NAME);
    }

    @Override
    public int getLevelCounts() {
        return this.levels.size();
    }

    /**
     * 获取所有的level
     *
     * @return
     */
    public Collection<Level> getAllLevels() {
        return Collections.unmodifiableCollection(this.levels.values());
    }
}
