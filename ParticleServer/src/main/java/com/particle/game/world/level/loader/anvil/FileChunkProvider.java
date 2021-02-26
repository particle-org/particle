package com.particle.game.world.level.loader.anvil;

import com.particle.core.ecs.module.ECSModule;
import com.particle.game.world.level.convert.ChunkData2NBTTagCompound;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.LevelProvider;
import com.particle.model.level.LevelProviderMapper;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.nbt.CompressedStreamTools;
import com.particle.model.nbt.NBTTagCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FileChunkProvider implements LevelProvider, LevelProviderMapper {

    private static Logger logger = LoggerFactory.getLogger(FileChunkProvider.class);

    private FileChunkReader fileChunkReader;

    private ChunkData2NBTTagCompound chunkData2NBTTagCompound;

    public FileChunkProvider(ChunkData2NBTTagCompound chunkData2NBTTagCompound, String levelName) {
        this.fileChunkReader = new FileChunkReader(levelName);
        this.chunkData2NBTTagCompound = chunkData2NBTTagCompound;
    }

    @Override
    public void saveTileEntity(TileEntity tileEntity) {

    }

    @Override
    public void removeTileEntity(TileEntity tileEntity) {

    }

    @Override
    public void loadModule(Chunk chunk, String moduleName) {
        throw new RuntimeException("Not support exception");
    }

    @Override
    public void saveModule(Chunk chunk, ECSModule module) {
        throw new RuntimeException("Not support exception");
    }

    @Override
    public ChunkData loadChunk(int x, int z) {
        DataInputStream chunkInputStream = fileChunkReader.getChunkInputStream(x, z);

        if (chunkInputStream == null) {
            return null;
        }

        //读取nbt信息
        NBTTagCompound nbtTagCompound = null;
        try {
            nbtTagCompound = CompressedStreamTools.read(chunkInputStream);
        } catch (IOException e) {
            logger.error("Exception while load chunk at {} {}", x, z);
            logger.error("Exception: ", e);

            return null;
        } finally {
            try {
                chunkInputStream.close();
            } catch (IOException e) {
                logger.error("Exception: ", e);
            }
        }

        return chunkData2NBTTagCompound.fromNBT(nbtTagCompound);
    }

    @Override
    public boolean isChunkExist(int x, int z) {
        return false;
    }

    @Override
    public void saveChunk(ChunkData chunkData, boolean release) {
        DataOutputStream dataOutputStream = fileChunkReader.getChunkOutputStream(chunkData.getxPos(), chunkData.getzPos());

        if (dataOutputStream == null) {
            return;
        }

        NBTTagCompound nbtTagCompound = chunkData2NBTTagCompound.toNbt(chunkData);

        try {
            CompressedStreamTools.write(nbtTagCompound, dataOutputStream);
        } catch (IOException e) {
            logger.error("Exception while save chunk at {} {}", chunkData.getxPos(), chunkData.getzPos());
            logger.error("Exception: ", e);
        } finally {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                logger.error("Exception: ", e);
            }
        }
    }

    @Override
    public LevelProvider getLevelProvider(int x, int z) {
        return this;
    }
}
