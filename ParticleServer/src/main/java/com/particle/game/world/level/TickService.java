package com.particle.game.world.level;

import com.particle.api.entity.attribute.PositionServiceApi;
import com.particle.core.aoi.container.SceneDataProvider;
import com.particle.core.aoi.model.Grid;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystemManager;
import com.particle.executor.service.AsyncScheduleService;
import com.particle.executor.thread.IScheduleThread;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.entity.spawn.SpawnModule;
import com.particle.game.player.save.PlayerDataService;
import com.particle.model.ecs.ECSSystem;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.player.PlayerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class TickService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TickService.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<SpawnModule> SPAWN_MODULE_HANDLER = ECSModuleHandler.buildHandler(SpawnModule.class);


    /**
     * 存档同步时间，5min
     */
    private static final long SAVE_INTERVAL = 1000 * 60 * 5;

    private static final int TICK_ROUND = 15;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private PlayerDataService playerDataService;

    @Inject
    private PositionServiceApi positionServiceApi;

    /**
     * Tick区块
     */
    public void tickChunks(SceneDataProvider<GameObject> sceneDataProvider, Chunk chunk) {
    }

    public void tickGrid(SceneDataProvider<GameObject> sceneDataProvider, Grid grid, long tickInterval) {
        GameObject gameObject = sceneDataProvider.getData(grid);
        if (gameObject != null) {
            long tickTimestamp = System.currentTimeMillis();

            ECSSystemManager.getECSSystemTickList(gameObject).forEach(
                    ecsSystem -> ecsSystem.tick(tickInterval)
            );

            tickTimestamp = System.currentTimeMillis() - tickTimestamp;
            if (tickTimestamp > 100) {
                LOGGER.warn("Tick grid long at{},{}. cost {}ms.", grid.getX(), grid.getZ(), tickTimestamp);
            }
        }
    }

    /**
     * Tick普通生物
     *
     * @param level
     * @param chunk
     */
    public void tickMobEntities(Level level, Chunk chunk) {
        chunk.getMobEntitiesCollection().getTickedEntitiesViewer().forEach(mobEntity -> {
            boolean state = this.tickEntity(level, mobEntity);
            if (!state) {
                chunk.getMobEntitiesCollection().removeEntity(mobEntity.getRuntimeId());
            }
        });

        // 同步Entity数据
        chunk.getMobEntitiesCollection().syncEntityViewer();
    }

    /**
     * Tick抛射物
     *
     * @param level
     * @param chunk
     */
    public void tickProjectileEntities(Level level, Chunk chunk) {
        chunk.getProjectileEntitiesCollection().getTickedEntitiesViewer().forEach(projectileEntity -> {
            boolean state = this.tickEntity(level, projectileEntity);
            if (!state) {
                chunk.getProjectileEntitiesCollection().removeEntity(projectileEntity.getRuntimeId());
            }
        });

        // 同步Entity数据
        chunk.getProjectileEntitiesCollection().syncEntityViewer();
    }

    /**
     * Tick掉落物
     *
     * @param level
     * @param chunk
     */
    public void tickItemEntities(Level level, Chunk chunk) {
        // Tick Entity
        chunk.getItemEntitiesCollection().getTickedEntitiesViewer().forEach(itemEntity -> {
            boolean state = this.tickEntity(level, itemEntity);
            if (!state) {
                chunk.getItemEntitiesCollection().removeEntity(itemEntity.getRuntimeId());
            }
        });

        // 同步Entity数据
        chunk.getItemEntitiesCollection().syncEntityViewer();
    }

    /**
     * Tick方块生物
     *
     * @param level
     * @param chunk
     */
    public void tickTileEntities(Level level, Chunk chunk) {
        List<TileEntity> tickedTileEntities = chunk.getTickedTileEntities();

        // 检查是否需要构造tick列表
        if (tickedTileEntities == null) {
            chunk.getTileEntitiesCollection().syncEntityViewer();
            tickedTileEntities = chunk.getTileEntitiesCollection().getTickedEntitiesViewer();

            chunk.setTickedTileEntities(tickedTileEntities);
        }

        if (tickedTileEntities != null) {
            // 循环tick
            int currentTickIndex = chunk.getTileEntityTickIndex();

            // 每次最多tick TICK_ROUND 次数
            for (int i = 0; i < TICK_ROUND; i++) {
                // 检查越界
                if (currentTickIndex < tickedTileEntities.size()) {
                    // 如果没有越界，则执行tick操作，并且移动index
                    this.tickEcsSystem(level, tickedTileEntities.get(currentTickIndex));
                    currentTickIndex++;
                } else {
                    // 如果越界越界，则结束当前tick，并重置下标和tick列表
                    currentTickIndex = 0;
                    chunk.setTickedTileEntities(null);
                    break;
                }
            }

            // 缓存下标
            chunk.setTileEntityTickIndex(currentTickIndex);
        }
    }

    /**
     * Tick玩家
     *
     * @param level
     * @param chunk
     */
    public void tickPlayers(Level level, Chunk chunk) {
        long timestamp = System.currentTimeMillis();

        IScheduleThread saveThread = AsyncScheduleService.getInstance().getThread();

        chunk.getPlayersCollection().getTickedEntitiesViewer().forEach((player) -> {
            this.tickEcsSystem(level, player);

            if (timestamp - player.getLastSaveTime() > SAVE_INTERVAL) {
                // 标记保存状态
                player.setLastSaveTime(System.currentTimeMillis());

                if (player.getPlayerState() == PlayerState.SPAWNED) {
                    saveThread.scheduleSimpleTask("PlayerDataSaver", () -> this.playerDataService.save(player));
                }
            }
        });

        // 同步Entity数据
        chunk.getPlayersCollection().syncEntityViewer();
    }

    /**
     * Tick NPC
     *
     * @param level
     * @param chunk
     */
    public void tickNPCs(Level level, Chunk chunk) {
        chunk.getNPCCollection().getTickedEntitiesViewer().forEach(npc -> {
            boolean state = this.tickEntity(level, npc);
            if (!state) {
                chunk.getNPCCollection().removeEntity(npc.getRuntimeId());
            }
        });

        // 同步Entity数据
        chunk.getNPCCollection().syncEntityViewer();
    }

    /**
     * Tick Monster
     *
     * @param level
     * @param chunk
     */
    public void tickMonsters(Level level, Chunk chunk) {
        chunk.getMonsterEntitiesCollection().getTickedEntitiesViewer().forEach(monster -> {
            boolean state = this.tickEntity(level, monster);
            if (!state) {
                chunk.getMonsterEntitiesCollection().removeEntity(monster.getRuntimeId());
            }
        });

        // 同步Entity数据
        chunk.getMonsterEntitiesCollection().syncEntityViewer();
    }

    /**
     * tick生物的基础操作
     *
     * @param level
     * @param entity
     */
    private boolean tickEntity(Level level, Entity entity) {
        try {
            long tickTimestamp = System.currentTimeMillis();

            this.tickEcsSystem(level, entity);

            tickTimestamp = System.currentTimeMillis() - tickTimestamp;

            if (tickTimestamp > 10) {
                LOGGER.warn("Tick entity {}({}) too long. cost {}.", entity.getRuntimeId(), entity.getActorType(), tickTimestamp);
            }
        } catch (Exception e) {
            LOGGER.error("Entity tick exception!", e);
            e.printStackTrace();
            this.entitySpawnService.despawn(entity);

            // Tick失败，返回状态通知
            return false;
        }

        // 移动检测
        if (this.entitySpawnService.isSpawned(entity)) {
            TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
            if (transformModule.getY() < 0 || transformModule.getY() > 480) {
                this.entitySpawnService.despawn(entity);

                // 生物移除，返回状态通知
                return false;
            } else {
                this.entitySpawnService.respawn(level, entity);
            }
        } else {
            // 生物处于despawn状态
            return false;
        }

        return true;
    }

    private boolean tickEcsSystem(Level level, Entity entity) {
        long tickInterval = entity.getTickInterval();

        SpawnModule spawnModule = SPAWN_MODULE_HANDLER.getModule(entity);

        List<com.particle.core.ecs.system.ECSSystem> systemList = ECSSystemManager.getECSSystemTickList(entity);
        int size = systemList.size();
        // 这里用foreach效率较低，改成fori
        for (int i = 0; i < size; i++) {
            if (spawnModule.getSpawnedChunk() == null) {
                return false;
            }

            systemList.get(i).tick(tickInterval);
        }

        // ----- 旧System tick -----
        List<ECSSystem> tickedSystem = entity.getTickedSystem();

        // 缓存Size，UnmodifiedCollection求Size很慢，而且还是LinkedList
        size = tickedSystem.size();

        // 这里用foreach效率较低，改成fori
        for (int i = 0; i < size; i++) {
            if (spawnModule.getSpawnedChunk() == null) {
                return false;
            }

            boolean checked = true;

            ECSSystem ecsSystem = tickedSystem.get(i);
            for (int componentId : ecsSystem.getRequiredComponent()) {
                if (!entity.hasComponent(componentId)) {
                    checked = false;

                    break;
                }
            }

            if (checked) {
                ecsSystem.tick(level, entity, tickInterval);
            }
        }

        entity.updateLastTickTimestamp();

        return true;
    }

    private List<Entity> filterTickedEntities(List<? extends Entity> entities) {
        List<Entity> filterEntities = new ArrayList<>();

        for (Entity entity : entities) {
            if (ECSSystemManager.getECSSystemTickList(entity).size() > 0 || entity.getTickedSystem().size() > 0) {
                filterEntities.add(entity);
            }
        }

        return filterEntities;
    }
}
