package com.particle.api.entity;

import com.particle.api.entity.function.IAddEntityPacketBuilder;
import com.particle.api.entity.function.IRemoveEntityPacketBuilder;
import com.particle.api.entity.function.ISpawnEntityProcessor;
import com.particle.model.entity.Entity;
import com.particle.model.level.Level;
import com.particle.model.network.packets.DataPacket;

public interface EntitySpawnServiceApi {

    void enableSpawn(Entity entity, ISpawnEntityProcessor spawnEntityProcessor, IAddEntityPacketBuilder addEntityPacketBuilder, IRemoveEntityPacketBuilder removeEntityPacketBuilder);

    boolean isSpawned(Entity entity);

    /**
     * 将生物注册到世界中
     *
     * @param level
     * @param entity
     */
    boolean spawnEntity(Level level, Entity entity);

    /**
     * 从世界移除生物
     *
     * @param entity 操作的生物
     */
    void despawnEntity(Entity entity);

    DataPacket[] getEntitySpawnPacket(Entity entity);

    DataPacket[] getEntityDespawnPacket(Entity entity);


}

