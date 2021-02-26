package com.particle.game.world.level.loader.anvil;

import com.particle.api.level.IFileChunkProviderFactory;
import com.particle.game.world.level.convert.ChunkData2NBTTagCompound;
import com.particle.model.level.LevelProviderMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FileChunkProviderFactory implements IFileChunkProviderFactory {

    @Inject
    private ChunkData2NBTTagCompound chunkData2NBTTagCompound;

    @Override
    public LevelProviderMapper getProvider(String levelName) {
        return new FileChunkProvider(chunkData2NBTTagCompound, levelName);
    }

}
