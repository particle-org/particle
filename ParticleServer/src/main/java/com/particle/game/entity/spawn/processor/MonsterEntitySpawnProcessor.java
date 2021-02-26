package com.particle.game.entity.spawn.processor;

import com.particle.api.entity.function.ISpawnEntityProcessor;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.particle.ParticleService;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.LevelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MonsterEntitySpawnProcessor extends EntitySpawnProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonsterEntitySpawnProcessor.class);

    @Inject
    private PositionService positionService;

    @Inject
    private ParticleService particleService;

    public ISpawnEntityProcessor getEntitySpawnProcessor(MonsterEntity entity) {
        return new MonsterEntitySpawnProcessor.SpawnProcess(entity);
    }

    private class SpawnProcess implements ISpawnEntityProcessor {

        private MonsterEntity entity;

        public SpawnProcess(MonsterEntity entity) {
            this.entity = entity;
        }

        @Override
        public void spawn(Level level, Chunk chunk) {
            // 检查并spawn生物
            if (!chunk.getMonsterEntitiesCollection().registerEntity(entity)) {
                return;
            }

            particleService.playParticle(entity.getLevel(), LevelEventType.ParticlesMobBlockSpawn, positionService.getPosition(entity).add(0, 0.2f, 0));
        }

        @Override
        public void respawn(Level level, Chunk from, Chunk to) {
            from.getMonsterEntitiesCollection().removeEntity(entity.getRuntimeId());

            if (!to.getMonsterEntitiesCollection().registerEntity(entity)) {
                LOGGER.error("Fail to respawn entity {}", entity.getRuntimeId());
            }

        }

        @Override
        public void despawn(Chunk chunk) {
            chunk.getMonsterEntitiesCollection().removeEntity(entity.getRuntimeId());
        }
    }

}
