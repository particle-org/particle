package com.particle.game.world.level.loader;

import com.particle.executor.thread.IScheduleThread;
import com.particle.game.world.level.ChunkService;
import com.particle.game.world.level.ChunkSnapshotService;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChunkLoaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkLoaderService.class);

    @Inject
    private ChunkService chunkService;

    private ChunkIOPool chunkIOPool;

    @Inject
    public void init() {
        this.chunkIOPool = new ChunkIOPool(this.chunkService);
    }

    public ChunkLoadTask loadChunk(Level level, int chunkX, int chunkZ) {
        return this.chunkIOPool.loadChunk(level, chunkX, chunkZ);
    }

    public void saveChunk(Level level, Chunk chunk, boolean release) {
        this.saveChunk(level, chunk, release, null, null);
    }

    public void saveChunk(Level level, Chunk chunk, boolean release, Runnable callback, IScheduleThread callbackThread) {
        // 检查区块状态,如果区块还未加载，则不执行存储操作
        if (chunk.getState() == ChunkState.UNLOAD || chunk.getState() == ChunkState.CLOSED) {
            return;
        }

        if (level.getLevelSettings().isReadOnly()) {
            if (callback != null && callbackThread != null) {
                callbackThread.scheduleSimpleTask("Chunk load callback", callback::run);
            }
        } else {
            ChunkData snapshot = ChunkSnapshotService.createSnapshot(chunk);

            this.chunkIOPool.saveChunk(chunk.getProvider(), snapshot, release, callback, callbackThread);
        }
    }

    public void shutdown() {
        this.chunkIOPool.shutdown();
    }
}
