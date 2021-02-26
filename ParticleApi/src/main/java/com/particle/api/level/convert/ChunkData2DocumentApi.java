package com.particle.api.level.convert;

import com.particle.model.level.chunk.ChunkData;
import org.bson.Document;

public interface ChunkData2DocumentApi {
    Document toDocument(ChunkData chunkData);

    ChunkData fromDocument(Document documentChunks);
}
