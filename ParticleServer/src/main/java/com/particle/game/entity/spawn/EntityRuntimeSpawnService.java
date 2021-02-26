package com.particle.game.entity.spawn;

import com.particle.api.entity.IEntityRuntimeSpawnServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.ChunkService;
import com.particle.game.world.level.IChunkLoadCallback;
import com.particle.game.world.level.IChunkUnloadCallback;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.entity.model.others.NpcEntity;
import com.particle.model.entity.model.projectile.ProjectileEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.chunk.ChunkState;
import com.particle.model.math.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运行时spawn服务，区块卸载时该业务缓存entity，区块重新加载时再填回来
 */
@Singleton
public class EntityRuntimeSpawnService implements IEntityRuntimeSpawnServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRuntimeSpawnService.class);

    private static final ECSModuleHandler<LevelBindEntityModule> LEVEL_BIND_ENTITY_MODULE_HANDLER = ECSModuleHandler.buildHandler(LevelBindEntityModule.class);

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private PositionService positionService;

    @Inject
    private ChunkService chunkService;

    /**
     * 记录缓存的entity及其准备传送到的区块，key entityId， val chunkIndex
     */
    private Map<Entity, Long> entityCache = new HashMap<>();

    @Inject
    public void init() {
        // 注册接口
        try {
            this.chunkService.registerChunkLoadCallback(new ChunkLoadCheck());
            this.chunkService.registerChunkUnloadCallback(new ChunkUnloadCheck());
        } catch (Exception e) {
            LOGGER.error("Fail to register chunk callback");

            throw e;
        }
    }

    /**
     * 将生物注册到本业务，启动托管
     *
     * @param level
     * @param entity
     */
    @Override
    public void spawn(Level level, Entity entity) {
        // spawn到世界里面
        if (!(entity instanceof MobEntity || entity instanceof NpcEntity || entity instanceof ProjectileEntity || entity instanceof ItemEntity || entity instanceof MonsterEntity)) {
            throw new RuntimeException("Not support entity!");
        }

        // 标记
        this.entityCache.put(entity, null);

        // 判断是否能spawn
        Vector3f position = this.positionService.getPosition(entity);
        Chunk chunk = this.chunkService.indexChunk(level, position);

        // 检查当前是否可以spawn
        if (chunk == null || chunk.getState() != ChunkState.RUNNING) {
            // 生物spawn的区块不存在，计入缓存
            long chunkIndex = this.chunkService.getChunkIndex(position.getFloorX() / 16, position.getFloorZ() / 16);
            this.entityCache.put(entity, chunkIndex);

            LEVEL_BIND_ENTITY_MODULE_HANDLER.bindModule(level).getChunkToEntitiesCache().computeIfAbsent(chunkIndex, k -> new ArrayList<>()).add(entity);
        } else {
            // 生物spawn的区块存在，直接spawn到区块上
            this.entitySpawnService.spawn(level, entity);
        }
    }

    /**
     * 将生物从本业务取消托管
     *
     * @param entity
     */
    @Override
    public void despawn(Entity entity) {
        // 处理缓存
        Long entityChunkIndex = this.entityCache.remove(entity);

        // 检查该生物是否spawn到世界中
        if (entityChunkIndex == null) {
            // 如果spawn在世界中，则执行despawn操作
            this.entitySpawnService.despawn(entity);
        } else {
            // 如果没有spawn在世界中，则执行缓存清理操作
            if (entity.getLevel() == null) {
                return;
            }

            Map<Long, List<Entity>> chunkToEntitiesCache = LEVEL_BIND_ENTITY_MODULE_HANDLER.bindModule(entity.getLevel()).getChunkToEntitiesCache();

            List<Entity> entities = chunkToEntitiesCache.get(entityChunkIndex);

            if (entities != null) {
                entities.remove(entity);
            }
        }
    }

    private class ChunkLoadCheck implements IChunkLoadCallback {
        @Override
        public void onload(Level level, Chunk chunk) {
            // 查找缓存
            long chunkIndex = chunkService.getChunkIndex(chunk);

            // 查找是否有生物在该区块待spawn
            List<Entity> entities = LEVEL_BIND_ENTITY_MODULE_HANDLER.bindModule(level).getChunkToEntitiesCache().remove(chunkIndex);
            if (entities != null) {
                // 清除缓存
                for (Entity entity : entities) {
                    entityCache.put(entity, null);

                    // 注册到世界中
                    if (entity instanceof MobEntity) {
                        chunk.getMobEntitiesCollection().registerEntity((MobEntity) entity);
                    } else if (entity instanceof NpcEntity) {
                        chunk.getNPCCollection().registerEntity((NpcEntity) entity);
                    } else if (entity instanceof ProjectileEntity) {
                        chunk.getProjectileEntitiesCollection().registerEntity((ProjectileEntity) entity);
                    } else if (entity instanceof ItemEntity) {
                        chunk.getItemEntitiesCollection().registerEntity((ItemEntity) entity);
                    } else if (entity instanceof MonsterEntity) {
                        chunk.getMonsterEntitiesCollection().registerEntity((MonsterEntity) entity);
                    }
                }
            }
        }
    }

    private class ChunkUnloadCheck implements IChunkUnloadCallback {
        @Override
        public void onunload(Level level, Chunk chunk) {
            Map<Long, List<Entity>> chunkToEntitiesCache = LEVEL_BIND_ENTITY_MODULE_HANDLER.bindModule(level).getChunkToEntitiesCache();

            // 查找缓存
            long chunkIndex = chunkService.getChunkIndex(chunk);
            List<Entity> entities = null;

            // 缓存本业务托管的Entity
            for (Entity mobEntity : chunk.getMobEntitiesCollection().getEntitiesViewer()) {
                // 检查是否存在托管情况
                if (entityCache.containsKey(mobEntity)) {
                    // 懒加载初始化缓存数组
                    if (entities == null) {
                        entities = chunkToEntitiesCache.computeIfAbsent(chunkIndex, k -> new ArrayList<>());
                    }

                    // 记录缓存
                    entities.add(mobEntity);

                    // 记录缓存index
                    entityCache.put(mobEntity, chunkIndex);
                }
            }

            // 缓存本业务托管的Entity
            for (Entity monsterEntity : chunk.getMonsterEntitiesCollection().getEntitiesViewer()) {
                // 检查是否存在托管情况
                if (entityCache.containsKey(monsterEntity)) {
                    // 懒加载初始化缓存数组
                    if (entities == null) {
                        entities = chunkToEntitiesCache.computeIfAbsent(chunkIndex, k -> new ArrayList<>());
                    }

                    // 记录缓存
                    entities.add(monsterEntity);

                    // 记录缓存index
                    entityCache.put(monsterEntity, chunkIndex);
                }
            }

            // 缓存本业务托管的Entity
            for (Entity npcEntity : chunk.getNPCCollection().getEntitiesViewer()) {
                // 检查是否存在托管情况
                if (entityCache.containsKey(npcEntity)) {
                    // 懒加载初始化缓存数组
                    if (entities == null) {
                        entities = chunkToEntitiesCache.computeIfAbsent(chunkIndex, k -> new ArrayList<>());
                    }

                    // 记录缓存
                    entities.add(npcEntity);

                    // 记录缓存index
                    entityCache.put(npcEntity, chunkIndex);
                }
            }
        }
    }
}
