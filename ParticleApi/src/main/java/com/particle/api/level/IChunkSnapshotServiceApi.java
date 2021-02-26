package com.particle.api.level;

import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.chunk.ChunkData;

public interface IChunkSnapshotServiceApi {
    ChunkData createSnapshot(Chunk chunk);

    void restoreChunkFromSnapshot(Level level, Chunk chunk, ChunkData chunkData);
}
