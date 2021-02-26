package com.particle.api.level;

import com.particle.model.level.IChunkGenerate;

public interface IChunkGenerateFactory {
    IChunkGenerate getFloatChunkGenerate();

    IChunkGenerate getSeaChunkGenerate();

    IChunkGenerate getCustomFileChunkGenerate();

    IChunkGenerate getGrassChunkGenerate();
}
