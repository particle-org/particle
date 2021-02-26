package com.particle.game.world.level.loader;

import com.particle.model.level.Chunk;
import com.particle.model.level.Level;

public class ChunkLoadTask {
    private Level level;

    private boolean cancelled;
    private boolean start;
    private boolean retry;

    private Chunk chunk;


    public ChunkLoadTask(Level level, Chunk chunk) {
        this.level = level;
        this.chunk = chunk;
    }

    public Level getLevel() {
        return level;
    }

    public int getChunkX() {
        return this.chunk.getxPos();
    }

    public int getChunkZ() {
        return this.chunk.getzPos();
    }

    public void start() {
        this.start = true;
    }

    public boolean cancel() {
        if (start) {
            return false;
        } else {
            cancelled = true;

            return true;
        }
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isDone() {
        return this.chunk.isRunning();
    }

    public Chunk getChunk() {
        return this.chunk;
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }
}