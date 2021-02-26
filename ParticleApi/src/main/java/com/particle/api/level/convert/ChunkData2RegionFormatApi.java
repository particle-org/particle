package com.particle.api.level.convert;

import com.particle.model.level.chunk.ChunkData;
import com.particle.model.nbt.NBTTagCompound;

public interface ChunkData2RegionFormatApi {
    ChunkData fromNBT(NBTTagCompound chunkTag);

    NBTTagCompound toNbt(ChunkData chunkData);
}
