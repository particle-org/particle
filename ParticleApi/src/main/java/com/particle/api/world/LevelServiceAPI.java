package com.particle.api.world;

import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.settings.Capacity;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import java.util.Set;

public interface LevelServiceAPI {

    /**
     * 获取生物数量统计
     *
     * @param level
     * @return
     */
    int getEntityCount(Level level);

    /**
     * 设置指定位置的Block
     *
     * @param level   操作的世界
     * @param block   放置的方块
     * @param vector3 放置的坐标
     * @return
     */
    boolean setBlockAt(Level level, Block block, Vector3 vector3);

    boolean setFakeBlockAt(Level level, Block block, Vector3 vector3);

    /**
     * 通过Level获取方块
     *
     * @param level   操作的世界
     * @param vector3 查询的坐标
     * @return 方块数据
     */
    Block getBlockAt(Level level, Vector3 vector3);

    BlockPrototype getBlockTypeAt(Level level, Vector3 vector3);

    /**
     * 通过Level获取方块
     *
     * @param level 操作的世界
     * @param x     基于Level的x坐标
     * @param y     基于Level的y坐标
     * @param z     基于Level的z坐标
     * @return 方块数据
     */
    Block getBlockAt(Level level, int x, int y, int z);

    BlockPrototype getBlockTypeAt(Level level, int x, int y, int z);

    /**
     * 通过Level获取方块亮度
     *
     * @param level
     * @param x     基于Level的x坐标
     * @param y     基于Level的y坐标
     * @param z     基于Level的z坐标
     * @return 方块亮度
     */
    int getBlockLightAt(Level level, int x, int y, int z);

    /**
     * 通过Level获取区间内最高的方块
     *
     * @param level   操作的世界
     * @param vector3 查询指定位置下方最大的方块
     * @return 高度
     */
    int getTopBlockHeightBelow(Level level, Vector3 vector3);

    /**
     * 通过Level获取区间内最高可站位置
     * 预留返回类型为float，为以后更细致的方块模型做准备
     *
     * @param level   操作的世界
     * @param vector3 位置
     * @return 高度
     */
    float getTopCanPassHeightBelow(Level level, Vector3 vector3);

    /**
     * 查询Biome
     *
     * @param level
     * @param x
     * @param z
     * @return
     */
    byte getBiomeAt(Level level, int x, int z);

    /**
     * 查询Biome
     *
     * @param chunk
     * @param chunkOffsetX
     * @param chunkOffsetZ
     * @return
     */
    byte getBiomeAt(Chunk chunk, int chunkOffsetX, int chunkOffsetZ);

    /**
     * 当前是否白天
     *
     * @param level 操作的世界
     * @return 当前是否白天
     */
    boolean isDay(Level level);

    /**
     * 当前是否黑夜
     *
     * @param level 操作的世界
     * @return 当前是否黑夜
     */
    boolean isNight(Level level);

    /**
     * 获取世界的人数容量
     *
     * @param level 操作的世界
     * @return 人数容量
     */
    Capacity getCapacity(Level level);

    /**
     * 获取该世界中的所有玩家
     *
     * @param level
     * @return
     */
    Set<Player> getPlayers(Level level);
}
