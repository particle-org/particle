package com.particle.model.level.chunk;

public class ChunkSection {

    private final byte y;
    private short[] blocks;
    private byte[] data;
    private byte[] blockLight;
    private byte[] skyLight;

    private byte[] encodeCache;
    private int latestEncodeVersion;

    public ChunkSection(byte y) {
        this.y = y;
        this.blocks = new short[4096];
        this.data = new byte[4096];
        this.blockLight = new byte[4096];
        this.skyLight = new byte[4096];
    }

    public byte getY() {
        return y;
    }

    public short getBlockId(int x, int y, int z) {
        return this.blocks[((y & 0xf) << 8) | ((z & 0xf) << 4) | x & 0xf];
    }

    public void setBlockId(int x, int y, int z, short id) {
        this.blocks[((y & 0xf) << 8) | ((z & 0xf) << 4) | x & 0xf] = id;
    }

    public byte getBlockData(int x, int y, int z) {
        return this.data[((y & 0xf) << 8) | ((z & 0xf) << 4) | x & 0xf];
    }

    public void setBlockData(int x, int y, int z, byte data) {
        this.data[((y & 0xf) << 8) | ((z & 0xf) << 4) | x & 0xf] = data;
    }

    public boolean setBlock(int x, int y, int z, int blockId) {
        int index = ((y & 0xf) << 8) | ((z & 0xf) << 4) | x & 0xf;
        if (this.blocks[index] != blockId) {
            this.blocks[index] = (short) blockId;

            return true;
        }

        return false;
    }

    public boolean setBlock(int x, int y, int z, int blockId, int meta) {
        int index = ((y & 0xf) << 8) | ((z & 0xf) << 4) | x & 0xf;

        boolean changed = false;

        if (this.blocks[index] != blockId) {
            this.blocks[index] = (short) blockId;
            changed = true;
        }

        if (this.data[index] != meta) {
            this.data[index] = (byte) meta;
            changed = true;
        }

        return changed;
    }

    public void setBlockSkyLight(int x, int y, int z, byte level) {
        int index = ((y & 0xf) << 8) | ((z & 0xf) << 4) | x & 0xf;

        this.skyLight[index] = level;
    }

    public void setBlockLight(int x, int y, int z, byte level) {
        int index = ((y & 0xf) << 8) | ((z & 0xf) << 4) | x & 0xf;

        this.blockLight[index] = level;
    }

    public byte getBlockSkyLight(int x, int y, int z) {
        return this.skyLight[((y & 0xf) << 8) | ((z & 0xf) << 4) | x & 0xf];
    }

    public byte getBlockLight(int x, int y, int z) {
        return this.blockLight[((y & 0xf) << 8) | ((z & 0xf) << 4) | x & 0xf];
    }

    public byte getLightAt(int x, int y, int z) {
        int index = ((y & 0xf) << 8) | ((z & 0xf) << 4) | x & 0xf;

        if (this.skyLight[index] > this.blockLight[index]) {
            return this.skyLight[index];
        } else {
            return this.blockLight[index];
        }
    }


    // -------------------- Get 和 Set ---------------------------
    public short[] getBlocks() {
        return blocks;
    }

    public byte[] getData() {
        return data;
    }

    public byte[] getBlockLight() {
        return blockLight;
    }

    public byte[] getSkyLight() {
        return skyLight;
    }

    public void setBlocks(short[] blocks) {
        this.blocks = blocks;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setBlockLight(byte[] blockLight) {
        this.blockLight = blockLight;
    }

    public void setSkyLight(byte[] skyLight) {
        this.skyLight = skyLight;
    }

    public byte[] getEncodeCache() {
        return encodeCache;
    }

    public void setEncodeCache(byte[] encodeCache) {
        this.encodeCache = encodeCache;
    }

    public int getLatestEncodeVersion() {
        return latestEncodeVersion;
    }

    public void setLatestEncodeVersion(int latestEncodeVersion) {
        this.latestEncodeVersion = latestEncodeVersion;
    }

    @Override
    public ChunkSection clone() {
        ChunkSection section = new ChunkSection(this.y);
        section.skyLight = this.skyLight.clone();
        section.blockLight = this.blockLight.clone();
        section.blocks = this.blocks.clone();
        section.data = this.data.clone();
        return section;
    }
}
