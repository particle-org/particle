package com.particle.game.world.level;

import com.particle.api.world.LevelServiceAPI;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.executor.service.LevelScheduleService;
import com.particle.game.block.interactor.BlockWorldService;
import com.particle.game.player.PlayerService;
import com.particle.game.server.Server;
import com.particle.game.world.aoi.BroadcastService;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.game.world.level.generate.SwitchChunkGrnerate;
import com.particle.model.block.Block;
import com.particle.model.block.geometry.BlockGeometry;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.TimeCycle;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkState;
import com.particle.model.level.settings.Capacity;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.FullChunkDataPacket;
import com.particle.model.network.packets.data.SetTimePacket;
import com.particle.model.network.packets.data.UpdateBlockPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
public class LevelService implements LevelServiceAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelService.class);

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Inject
    private ChunkService chunkService;

    @Inject
    private PlayerService playerService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private NetworkManager networkManager;

    @Inject
    private Server server;


    //------------------ 方块操作相关 -----------------------
    @Inject
    private BlockWorldService blockWorldService;

    /**
     * 获取生物数量
     *
     * @param level
     * @return
     */
    @Override
    public int getEntityCount(Level level) {
        return level.getEntityCount();
    }


    /**
     * 放置方块
     *
     * @param level
     * @param block
     * @param vector3
     */
    @Override
    public boolean setBlockAt(Level level, Block block, Vector3 vector3) {
        Chunk chunk = this.chunkService.indexChunk(level, vector3);

        if (chunk == null || chunk.getState() != ChunkState.RUNNING) {
            return false;
        }

        //更新Level数据
        chunk.setBlockAt(block, vector3.getX(), vector3.getY(), vector3.getZ());

        LOGGER.debug("Block update at {},{},{} to {}:{} in {}", vector3.getX(), vector3.getY(), vector3.getZ(), block.getType().getName(), block.getMeta(), level.getLevelName());

        //广播通知数据
        UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
        updateBlockPacket.setBlock(block);
        updateBlockPacket.setVector3(vector3);
        updateBlockPacket.setRuntimeId(block.getRuntimeId());
        updateBlockPacket.setLayer(0);
        updateBlockPacket.setFlag(UpdateBlockPacket.All);
        BroadcastService.broadcast(level, chunk, updateBlockPacket);

        return true;
    }

    public boolean refreshBlockAt(Level level, Player player, Vector3 vector3) {
        Chunk chunk = this.chunkService.indexChunk(level, vector3);

        if (chunk == null || chunk.getState() != ChunkState.RUNNING) {
            return false;
        }

        //更新Level数据
        Block block = chunk.getBlockAt(vector3.getX(), vector3.getY(), vector3.getZ());

        //广播通知数据
        UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
        updateBlockPacket.setBlock(block);
        updateBlockPacket.setVector3(vector3);
        updateBlockPacket.setRuntimeId(block.getRuntimeId());
        updateBlockPacket.setLayer(0);
        updateBlockPacket.setFlag(UpdateBlockPacket.Clients);
        networkManager.sendMessage(player.getClientAddress(), updateBlockPacket);

        return true;
    }

    /**
     * 发送一个假方块
     *
     * @param level
     * @param block
     * @param vector3
     * @return
     */
    public boolean setFakeBlockAt(Level level, Block block, Vector3 vector3) {
        Chunk chunk = this.chunkService.indexChunk(level, vector3);

        if (chunk != null) {
            //广播通知数据
            UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
            updateBlockPacket.setBlock(block);
            updateBlockPacket.setVector3(vector3);
            updateBlockPacket.setRuntimeId(block.getRuntimeId());
            updateBlockPacket.setLayer(0);
            updateBlockPacket.setFlag(UpdateBlockPacket.All);
            BroadcastService.broadcast(level, chunk, updateBlockPacket);
        }


        return true;
    }

    /**
     * 通过Level获取方块
     *
     * @param level
     * @param vector3
     * @return
     */
    @Override
    public Block getBlockAt(Level level, Vector3 vector3) {
        if (vector3.getY() < 0 || vector3.getY() > 255) {
            return Block.getBlock(BlockPrototype.AIR);
        }

        return this.getBlockAt(this.chunkService.indexChunk(level, vector3), vector3.getX() & 15, vector3.getY(), vector3.getZ() & 15);
    }

    @Override
    public BlockPrototype getBlockTypeAt(Level level, Vector3 vector3) {
        if (vector3.getY() < 0 || vector3.getY() > 255) {
            return BlockPrototype.AIR;
        }

        return this.getBlockTypeAt(this.chunkService.indexChunk(level, vector3), vector3.getX() & 15, vector3.getY(), vector3.getZ() & 15);
    }

    /**
     * 通过Level获取方块
     *
     * @param level
     * @param x     基于Level的x坐标
     * @param y     基于Level的y坐标
     * @param z     基于Level的z坐标
     * @return
     */
    @Override
    public Block getBlockAt(Level level, int x, int y, int z) {
        return this.getBlockAt(this.chunkService.indexChunk(level, x, z), x & 15, y, z & 15);
    }

    @Override
    public BlockPrototype getBlockTypeAt(Level level, int x, int y, int z) {
        return this.getBlockTypeAt(this.chunkService.indexChunk(level, x, z), x & 15, y, z & 15);
    }

    /**
     * 通过Level获取方块亮度
     *
     * @param level
     * @param x     基于Level的x坐标
     * @param y     基于Level的y坐标
     * @param z     基于Level的z坐标
     * @return 方块亮度
     */
    @Override
    public int getBlockLightAt(Level level, int x, int y, int z) {
        return 15;
    }


    public byte getBiomeAt(Level level, int x, int z) {
        return this.getBiomeAt(this.chunkService.indexChunk(level, x, z), x & 15, z & 15);
    }

    public byte getBiomeAt(Chunk chunk, int chunkOffsetX, int chunkOffsetZ) {
        if (chunk == null) return 0;

        byte[] biome = chunk.getBiome();
        if (biome != null) {
            return chunk.getBiome()[(chunkOffsetZ << 4) | chunkOffsetX];
        }
        return 1;
    }

    /**
     * 通过Chunk获取方块
     *
     * @param chunk
     * @param x     基于Chunk的x坐标
     * @param y     基于Chunk的y坐标
     * @param z     基于Chunk的z坐标
     * @return
     */
    public Block getBlockAt(Chunk chunk, int x, int y, int z) {
        if (chunk == null) {
            return Block.getBlock(BlockPrototype.AIR);
        }

        return chunk.getBlockAt(x, y, z);
    }

    public BlockPrototype getBlockTypeAt(Chunk chunk, int x, int y, int z) {
        if (chunk == null) {
            return BlockPrototype.AIR;
        }

        return chunk.getBlockTypeAt(x, y, z);
    }

    //-------------------------------- 高级方块获取方法 ----------------------------------

    /**
     * 通过Level获取区间内最高的方块
     *
     * @param level
     * @param vector3
     * @return
     */
    @Override
    public int getTopBlockHeightBelow(Level level, Vector3 vector3) {
        Chunk chunk = this.chunkService.indexChunk(level, vector3);

        return chunk.getTopBlockHeightBelow(vector3.getX() & 15, vector3.getY(), vector3.getZ() & 15);
    }

    /**
     * 通过Level获取区间内最高可站位置
     * 预留返回类型为float，为以后更细致的方块模型做准备
     *
     * @param level
     * @param vector3
     * @return
     */
    @Override
    public float getTopCanPassHeightBelow(Level level, Vector3 vector3) {
        Chunk chunk = this.chunkService.indexChunk(level, vector3);

        if (chunk != null && chunk.isRunning()) {
            int x = vector3.getX() & 15;
            int z = vector3.getZ() & 15;

            int index = chunk.getTopBlockHeightBelow(x, vector3.getY(), z);

            //往下寻找非EMPTY的方块
            while (index > 0 && this.getBlockTypeAt(chunk, x, index, z).getBlockGeometry().canPassThrow()) {
                index = index - 1;
            }

            return index;
        } else {
            return 0;
        }
    }

    /**
     * 获取安全spawn地点
     *
     * @param level
     * @param vector3f
     * @return
     */
    public float getSafePositionAbove(Level level, Vector3f vector3f) {
        Chunk chunk = this.chunkService.indexChunk(level, vector3f);

        if (chunk == null) {
            LOGGER.error(String.format("safe chunk is null. level : %s , location : (%s, %s, %s)", level.getLevelName(), vector3f.getX(), vector3f.getY(), vector3f.getZ()));
            return 255;
        }

        if (chunk.isRunning()) {
            int x = vector3f.getFloorX() & 15;
            int z = vector3f.getFloorZ() & 15;

            int y = vector3f.getFloorY();

            while (y < 255) {
                // 玩家刷在空中，则往地下挪
                if (this.getBlockTypeAt(chunk, x, y - 1, z).getBlockGeometry().canPassThrow()) {
                    BlockGeometry body1 = this.getBlockTypeAt(chunk, x, y, z).getBlockGeometry();
                    BlockGeometry body2 = this.getBlockTypeAt(chunk, x, y + 1, z).getBlockGeometry();

                    if (body1.canPassThrow() && body2.canPassThrow()) {
                        y--;
                        continue;
                    }
                }

                // 合法位置，返回
                if (!this.getBlockTypeAt(chunk, x, y - 1, z).getBlockGeometry().canPassThrow()) {
                    if (this.getBlockTypeAt(chunk, x, y, z).getBlockGeometry().canPassThrow() && this.getBlockTypeAt(chunk, x, y + 1, z).getBlockGeometry().canPassThrow()) {
                        return y;
                    }
                }

                // 玩家刷在方块中，往天上差
                y++;
            }
        }

        return 255;
    }

    /**
     * 当前是否白天
     *
     * @param level
     * @return
     */
    @Override
    public boolean isDay(Level level) {
        long time = level.getTime() % 24000;
        if (time >= TimeCycle.DAY_START.getTick() && time <= TimeCycle.DAY_OVER.getTick()) {
            return true;
        }
        return false;
    }

    /**
     * 当前是否黑夜
     *
     * @param level
     * @return
     */
    @Override
    public boolean isNight(Level level) {
        long time = level.getTime();
        if (time >= TimeCycle.MOON_APPEAR.getTick() && time <= TimeCycle.MOON_DISAPPEAR.getTick()) {
            return true;
        }
        return false;
    }

    /**
     * 给Level中所有玩家同步时间
     *
     * @param level
     */
    public void syncTime(Level level) {
        if (level.getPlayers().size() == 0) {
            return;
        }

        SetTimePacket setTimePacket = new SetTimePacket();
        setTimePacket.setTime((int) level.getTime());

        ArrayList<InetSocketAddress> playerAddress = new ArrayList<>();
        for (Map.Entry<String, Player> playerEntry : level.getPlayers().entrySet()) {
            playerAddress.add(playerEntry.getValue().getClientAddress());
        }

        this.networkManager.broadcastMessage(playerAddress, setTimePacket);
    }

    /**
     * T掉所有的玩家
     * <p>
     * 这里可以修改成不T玩家，但是必须做玩家离开世界的后续处理方案，否则不如直接T掉
     *
     * @param level
     * @return
     */
    public boolean kickPlayerInLevel(Level level) {
        for (Map.Entry<String, Player> playerDatas : level.getPlayers().entrySet()) {
            // T掉所有的玩家
            this.server.close(playerDatas.getValue(), "LevelClosed");
        }

        return true;
    }

    /**
     * 获取该世界中的所有玩家
     *
     * @param level
     * @return
     */
    @Override
    @Deprecated
    public Set<Player> getPlayers(Level level) {
        Set<Player> players = new HashSet<>();
        for (Chunk chunk : level.getChunks()) {
            players.addAll(chunk.getPlayersCollection().getEntitiesViewer());
        }
        return players;
    }

    /**
     * 获取世界的人数容量
     *
     * @param level
     * @return
     */
    @Override
    public Capacity getCapacity(Level level) {
        return level.getCapacity();
    }

    /**
     * 关闭Schedule
     *
     * @param level
     */
    public void shutdownSchedule(Level level) {
        LevelScheduleService.getInstance().shutdown(level.getLevelSchedule());
    }

    /**
     * 发送切换中转地的区块
     *
     * @param level
     * @param player
     */
    public void sendSwitchChunkDataPacket(Level level, Player player) {
        FullChunkDataPacket fullChunkDataPacket = new FullChunkDataPacket();
        fullChunkDataPacket.setChunkX(SwitchChunkGrnerate.chunkX);
        fullChunkDataPacket.setChunkZ(SwitchChunkGrnerate.chunkZ);
        SwitchChunkGrnerate switchChunkGrnerate = new SwitchChunkGrnerate();
        ChunkData switchData = switchChunkGrnerate.getEmptyChunk(SwitchChunkGrnerate.chunkX, SwitchChunkGrnerate.chunkZ);
        Chunk chunk = new Chunk(null, level.getLevelProviderMapper().getLevelProvider(0, 0), SwitchChunkGrnerate.chunkX, SwitchChunkGrnerate.chunkZ);
        ChunkSnapshotService.restoreChunkFromSnapshot(level, chunk, switchData);
        fullChunkDataPacket.setChunk(chunk);

        // 發包即時處理
        fullChunkDataPacket.setHeightMap(this.chunkService.getHeightMap(chunk));
        fullChunkDataPacket.setBiomes(this.chunkService.getBiome(chunk));
        fullChunkDataPacket.setTileEntities(this.chunkService.getTileEntities(chunk));
        fullChunkDataPacket.setSubChunkCount(chunk.getChunkSections().length);
        fullChunkDataPacket.setCacheEnabled(false);
        this.networkManager.sendMessage(player.getClientAddress(), fullChunkDataPacket);
    }

}
