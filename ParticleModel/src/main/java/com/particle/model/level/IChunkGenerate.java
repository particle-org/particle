package com.particle.model.level;

import com.particle.model.level.chunk.ChunkData;

public interface IChunkGenerate {
    ChunkData getEmptyChunk(int xPos, int zPos);
}
