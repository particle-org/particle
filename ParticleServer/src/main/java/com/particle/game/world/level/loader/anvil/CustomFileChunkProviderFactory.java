package com.particle.game.world.level.loader.anvil;

import com.particle.api.level.ICustomFileChunkProviderFactory;
import com.particle.game.world.level.convert.ChunkData2NBTTagCompound;
import com.particle.model.level.LevelProviderMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CustomFileChunkProviderFactory implements ICustomFileChunkProviderFactory {

    @Inject
    private ChunkData2NBTTagCompound chunkData2NBTTagCompound;

    @Override
    public LevelProviderMapper getProvider(String location, String levelName) {
        return new CustomFileChunkProvider(chunkData2NBTTagCompound, location, levelName);
    }

}
