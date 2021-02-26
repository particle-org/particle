package com.particle.game.world.level.generate;

import com.particle.api.level.IChunkGenerateFactory;
import com.particle.model.level.IChunkGenerate;

import javax.inject.Singleton;

@Singleton
public class ChunkGenerateFactory implements IChunkGenerateFactory {

    private IChunkGenerate floatChunkGenerate = new FloatChunkGenerate();

    private IChunkGenerate seaChunkGenerate = new SeaChunkGenerate(64);

    private IChunkGenerate customFileChunkGenerate = new CustomFileChunkGenerate("worlds/default");

    private IChunkGenerate grassChunkGenerate = new GrassChunkGenerate(64);

    @Override
    public IChunkGenerate getFloatChunkGenerate() {
        return floatChunkGenerate;
    }

    @Override
    public IChunkGenerate getSeaChunkGenerate() {
        return seaChunkGenerate;
    }

    @Override
    public IChunkGenerate getCustomFileChunkGenerate() {
        return customFileChunkGenerate;
    }

    @Override
    public IChunkGenerate getGrassChunkGenerate() {
        return grassChunkGenerate;
    }
}
