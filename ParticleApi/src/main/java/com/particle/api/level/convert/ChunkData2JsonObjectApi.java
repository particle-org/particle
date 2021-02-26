package com.particle.api.level.convert;

import com.alibaba.fastjson.JSONObject;
import com.particle.model.level.chunk.ChunkData;

public interface ChunkData2JsonObjectApi {
    ChunkData fromNBT(JSONObject data);

    JSONObject toNbt(ChunkData chunkData);
}
