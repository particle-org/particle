package com.particle.api.world;

import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

public interface ChunkServiceAPI {

    /**
     * 通过chunk的坐标获取chunk
     *
     * @param chunkX ChunkX坐标
     * @param chunkZ ChunkZ坐标
     * @return
     */
    Chunk getChunk(Level level, int chunkX, int chunkZ);

    /**
     * 通过chunk的坐标获取chunk
     *
     * @param level
     * @param chunkX
     * @param chunkZ
     * @param needInited 是否需要初始化
     * @return
     */
    Chunk getChunk(Level level, int chunkX, int chunkZ, boolean needInited);

    /**
     * 保存并重载区块
     *
     * @param level
     * @param chunkX
     * @param chunkZ
     * @return
     */
    void reloadChunk(Level level, int chunkX, int chunkZ);

    /**
     * 触发保存区块
     *
     * @param level
     * @param chunkX
     * @param chunkZ
     * @return
     */
    boolean saveChunk(Level level, int chunkX, int chunkZ);

    /**
     * 通过玩家/生物的坐标获取所在chunk
     *
     * @param vector3 生物坐标
     * @return
     */
    Chunk indexChunk(Level level, Vector3 vector3);

    /**
     * 通过玩家/生物的坐标获取所在chunk
     *
     * @param vector3f 生物坐标
     * @return
     */
    Chunk indexChunk(Level level, Vector3f vector3f);

    /**
     * 通过玩家/生物的坐标获取所在chunk
     *
     * @param level 生物所在的世界
     * @param x
     * @param z
     * @return
     */
    Chunk indexChunk(Level level, int x, int z);

    /**
     * 启用并刷新Chunk中的数据
     *
     * @param level
     * @param chunk
     * @param chunkData
     * @return
     */
    boolean enableChunk(Level level, Chunk chunk, ChunkData chunkData);

    /**
     * 关闭Chunk并移除该Chunk上的生物
     *
     * @param level
     * @param chunk
     * @return
     */
    boolean blockingChunk(Level level, Chunk chunk);
}
