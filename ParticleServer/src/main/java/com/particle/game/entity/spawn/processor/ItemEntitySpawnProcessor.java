package com.particle.game.entity.spawn.processor;

import com.particle.api.entity.function.ISpawnEntityProcessor;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class ItemEntitySpawnProcessor extends EntitySpawnProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemEntitySpawnProcessor.class);

    public ISpawnEntityProcessor getEntitySpawnProcessor(ItemEntity entity) {
        return new SpawnProcess(entity);
    }

    private class SpawnProcess implements ISpawnEntityProcessor {

        private ItemEntity entity;

        public SpawnProcess(ItemEntity entity) {
            this.entity = entity;
        }

        @Override
        public void spawn(Level level, Chunk chunk) {
            // 检查并spawn生物
            if (!chunk.getItemEntitiesCollection().registerEntity(entity)) {
                return;
            }
        }

        @Override
        public void respawn(Level level, Chunk from, Chunk to) {
            //重新注册chunk
            from.getItemEntitiesCollection().removeEntity(entity.getRuntimeId());

            if (!to.getItemEntitiesCollection().registerEntity(entity)) {
                LOGGER.error("Fail to respawn entity {}", entity.getRuntimeId());
            }
        }

        @Override
        public void despawn(Chunk chunk) {
            chunk.getItemEntitiesCollection().removeEntity(entity.getRuntimeId());
        }
    }

}
