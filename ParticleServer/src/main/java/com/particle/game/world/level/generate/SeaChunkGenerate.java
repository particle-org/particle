package com.particle.game.world.level.generate;

import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.IChunkGenerate;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkSection;

import java.util.LinkedList;

public class SeaChunkGenerate implements IChunkGenerate {
    private ChunkData chunkData;
    private int seaHeight;

    public SeaChunkGenerate(int seaHeight) {
        this.seaHeight = seaHeight;
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

            // 填充海水
            if (i * 16 < this.seaHeight) {
                for (int y = 0; y < 16 && y + 16 * i < this.seaHeight; y++) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            chunkSection.setBlock(x, y, z, (short) BlockPrototype.WATER.getId());
                        }
                    }
                }
            }

            // 填充地板
            if (i == 0) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        chunkSection.setBlock(x, 0, z, (short) BlockPrototype.BEDROCK.getId());
                        chunkSection.setBlock(x, 1, z, (short) BlockPrototype.SAND.getId());
                    }
                }
            }

            chunkSections[i] = chunkSection;
        }

        chunkData.setSections(chunkSections);

        return chunkData;
    }
}
