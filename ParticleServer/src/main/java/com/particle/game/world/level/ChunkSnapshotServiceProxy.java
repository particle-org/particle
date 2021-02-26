package com.particle.game.world.level;

import com.particle.api.level.IChunkSnapshotServiceApi;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.chunk.ChunkData;

public class ChunkSnapshotServiceProxy implements IChunkSnapshotServiceApi {

    @Override
    public ChunkData createSnapshot(Chunk chunk) {
        return ChunkSnapshotService.createSnapshot(chunk);
    }

    @Override
    public void restoreChunkFromSnapshot(Level level, Chunk chunk, ChunkData chunkData) {
        ChunkSnapshotService.restoreChunkFromSnapshot(level, chunk, chunkData);
    }

}
