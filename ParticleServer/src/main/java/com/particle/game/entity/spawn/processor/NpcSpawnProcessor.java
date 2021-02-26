package com.particle.game.entity.spawn.processor;

import com.particle.api.entity.function.ISpawnEntityProcessor;
import com.particle.model.entity.model.others.NpcEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class NpcSpawnProcessor extends EntitySpawnProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(NpcSpawnProcessor.class);

    public ISpawnEntityProcessor getEntitySpawnProcessor(NpcEntity entity) {
        return new SpawnProcess(entity);
    }

    private class SpawnProcess implements ISpawnEntityProcessor {

        private NpcEntity entity;

        public SpawnProcess(NpcEntity entity) {
            this.entity = entity;
        }

        @Override
        public void spawn(Level level, Chunk chunk) {
            //检查并spawn生物
            if (!chunk.getNPCCollection().registerEntity(entity)) {
                return;
            }
        }

        @Override
        public void respawn(Level level, Chunk from, Chunk to) {
            from.getNPCCollection().removeEntity(entity.getRuntimeId());

            if (!to.getNPCCollection().registerEntity(entity)) {
                LOGGER.error("Fail to respawn entity {}", entity.getRuntimeId());
            }
        }

        @Override
        public void despawn(Chunk chunk) {
            chunk.getNPCCollection().removeEntity(entity.getRuntimeId());
        }
    }

}

