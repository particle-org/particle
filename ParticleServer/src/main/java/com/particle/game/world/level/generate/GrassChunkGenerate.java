package com.particle.game.world.level.generate;

import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.IChunkGenerate;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkSection;

import java.util.LinkedList;

public class GrassChunkGenerate implements IChunkGenerate {
    private ChunkData chunkData;
    private int dirtHeight;

    public GrassChunkGenerate(int dirtHeight) {
        this.dirtHeight = dirtHeight;
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

            // 填充泥土
            if (i * 16 < this.dirtHeight) {
                for (int y = 0; y < 16 && y + 16 * i < this.dirtHeight; y++) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            chunkSection.setBlock(x, y, z, (short) BlockPrototype.DIRT.getId());
                        }
                    }
                }
            }

            // 填充草地
            if (i * 16 == dirtHeight) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        chunkSection.setBlock(x, 0, z, (short) BlockPrototype.GRASS.getId());
                    }
                }
            }

            chunkSections[i] = chunkSection;
        }

        chunkData.setSections(chunkSections);

        return chunkData;
    }
}
