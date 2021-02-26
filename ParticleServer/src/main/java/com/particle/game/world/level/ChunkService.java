package com.particle.game.world.level;

import com.particle.api.world.ChunkServiceAPI;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.executor.thread.IScheduleThread;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.common.modules.NBTTagCompoundModule;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.player.inventory.modules.SingleContainerModule;
import com.particle.game.world.aoi.BroadcastService;
import com.particle.game.world.level.convert.ChunkSection2PacketBinary;
import com.particle.game.world.level.loader.ChunkLoadTask;
import com.particle.game.world.level.loader.ChunkLoaderService;
import com.particle.game.world.nav.NavMeshService;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.entity.model.others.NpcEntity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.events.level.world.ChunkSaveEvent;
import com.particle.model.inventory.Inventory;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkSection;
import com.particle.model.level.chunk.ChunkState;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.network.packets.data.FullChunkDataPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ChunkService implements ChunkServiceAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkService.class);

    private static final ECSModuleHandler<NBTTagCompoundModule> NBT_TAG_COMPOUND_MODULE_HANDLER = ECSModuleHandler.buildHandler(NBTTagCompoundModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<SingleContainerModule> SINGLE_CONTAINER_MODULE_HANDLER = ECSModuleHandler.buildHandler(SingleContainerModule.class);

    @Inject
    private TileEntityService tileEntityService;
    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private ChunkLoaderService chunkLoaderService;

    @Inject
    private NavMeshService navMeshService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    /**
     * 目前回调处理器数量较少，并且没有移除的情况，因此用ArrayList，尽可能提高迭代效率
     */
    private List<IChunkLoadCallback> chunkLoadCallback = new ArrayList<>();
    private List<IChunkUnloadCallback> chunkUnloadCallbacks = new ArrayList<>();

    //----------------- Chunk 操作相关 ------------------

    /**
     * 通过chunk的坐标获取chunk
     * 该操作需要线程安全，避免重复加载
     *
     * @param chunkX
     * @param chunkZ
     * @return
     */
    @Override
    public Chunk getChunk(Level level, int chunkX, int chunkZ) {
        return this.getChunk(level, chunkX, chunkZ, true);
    }

    @Override
    public Chunk getChunk(Level level, int chunkX, int chunkZ, boolean needInited) {
        // 查找并加载区块
        long index = this.getChunkIndex(chunkX, chunkZ);

        synchronized (level) {
            Chunk chunk = level.getChunk(index);

            if (chunk != null)
                return chunk;

            if (needInited) {
                // 启动读取任务
                ChunkLoadTask task = this.chunkLoaderService.loadChunk(level, chunkX, chunkZ);
                if (task != null) {
                    level.putChunk(index, task.getChunk());

                    return task.getChunk();
                } else {
                    return null;
                }
            }

            return null;
        }
    }

    @Override
    public void reloadChunk(Level level, int chunkX, int chunkZ) {
        //查找区块
        long index = this.getChunkIndex(chunkX, chunkZ);

        synchronized (level) {
            Chunk chunk = level.removeChunk(index);

            if (chunk == null) {
                return;
            }

            // 保存Chunk
            this.blockingChunk(level, chunk);

            // 重载区块
            ChunkLoadTask task = this.chunkLoaderService.loadChunk(level, chunkX, chunkZ);
            if (task != null) {
                level.putChunk(index, task.getChunk());
            }
        }
    }

    @Override
    public boolean saveChunk(Level level, int chunkX, int chunkZ) {
        //查找区块
        long index = this.getChunkIndex(chunkX, chunkZ);
        synchronized (level) {
            Chunk chunk = level.getChunk(index);

            if (chunk == null) {
                return false;
            }
            // 存储区块
            this.chunkLoaderService.saveChunk(level, chunk, false);
        }
        return true;
    }

    /**
     * 卸载Chunk
     *
     * @param level
     * @param chunkX
     * @param chunkZ
     */
    public void unloadChunk(Level level, int chunkX, int chunkZ) {
        //查找并加载区块
        long index = this.getChunkIndex(chunkX, chunkZ);

        Chunk chunk = level.removeChunk(index);

        if (chunk == null) {
            LOGGER.warn("Fail to unload chunk at {},{}, because chunk unloaded", chunkX, chunkZ);

            return;
        }

        LOGGER.debug("Unload chunk {},{}", chunkX, chunkZ);

        // 把他从条件判断中移动出来，防止插件调用blockChunk时，多个插件调用block，结果返回false.
        this.blockingChunk(level, chunk);
        chunk.updateState(ChunkState.CLOSED);
    }

    /**
     * 更新Chunk中的数据
     *
     * @param level
     * @param chunk
     * @param chunkData
     */
    @Override
    public boolean enableChunk(Level level, Chunk chunk, ChunkData chunkData) {
        synchronized (level) {
            // 确认Chunk没有卸载或替换
            Chunk cachedChunk = level.getChunk(this.getChunkIndex(chunk));
            if (cachedChunk == null) {
                LOGGER.warn("Fail to enable chunk  at {},{} because chunk has been unloaded.", chunk.getxPos(), chunk.getzPos());

                return false;
            }

            if (cachedChunk.getTimestamp() != chunk.getTimestamp()) {
                LOGGER.warn("Fail to enable chunk  at {},{} because chunk has been replaced.", chunk.getxPos(), chunk.getzPos());

                return false;
            }
        }
        // 检查状态
        if (chunk.updateState(ChunkState.RUNNING)) {
            LOGGER.debug("Enable chunk at {},{}", chunk.getxPos(), chunk.getzPos());

            // 加载Chunk中的数据
            ChunkSnapshotService.restoreChunkFromSnapshot(level, chunk, chunkData);

            if (chunkData.getSections() != null) {
                try {
                    this.navMeshService.calculateLayouts(level, chunk);
                } catch (Exception e) {
                    LOGGER.error("Fail to caculate chunk layout at ({},{}).", chunk.getxPos(), chunk.getzPos(), e);
                }

            }

            // 执行回调
            for (IChunkLoadCallback callback : this.chunkLoadCallback) {
                callback.onload(level, chunk);
            }

            // 发送区块数据
            FullChunkDataPacket fullChunkDataPacket = new FullChunkDataPacket();
            fullChunkDataPacket.setChunk(chunk);
            fullChunkDataPacket.setChunkX(chunk.getxPos());
            fullChunkDataPacket.setChunkZ(chunk.getzPos());
            fullChunkDataPacket.setHeightMap(this.getHeightMap(chunk));
            fullChunkDataPacket.setBiomes(this.getBiome(chunk));
            fullChunkDataPacket.setTileEntities(this.getTileEntities(chunk));
            fullChunkDataPacket.setSubChunkCount(chunk.getChunkSections().length);
            fullChunkDataPacket.setCacheEnabled(false);
            BroadcastService.broadcast(level, chunk, fullChunkDataPacket);

            // 加载普通生物
            for (MobEntity mobEntity : chunk.getMobEntitiesCollection().getEntitiesViewer()) {
                this.entitySpawnService.spawn(level, mobEntity);
            }

            // 加载精英怪
            for (MonsterEntity monsterEntity : chunk.getMonsterEntitiesCollection().getEntitiesViewer()) {
                this.entitySpawnService.spawn(level, monsterEntity);
            }

            // 加载NPC
            for (NpcEntity npcEntity : chunk.getNPCCollection().getEntitiesViewer()) {
                this.entitySpawnService.spawn(level, npcEntity);
            }

            for (TileEntity tileEntity : chunk.getTileEntitiesCollection().getEntitiesViewer()) {
                this.entitySpawnService.spawn(level, tileEntity);
            }
            chunk.getTileEntitiesCollection().requestUpdate();

            return true;
        } else {
            LOGGER.warn("Fail to enable chunk at {},{}", chunk.getxPos(), chunk.getzPos());

            return false;
        }
    }

    /**
     * 关闭Chunk，并清空Chunk中的数据
     *
     * @param chunk
     */
    @Override
    public boolean blockingChunk(Level level, Chunk chunk) {
        return this.blockingChunk(level, chunk, null, null);
    }

    public boolean blockingChunk(Level level, Chunk chunk, Runnable callback, IScheduleThread callbackThread) {
        if (chunk.updateState(ChunkState.BLOCKING)) {
            LOGGER.debug("Blocking chunk {},{}", chunk.getxPos(), chunk.getzPos());

            // 回调处理器
            for (IChunkUnloadCallback chunkUnloadCallback : this.chunkUnloadCallbacks) {
                chunkUnloadCallback.onunload(level, chunk);
            }

            // 卸载Chunk中的Entity
            chunk.getMobEntitiesCollection().getEntitiesViewer().forEach(this.entitySpawnService::despawn);
            chunk.getMobEntitiesCollection().syncEntityViewer();

            // monster卸载
            chunk.getMonsterEntitiesCollection().getEntitiesViewer().forEach(this.entitySpawnService::despawn);
            chunk.getMonsterEntitiesCollection().syncEntityViewer();

            chunk.getItemEntitiesCollection().getEntitiesViewer().forEach(this.entitySpawnService::despawn);
            chunk.getItemEntitiesCollection().syncEntityViewer();
            chunk.getNPCCollection().getEntitiesViewer().forEach(this.entitySpawnService::despawn);
            chunk.getNPCCollection().syncEntityViewer();
            chunk.getProjectileEntitiesCollection().getEntitiesViewer().forEach(this.entitySpawnService::despawn);
            chunk.getProjectileEntitiesCollection().syncEntityViewer();

            // TileEntity标记
            chunk.getTileEntitiesCollection().getEntitiesViewer().forEach((tileEntity -> {
                SingleContainerModule singleContainerModule = SINGLE_CONTAINER_MODULE_HANDLER.getModule(tileEntity);
                if (singleContainerModule != null) {
                    Inventory inventory = singleContainerModule.getInventory();
                    if (inventory != null) {
                        inventory.setActive(false);
                    }
                }
            }));
            chunk.getTileEntitiesCollection().syncEntityViewer();

            // 清理NavSquare
            this.navMeshService.removeNavsquares(chunk);

            // 区块保存任务
            ChunkSaveEvent chunkSaveEvent = new ChunkSaveEvent(level, chunk);
            this.eventDispatcher.dispatchEvent(chunkSaveEvent);

            // 存储区块,无视cancel情况
            this.chunkLoaderService.saveChunk(level, chunk, true, callback, callbackThread);

            // 重置Section
            chunk.setChunkSections(null);

            return true;
        }

        return false;
    }

    /**
     * 通过玩家/生物的坐标获取所在chunk
     *
     * @param vector3
     * @return
     */
    @Override
    public Chunk indexChunk(Level level, Vector3 vector3) {
        return this.indexChunk(level, vector3.getX(), vector3.getZ());
    }

    /**
     * 通过玩家/生物的坐标获取所在chunk
     *
     * @param level
     * @param vector3f
     * @return
     */
    @Override
    public Chunk indexChunk(Level level, Vector3f vector3f) {
        return this.indexChunk(level, vector3f.getFloorX(), vector3f.getFloorZ());
    }

    /**
     * 通过玩家/生物的坐标获取所在chunk
     *
     * @param level
     * @param x
     * @param z
     * @return
     */
    @Override
    public Chunk indexChunk(Level level, int x, int z) {
        return this.getChunk(level, x >> 4, z >> 4, false);
    }

    /**
     * 查询Chunk的HashIndex
     *
     * @param chunk
     * @return
     */
    public long getChunkIndex(Chunk chunk) {
        return (((long) chunk.getxPos()) << 32) | (((long) chunk.getzPos()) & 0xFFFFFFFFL);
    }

    public int getChunkXPosFromIndex(long index) {
        return (int) (index >> 32);
    }

    public int getChunkZPosFromIndex(long index) {
        return (int) (index & 0xFFFFFFFFL);
    }

    /**
     * 通过玩家/生物的坐标获取所在chunk
     *
     * @param x
     * @param z
     * @returnreload
     */
    public long getChunkIndex(int x, int z) {
        return (((long) x) << 32) | (z & 0xFFFFFFFFL);
    }

    //-------------------------- 接口处理 ------------------------------------------
    public void registerChunkLoadCallback(IChunkLoadCallback callback) {
        this.chunkLoadCallback.add(callback);
    }

    public void registerChunkUnloadCallback(IChunkUnloadCallback callback) {
        this.chunkUnloadCallbacks.add(callback);
    }

    //-------------------------- 数据导出 ------------------------------------------
    public List<byte[]> getChunkSectionPacketData(Chunk chunk) {
        List<byte[]> sectionCache = new ArrayList<>(16);

        if (chunk.getChunkSections() != null) {
            for (ChunkSection chunkSection : chunk.getChunkSections()) {
                byte[] encodeCache = null;
                if (chunkSection != null) {
                    encodeCache = chunkSection.getEncodeCache();
                }

                if (encodeCache == null) {
                    ByteBuf byteBuf = Unpooled.buffer();
                    byteBuf.writeByte(1);
                    byteBuf.writeBytes(ChunkSection2PacketBinary.getPacketFormatBytesV2(chunkSection));

                    encodeCache = new byte[byteBuf.writerIndex() - byteBuf.readerIndex()];
                    byteBuf.readBytes(encodeCache);

                    if (chunkSection != null) {
                        chunkSection.setEncodeCache(encodeCache);
                    }
                }

                sectionCache.add(encodeCache);
            }
        }

        return sectionCache;
    }

    public byte[] getHeightMap(Chunk chunk) {
        return chunk.getHeightMap();
    }

    public byte[] getBiome(Chunk chunk) {
        return chunk.getBiome();
    }

    public List<NBTTagCompound> getTileEntities(Chunk chunk) {
        List<NBTTagCompound> sectionCache = new ArrayList<>();

        chunk.getTileEntitiesCollection().getEntitiesViewer().forEach(tileEntity -> {
            if (tileEntity.needNoticeClient())
                sectionCache.add(this.tileEntityService.getViewNbtCompound(tileEntity));
        });

        return sectionCache;
    }

    public void loadChunkData(ChunkData chunkData, Chunk chunk, Level level) {
        if (level.getLevelSettings().isReadOnly()) {
            ChunkSection[] sections = chunkData.getSections();

            ChunkSection[] sectionsClone = new ChunkSection[sections.length];

            for (int i = 0; i < sections.length; i++) {
                sectionsClone[i] = sections[i].clone();
            }

            chunk.setChunkSections(sectionsClone);
        } else {
            chunk.setChunkSections(chunkData.getSections());
        }

        chunk.setBiome(chunkData.getBiomColors());
        chunk.setHeightMap(chunkData.getHeightMap());

        //注册TileEntity
        chunk.getTileEntitiesCollection().registerEntities(chunkData.getTileEntities());
        //初始化数据
        chunk.getTileEntitiesCollection().getEntitiesViewer().forEach(tileEntity -> {
            tileEntity.setLevel(level);

            TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(tileEntity);

            Vector3 position = new Vector3(transformModule.getPosition());

            NBTTagCompoundModule nbtTagCompoundModule = NBT_TAG_COMPOUND_MODULE_HANDLER.getModule(tileEntity);
            NBTTagCompound nbtTagCompound = nbtTagCompoundModule.getNbtTagCompound();
            nbtTagCompound.setString("id", tileEntity.getName());
            nbtTagCompound.setInteger("x", position.getX());
            nbtTagCompound.setInteger("y", position.getY());
            nbtTagCompound.setInteger("z", position.getZ());
        });

        // 注册npc
        for (NpcEntity npcEntity : chunkData.getNpcEntities()) {
            npcEntity.setLevel(level);
        }
        chunk.getNPCCollection().registerEntities(chunkData.getNpcEntities());
    }
}
