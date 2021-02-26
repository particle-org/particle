package com.particle.model.network.packets.data;

import com.particle.model.level.Chunk;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.util.List;

public class FullChunkDataPacket extends DataPacket {
    private int chunkX;
    private int chunkZ;
    private int subChunkCount;
    private boolean cacheEnabled;
    private long[] blobIds;

    private List<byte[]> chunkSections;
    private byte[] heightMap;
    private byte[] biomes;
    private List<NBTTagCompound> tileEntities;


    // 區分版本用
    private Chunk chunk;

    @Override
    public int pid() {
        return ProtocolInfo.FULL_CHUNK_DATA_PACKET;
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    public int getSubChunkCount() {
        return subChunkCount;
    }

    public void setSubChunkCount(int subChunkCount) {
        this.subChunkCount = subChunkCount;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public long[] getBlobIds() {
        return blobIds;
    }

    public void setBlobIds(long[] blobIds) {
        this.blobIds = blobIds;
    }


    public List<byte[]> getChunkSections() {
        return chunkSections;
    }

    public void setChunkSections(List<byte[]> chunkSections) {
        this.chunkSections = chunkSections;
    }

    public byte[] getHeightMap() {
        return heightMap;
    }

    public void setHeightMap(byte[] heightMap) {
        this.heightMap = heightMap;
    }

    public byte[] getBiomes() {
        return biomes;
    }

    public void setBiomes(byte[] biomes) {
        this.biomes = biomes;
    }

    public List<NBTTagCompound> getTileEntities() {
        return tileEntities;
    }

    public void setTileEntities(List<NBTTagCompound> tileEntities) {
        this.tileEntities = tileEntities;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }
}
