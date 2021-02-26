package com.particle.game.world.aoi.components;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.level.Chunk;

import java.util.HashSet;
import java.util.Set;

public class SubscriptModule extends BehaviorModule {

    /**
     * 订阅者所在的区块
     */
    private Chunk currentChunk = null;

    /**
     * 该生物订阅的区块
     */
    private Set<Long> subscriptChunkIndex = new HashSet<>();

    /**
     * Chunk加载范围
     */
    private int chunkLoadRadius;

    /**
     * Chunk卸载范围
     */
    private int chunkUnloadRadius;

    public Chunk getCurrentChunk() {
        return currentChunk;
    }

    public void setCurrentChunk(Chunk currentChunk) {
        this.currentChunk = currentChunk;
    }

    public Set<Long> getSubscriptChunkIndex() {
        return subscriptChunkIndex;
    }

    public void setSubscriptChunkIndex(Set<Long> subscriptChunkIndex) {
        this.subscriptChunkIndex = subscriptChunkIndex;
    }

    public int getChunkLoadRadius() {
        return chunkLoadRadius;
    }

    public void setChunkLoadRadius(int chunkLoadRadius) {
        this.chunkLoadRadius = chunkLoadRadius;
    }

    public int getChunkUnloadRadius() {
        return chunkUnloadRadius;
    }

    public void setChunkUnloadRadius(int chunkUnloadRadius) {
        this.chunkUnloadRadius = chunkUnloadRadius;
    }
}
