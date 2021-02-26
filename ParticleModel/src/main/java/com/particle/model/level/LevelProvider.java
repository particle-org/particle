package com.particle.model.level;

import com.particle.core.ecs.module.ECSModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.chunk.ChunkData;

public interface LevelProvider {

    /**
     * 保存区块数据
     *
     * @param chunkData
     * @param release
     */
    void saveChunk(ChunkData chunkData, boolean release);

    /**
     * 读取区块数据
     *
     * @param x
     * @param z
     * @return
     */
    ChunkData loadChunk(int x, int z);

    /**
     * 判断区块是否存在
     *
     * @param x
     * @param z
     * @return
     */
    boolean isChunkExist(int x, int z);

    /**
     * 保存指定TileEntity的数据
     *
     * @param tileEntity
     */
    void saveTileEntity(TileEntity tileEntity);

    /**
     * 删除指定的TileEntity数据
     *
     * @param tileEntity
     */
    void removeTileEntity(TileEntity tileEntity);

    /**
     * 读取模块
     *
     * @param chunk
     * @param moduleName
     */
    void loadModule(Chunk chunk, String moduleName);

    /**
     * 保存Chunk锁存储的额外数据
     *
     * @param chunk
     * @param module
     */
    void saveModule(Chunk chunk, ECSModule module);
}
