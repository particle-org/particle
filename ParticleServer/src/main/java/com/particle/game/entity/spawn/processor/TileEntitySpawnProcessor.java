package com.particle.game.entity.spawn.processor;

import com.particle.api.entity.function.ISpawnEntityProcessor;
import com.particle.executor.service.AsyncScheduleService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.world.aoi.BroadcastService;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.network.packets.DataPacket;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TileEntitySpawnProcessor extends EntitySpawnProcessor {

    @Inject
    private EntitySpawnService entitySpawnService;

    public ISpawnEntityProcessor getEntitySpawnProcessor(TileEntity entity) {
        return new SpawnProcess(entity);
    }

    private class SpawnProcess implements ISpawnEntityProcessor {

        private TileEntity entity;

        public SpawnProcess(TileEntity entity) {
            this.entity = entity;
        }

        @Override
        public void spawn(Level level, Chunk chunk) {
            // 检查并spawn生物
            chunk.getTileEntitiesCollection().registerEntity(entity);

            // 刷新订阅列表
            if (entity.needNoticeClient()) {
                for (DataPacket dataPacket : entitySpawnService.getEntitySpawnPacket(entity)) {
                    BroadcastService.broadcast(level, chunk, dataPacket);
                }
            }
        }

        @Override
        public void respawn(Level level, Chunk from, Chunk to) {
            throw new RuntimeException("Can't respawn tile entity.");
        }

        @Override
        public void despawn(Chunk chunk) {
            //移除生物
            chunk.getTileEntitiesCollection().removeEntity(entity.getRuntimeId());

            // 刷新订阅列表
            if (entity.needNoticeClient()) {
                for (DataPacket dataPacket : entitySpawnService.getEntityDespawnPacket(entity)) {
                    BroadcastService.broadcast(entity.getLevel(), chunk, dataPacket);
                }
            }

            AsyncScheduleService.getInstance().getThread().scheduleSerialSimpleTask("Save Tile Entity", "TileEntitySave", () -> {
                chunk.getProvider().removeTileEntity(entity);
            });
        }
    }
}
