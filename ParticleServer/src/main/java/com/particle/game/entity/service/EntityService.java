package com.particle.game.entity.service;

import com.particle.api.entity.EntityServiceApi;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.game.world.level.ChunkService;
import com.particle.game.world.level.WorldService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.LevelStatus;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.MoveEntityPacket;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class EntityService implements EntityServiceApi {

    private static Logger logger = LoggerFactory.getLogger(EntityService.class);

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private EntityNameService entityNameService;

    @Inject
    private WorldService worldService;
    @Inject
    private ChunkService chunkService;

    @Inject
    private PositionService positionService;

    /**
     * tp操作
     *
     * @param entity
     * @param position
     */
    @Override
    public void teleport(Entity entity, Vector3f position) {
        this.positionService.setPosition(entity, position);
        Direction direction = this.positionService.getDirection(entity);
        if (direction == null) {
            direction = new Direction(0, 0, 0);
        }
        MoveEntityPacket moveEntityPacket = new MoveEntityPacket();
        moveEntityPacket.setEntityId(entity.getRuntimeId());
        moveEntityPacket.setVector3f(position.add(0, 1.62f, 0));
        moveEntityPacket.setDirection(direction);
        moveEntityPacket.setTeleport(true);
        moveEntityPacket.setOnGround(false);
        logger.debug("tp entity[{}] in position[{}]", entityNameService.getEntityName(entity), position);
        broadcastServiceProxy.broadcast(entity, moveEntityPacket, true);
    }

    @Override
    public void teleport(Entity entity, Vector3f position, Direction direction) {
        this.positionService.setPosition(entity, position);
        this.positionService.setDirection(entity, direction);

        MoveEntityPacket moveEntityPacket = new MoveEntityPacket();
        moveEntityPacket.setEntityId(entity.getRuntimeId());
        moveEntityPacket.setVector3f(position.add(0, 1.62f, 0));
        moveEntityPacket.setDirection(direction);
        moveEntityPacket.setTeleport(true);
        moveEntityPacket.setOnGround(false);

        logger.debug("tp entity[{}] in position[{}]", entityNameService.getEntityName(entity), position);
        broadcastServiceProxy.broadcast(entity, moveEntityPacket, true);
    }

    /**
     * 切换世界
     *
     * @param entity
     * @param newLevel
     */
    @Override
    public void switchLevel(Entity entity, Level newLevel) {
        Vector3f spawnPoint = new Vector3f(newLevel.getLevelSettings().getSpawn());
        this.switchLevel(entity, newLevel, spawnPoint);
    }

    /**
     * 切换世界
     *
     * @param entity
     * @param newLevel
     * @param spawnPosition
     */
    @Override
    public void switchLevel(Entity entity, Level newLevel, Vector3f spawnPosition) {
        if (newLevel == null || newLevel.getLevelStatus() == LevelStatus.DESTROYING
                || newLevel.getLevelStatus() == LevelStatus.CLOSED) {
            return;
        }
        // 从旧世界despawn掉
        if (entity.getLevel() != null) {
            entitySpawnService.despawn(entity);
        }

        positionService.setPosition(entity, spawnPosition);
        //执行tp操作
        this.entitySpawnService.spawn(newLevel, entity);
    }

    /**
     * 切换世界
     *
     * @param entity
     * @param newLevelName
     */
    @Override
    public void switchLevel(Entity entity, String newLevelName) {
        if (StringUtils.isEmpty(newLevelName)) {
            return;
        }
        Level newLevel = this.worldService.getLevel(newLevelName);
        this.switchLevel(entity, newLevel);
    }

    /**
     * 获取某个坐标最近的生物
     *
     * @param level    检查Level
     * @param position 检查坐标
     * @param limit    最远距离
     * @return
     */
    @Override
    public MobEntity getClosestMobEntity(Level level, Vector3f position, float limit) {
        int xMin = ((int) (position.getX() - limit)) / 16;
        int xMax = ((int) (position.getX() + limit)) / 16;
        int zMin = ((int) (position.getZ() - limit)) / 16;
        int zMax = ((int) (position.getZ() + limit)) / 16;

        List<Chunk> chunks = new LinkedList<>();
        for (int i = xMin; i <= xMax; i++) {
            for (int j = zMin; j <= zMax; j++) {
                Chunk chunk = this.chunkService.getChunk(level, i, j, false);
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }

        limit = limit * limit;

        MobEntity closestEntity = null;
        float closestDistance = Float.MAX_VALUE;

        for (Chunk chunk : chunks) {
            for (MobEntity entity : chunk.getMobEntitiesCollection().getEntitiesViewer()) {
                float distance = this.positionService.getPosition(entity).subtract(position).lengthSquared();
                if (distance < limit && distance < closestDistance) {
                    closestDistance = distance;
                    closestEntity = entity;
                }
            }
        }

        return closestEntity;
    }

    /**
     * 获取某个坐标最近的生物
     *
     * @param level    检查Level
     * @param position 检查坐标
     * @param limit    最远距离
     * @return
     */
    public List<MobEntity> getNearMobEntities(Level level, Vector3f position, float limit) {
        int xMin = ((int) (position.getX() - limit)) / 16;
        int xMax = ((int) (position.getX() + limit)) / 16;
        int zMin = ((int) (position.getZ() - limit)) / 16;
        int zMax = ((int) (position.getZ() + limit)) / 16;

        List<Chunk> chunks = new LinkedList<>();
        for (int i = xMin; i <= xMax; i++) {
            for (int j = zMin; j <= zMax; j++) {
                Chunk chunk = this.chunkService.getChunk(level, i, j, false);
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }

        limit = limit * limit;

        List<MobEntity> entityList = new ArrayList<>();

        for (Chunk chunk : chunks) {
            for (MobEntity entity : chunk.getMobEntitiesCollection().getEntitiesViewer()) {
                if (this.positionService.getPosition(entity).subtract(position).lengthSquared() < limit) {
                    entityList.add(entity);
                }
            }
        }

        return entityList;
    }


    @Override
    public MonsterEntity getClosestMonsterEntity(Level level, Vector3f position, float limit) {
        int xMin = ((int) (position.getX() - limit)) / 16;
        int xMax = ((int) (position.getX() + limit)) / 16;
        int zMin = ((int) (position.getZ() - limit)) / 16;
        int zMax = ((int) (position.getZ() + limit)) / 16;

        List<Chunk> chunks = new LinkedList<>();
        for (int i = xMin; i <= xMax; i++) {
            for (int j = zMin; j <= zMax; j++) {
                Chunk chunk = this.chunkService.getChunk(level, i, j, false);
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }

        limit = limit * limit;

        MonsterEntity closestEntity = null;
        float closestDistance = Float.MAX_VALUE;

        for (Chunk chunk : chunks) {
            for (MonsterEntity entity : chunk.getMonsterEntitiesCollection().getEntitiesViewer()) {
                float distance = this.positionService.getPosition(entity).subtract(position).lengthSquared();
                if (distance < limit && distance < closestDistance) {
                    closestDistance = distance;
                    closestEntity = entity;
                }
            }
        }
        return closestEntity;
    }

    @Override
    public List<MonsterEntity> getNearMonsterEntities(Level level, Vector3f position, float limit) {
        int xMin = ((int) (position.getX() - limit)) / 16;
        int xMax = ((int) (position.getX() + limit)) / 16;
        int zMin = ((int) (position.getZ() - limit)) / 16;
        int zMax = ((int) (position.getZ() + limit)) / 16;

        List<Chunk> chunks = new LinkedList<>();
        for (int i = xMin; i <= xMax; i++) {
            for (int j = zMin; j <= zMax; j++) {
                Chunk chunk = this.chunkService.getChunk(level, i, j, false);
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }

        limit = limit * limit;

        List<MonsterEntity> monsterEntityList = new ArrayList<>();

        for (Chunk chunk : chunks) {
            for (MonsterEntity entity : chunk.getMonsterEntitiesCollection().getEntitiesViewer()) {
                if (this.positionService.getPosition(entity).subtract(position).lengthSquared() < limit) {
                    monsterEntityList.add(entity);
                }
            }
        }

        return monsterEntityList;
    }

    @Override
    public List<ItemEntity> getNearItemEntities(Level level, Vector3f position, float limit) {
        int xMin = ((int) (position.getX() - limit)) / 16;
        int xMax = ((int) (position.getX() + limit)) / 16;
        int zMin = ((int) (position.getZ() - limit)) / 16;
        int zMax = ((int) (position.getZ() + limit)) / 16;

        List<Chunk> chunks = new LinkedList<>();
        for (int i = xMin; i <= xMax; i++) {
            for (int j = zMin; j <= zMax; j++) {
                Chunk chunk = this.chunkService.getChunk(level, i, j, false);
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }

        limit = limit * limit;

        List<ItemEntity> itemEntityList = new ArrayList<>();

        for (Chunk chunk : chunks) {
            for (ItemEntity entity : chunk.getItemEntitiesCollection().getEntitiesViewer()) {
                if (this.positionService.getPosition(entity).subtract(position).lengthSquared() < limit) {
                    itemEntityList.add(entity);
                }
            }
        }

        return itemEntityList;
    }
}
