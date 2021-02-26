package com.particle.game.entity.service;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attack.component.ProjectileAttackModule;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.model.entity.Entity;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public class EntityRecorderService {

    private static final ECSModuleHandler<ProjectileAttackModule> PROJECTILE_ATTACK_MODULE_HANDLER = ECSModuleHandler.buildHandler(ProjectileAttackModule.class);

    private static final int MAX_PROJECTILE_ENTITY = 50;

    private Map<Long, Entity> spawnedEntity = new ConcurrentHashMap<>();

    private Queue<Entity> spawnedProjectileEntities = new ConcurrentLinkedQueue<>();

    @Inject
    private EntitySpawnService entitySpawnService;

    /**
     * 注册生物
     *
     * @param entity
     */
    public void registerSpawnedEntity(Entity entity) {
        this.spawnedEntity.put(entity.getRuntimeId(), entity);

        if (PROJECTILE_ATTACK_MODULE_HANDLER.hasModule(entity)) {
            spawnedProjectileEntities.add(entity);

            if (spawnedProjectileEntities.size() > MAX_PROJECTILE_ENTITY) {
                Entity removedEntity = spawnedProjectileEntities.poll();

                if (removedEntity != null && this.entitySpawnService.isSpawned(removedEntity)) {
                    this.entitySpawnService.despawn(removedEntity);
                }
            }
        }
    }

    public void removeSpawnedEntity(long entityId) {
        this.spawnedEntity.remove(entityId);
    }

    /**
     * 查询生物
     *
     * @param entityId
     * @return
     */
    public Entity getEntity(long entityId) {
        return this.spawnedEntity.get(entityId);
    }

    public <T> T getEntity(long entityId, Class<T> entityType) {
        Entity entity = this.spawnedEntity.get(entityId);

        if (entity.getClass() == entityType) {
            return (T) entity;
        }

        return null;
    }
}
