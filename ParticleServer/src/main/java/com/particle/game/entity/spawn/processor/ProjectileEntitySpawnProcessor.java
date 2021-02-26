package com.particle.game.entity.spawn.processor;

import com.particle.api.entity.function.ISpawnEntityProcessor;
import com.particle.model.entity.Entity;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class ProjectileEntitySpawnProcessor extends EntitySpawnProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectileEntitySpawnProcessor.class);

    public ISpawnEntityProcessor getEntitySpawnProcessor(Entity entity) {
        return new SpawnProcess(entity);
    }

    private class SpawnProcess implements ISpawnEntityProcessor {

        private Entity entity;

        public SpawnProcess(Entity entity) {
            this.entity = entity;
        }

        @Override
        public void spawn(Level level, Chunk chunk) {
            // 检查并spawn生物
            if (!chunk.getProjectileEntitiesCollection().registerEntity(entity)) {
                return;
            }
        }

        @Override
        public void respawn(Level level, Chunk from, Chunk to) {
            from.getProjectileEntitiesCollection().removeEntity(entity.getRuntimeId());

            if (!to.getProjectileEntitiesCollection().registerEntity(entity)) {
                LOGGER.error("Fail to respawn entity {}", entity.getRuntimeId());
            }
        }

        @Override
        public void despawn(Chunk chunk) {
            //移除生物
            chunk.getProjectileEntitiesCollection().removeEntity(entity.getRuntimeId());
        }
    }
}
