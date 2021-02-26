package com.particle.game.world.level.generate;

import com.particle.model.level.IChunkGenerate;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkSection;

import java.util.LinkedList;

public class FloatChunkGenerate implements IChunkGenerate {
    private ChunkData chunkData;

    public FloatChunkGenerate() {
        this.chunkData = buildChunkData();
    }

    @Override
    public ChunkData getEmptyChunk(int xPos, int zPos) {
        ChunkData chunkData = this.chunkData.clone();

        chunkData.setxPos(xPos);
        chunkData.setzPos(zPos);

        return chunkData;
    }

    private ChunkData buildChunkData() {
        ChunkData chunkData = new ChunkData();
        chunkData.setxPos(0);
        chunkData.setzPos(0);
        chunkData.setBiomColors(new byte[256]);
        chunkData.setHeightMap(new byte[256]);
        chunkData.setMobEntities(new LinkedList<>());
        chunkData.setItemEntities(new LinkedList<>());
        chunkData.setTileEntities(new LinkedList<>());
        chunkData.setExtraData(new byte[0]);

        ChunkSection[] chunkSections = new ChunkSection[16];
        for (byte i = 0; i < 16; i++) {
            ChunkSection chunkSection = new ChunkSection(i);

            if (i == 0) {
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 3; y++) {
                        for (int z = 0; z < 16; z++) {
                            chunkSection.setBlock(x, y, z, (short) 1);
                        }
                    }
                }
            }

            chunkSections[i] = chunkSection;
        }

        chunkData.setSections(chunkSections);

        return chunkData;
    }
}
