package com.particle.game.entity.spawn;

import com.particle.api.entity.function.IAddEntityPacketBuilder;
import com.particle.api.entity.function.IRemoveEntityPacketBuilder;
import com.particle.api.entity.function.ISpawnEntityProcessor;
import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.level.Chunk;

public class SpawnModule extends BehaviorModule {

    /**
     * 该生物spawn的区块
     */
    private Chunk spawnedChunk;

    /**
     * 增加生物数据包构造器
     */
    private IAddEntityPacketBuilder addEntityPacketBuilder;
    /**
     * 移除生物数据包构造器
     */
    private IRemoveEntityPacketBuilder removeEntityPacketBuilder;

    /**
     * Spawn业务处理器
     */
    private ISpawnEntityProcessor spawnEntityProcessor;

    /**
     * 记录生物spawn的区块
     *
     * @param chunk
     */
    public Chunk spawnAt(Chunk chunk) {
        Chunk spawnedChunk = this.spawnedChunk;

        this.spawnedChunk = chunk;

        return spawnedChunk;
    }

    /**
     * 查询生物spawn的区块
     *
     * @return
     */
    public Chunk getSpawnedChunk() {
        return spawnedChunk;
    }

    /**
     * 重置生物spawn的区块
     *
     * @return
     */
    public Chunk resetSpawnedChunk() {
        Chunk spawnedChunk = this.spawnedChunk;

        this.spawnedChunk = null;

        return spawnedChunk;
    }

    public IAddEntityPacketBuilder getAddEntityPacketBuilder() {
        return addEntityPacketBuilder;
    }

    public void setAddEntityPacketBuilder(IAddEntityPacketBuilder addEntityPacketBuilder) {
        this.addEntityPacketBuilder = addEntityPacketBuilder;
    }

    public IRemoveEntityPacketBuilder getRemoveEntityPacketBuilder() {
        return removeEntityPacketBuilder;
    }

    public void setRemoveEntityPacketBuilder(IRemoveEntityPacketBuilder removeEntityPacketBuilder) {
        this.removeEntityPacketBuilder = removeEntityPacketBuilder;
    }

    public ISpawnEntityProcessor getSpawnEntityProcessor() {
        return spawnEntityProcessor;
    }

    public void setSpawnEntityProcessor(ISpawnEntityProcessor spawnEntityProcessor) {
        this.spawnEntityProcessor = spawnEntityProcessor;
    }

}
