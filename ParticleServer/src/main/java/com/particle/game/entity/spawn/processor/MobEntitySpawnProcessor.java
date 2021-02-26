package com.particle.game.entity.spawn.processor;

import com.particle.api.entity.function.ISpawnEntityProcessor;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.particle.ParticleService;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.LevelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MobEntitySpawnProcessor extends EntitySpawnProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobEntitySpawnProcessor.class);

    @Inject
    private PositionService positionService;

    @Inject
    private ParticleService particleService;

    public ISpawnEntityProcessor getEntitySpawnProcessor(MobEntity entity) {
        return new SpawnProcess(entity);
    }

    private class SpawnProcess implements ISpawnEntityProcessor {

        private MobEntity entity;

        public SpawnProcess(MobEntity entity) {
            this.entity = entity;
        }

        @Override
        public void spawn(Level level, Chunk chunk) {
            // 检查并spawn生物
            if (!chunk.getMobEntitiesCollection().registerEntity(entity)) {
                return;
            }

            particleService.playParticle(entity.getLevel(), LevelEventType.ParticlesMobBlockSpawn, positionService.getPosition(entity).add(0, 0.2f, 0));
        }

        @Override
        public void respawn(Level level, Chunk from, Chunk to) {
            from.getMobEntitiesCollection().removeEntity(entity.getRuntimeId());

            if (!to.getMobEntitiesCollection().registerEntity(entity)) {
                LOGGER.error("Fail to respawn entity {}", entity.getRuntimeId());
            }

        }

        @Override
        public void despawn(Chunk chunk) {
            chunk.getMobEntitiesCollection().removeEntity(entity.getRuntimeId());
        }
    }


}
