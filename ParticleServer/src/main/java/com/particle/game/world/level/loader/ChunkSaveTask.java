package com.particle.game.world.level.loader;

import com.particle.executor.thread.IScheduleThread;
import com.particle.model.level.LevelProvider;
import com.particle.model.level.chunk.ChunkData;

public class ChunkSaveTask {
    private LevelProvider provider;
    private boolean release;
    private ChunkData chunk;

    private Runnable callback;
    private IScheduleThread callbackThread;

    public ChunkSaveTask(LevelProvider provider, ChunkData chunk, boolean release) {
        this.provider = provider;
        this.chunk = chunk;
        this.release = release;
    }

    public LevelProvider getProvider() {
        return provider;
    }

    public int getChunkX() {
        return this.chunk.getxPos();
    }

    public int getChunkZ() {
        return this.chunk.getzPos();
    }

    public ChunkData getChunk() {
        return this.chunk;
    }

    public boolean isRelease() {
        return release;
    }

    public Runnable getCallback() {
        return callback;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    public IScheduleThread getCallbackThread() {
        return callbackThread;
    }

    public void setCallbackThread(IScheduleThread callbackThread) {
        this.callbackThread = callbackThread;
    }
}