package com.particle.game.world.level.loader.empty;

import com.particle.core.ecs.module.ECSModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.Chunk;
import com.particle.model.level.LevelProvider;
import com.particle.model.level.chunk.ChunkData;

import javax.inject.Singleton;

@Singleton
public class EmptyChunkProvider implements LevelProvider {
    @Override
    public void saveTileEntity(TileEntity tileEntity) {
        return;
    }

    @Override
    public void removeTileEntity(TileEntity tileEntity) {

    }

    @Override
    public void loadModule(Chunk chunk, String moduleName) {
        return;
    }

    @Override
    public void saveModule(Chunk chunk, ECSModule module) {
        return;
    }

    @Override
    public ChunkData loadChunk(int x, int z) {
        return null;
    }

    @Override
    public boolean isChunkExist(int x, int z) {
        return false;
    }

    @Override
    public void saveChunk(ChunkData chunkData, boolean release) {

    }
}
