package com.particle.game.world.level;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.modules.NBTTagCompoundModule;
import com.particle.game.common.modules.TransformModule;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkSection;
import com.particle.model.nbt.NBTTagCompound;

import java.util.LinkedList;

public class ChunkSnapshotService {

    private static final ECSModuleHandler<NBTTagCompoundModule> NBT_TAG_COMPOUND_MODULE_HANDLER = ECSModuleHandler.buildHandler(NBTTagCompoundModule.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    /**
     * 创建Chunk的镜像
     *
     * @param chunk
     * @return
     */
    public static ChunkData createSnapshot(Chunk chunk) {
        ChunkData chunkData = new ChunkData();
        chunkData.setxPos(chunk.getxPos());
        chunkData.setzPos(chunk.getzPos());
        chunkData.setSections(chunk.getChunkSections().clone());
        chunkData.setHeightMap(chunk.getHeightMap());
        chunkData.setBiomColors(chunk.getBiome());
        chunkData.setExtraData(new byte[0]);

        // 目前只保存TileEntity信息
        chunkData.setTileEntities(new LinkedList<>(chunk.getTileEntitiesCollection().getEntitiesViewer()));

        return chunkData;
    }

    /**
     * 基于镜像恢复区块
     *
     * @param level
     * @param chunk
     * @param chunkData
     */
    public static void restoreChunkFromSnapshot(Level level, Chunk chunk, ChunkData chunkData) {
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
        chunk.setHeightMap(chunkData.getHeightMap());
        chunk.setBiome(chunkData.getBiomColors());

        //注册TileEntity
        chunk.getTileEntitiesCollection().registerEntities(chunkData.getTileEntities());
        //初始化数据
        chunk.getTileEntitiesCollection().getEntitiesViewer().forEach(tileEntity -> {
            tileEntity.setLevel(level);

            // 初始化组件
            TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(tileEntity);
            NBTTagCompoundModule nbtTagCompoundModule = NBT_TAG_COMPOUND_MODULE_HANDLER.getModule(tileEntity);
            NBTTagCompound nbtTagCompound = nbtTagCompoundModule.getNbtTagCompound();
            nbtTagCompound.setString("id", tileEntity.getName());
            nbtTagCompound.setInteger("x", transformModule.getFloorX());
            nbtTagCompound.setInteger("y", transformModule.getFloorY());
            nbtTagCompound.setInteger("z", transformModule.getFloorZ());
        });
    }

}
