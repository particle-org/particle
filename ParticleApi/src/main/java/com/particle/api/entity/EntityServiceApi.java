package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.level.Level;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;

import java.util.List;

public interface EntityServiceApi {

    /**
     * tp生物至指定地点
     *
     * @param entity   目標生物
     * @param position 位置
     */
    void teleport(Entity entity, Vector3f position);

    /**
     * tp生物至指定地点，并设置朝向
     *
     * @param entity    目標生物
     * @param position  位置
     * @param direction 朝向
     */
    void teleport(Entity entity, Vector3f position, Direction direction);

    /**
     * 修改生物所在的Level
     *
     * @param entity   目標生物
     * @param newLevel 新的Level
     */
    void switchLevel(Entity entity, Level newLevel);

    /**
     * 修改生物所在的Level
     *
     * @param entity
     * @param newLevel
     * @param spawnPosition
     */
    void switchLevel(Entity entity, Level newLevel, Vector3f spawnPosition);

    /**
     * 修改生物所在的Level
     *
     * @param entity       目標生物
     * @param newLevelName 新的Level名称
     */
    void switchLevel(Entity entity, String newLevelName);

    /**
     * 获取某个坐标最近的生物（不跨区快检查）
     *
     * @param level    检查Level
     * @param position 检查坐标
     * @param limit    最远距离
     * @return 获取某个坐标最近的生物
     */
    Entity getClosestMobEntity(Level level, Vector3f position, float limit);

    /**
     * 获取某个坐标最近的MonsterEntity（不跨区快检查）
     *
     * @param level    检查Level
     * @param position 检查坐标
     * @param limit    最远距离
     * @return 获取某个坐标最近的Monster
     */
    MonsterEntity getClosestMonsterEntity(Level level, Vector3f position, float limit);

    /**
     * 获取某个坐标limit范围内的MonsterEntity列表（不跨区快检查）
     *
     * @param level    检查Level
     * @param position 检查坐标
     * @param limit    最远距离
     * @return 获取某个坐标最近的生物
     */
    List<MonsterEntity> getNearMonsterEntities(Level level, Vector3f position, float limit);

    /**
     * 获取某个坐标limit范围内的ItemEntity列表（不跨区快检查）
     *
     * @param level    检查Level
     * @param position 检查坐标
     * @param limit    最远距离
     * @return 获取某个坐标最近的Item
     */
    List<ItemEntity> getNearItemEntities(Level level, Vector3f position, float limit);
}

