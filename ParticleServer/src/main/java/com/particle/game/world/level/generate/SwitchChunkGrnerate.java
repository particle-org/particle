package com.particle.game.world.level.generate;

import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.IChunkGenerate;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkSection;

import java.util.LinkedList;

public class SwitchChunkGrnerate implements IChunkGenerate {

    /**
     * 中转区块的x位置
     */
    public static final int chunkX = -200;

    /**
     * 中转区块的z位置
     */
    public static final int chunkZ = -200;

    /**
     * x坐标
     */
    public static final int coorX = -3195;
    /**
     * y坐标
     */
    public static final int coorY = 4;
    /**
     * z坐标
     */
    public static final int coorZ = -3195;


    private ChunkData chunkData;

    public SwitchChunkGrnerate() {
        this.chunkData = new ChunkData();
        this.chunkData.setxPos(0);
        this.chunkData.setzPos(0);
        this.chunkData.setBiomColors(new byte[256]);
        this.chunkData.setHeightMap(new byte[256]);
        this.chunkData.setMobEntities(new LinkedList<>());
        this.chunkData.setItemEntities(new LinkedList<>());
        this.chunkData.setTileEntities(new LinkedList<>());
        this.chunkData.setExtraData(new byte[0]);

        ChunkSection[] chunkSections = new ChunkSection[16];
        for (byte i = 0; i < 16; i++) {
            ChunkSection chunkSection = new ChunkSection(i);

            if (i == 0) {
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 3; y++) {
                        for (int z = 0; z < 16; z++) {
                            chunkSection.setBlock(x, y, z, BlockPrototype.GLASS.getId());
                        }
                    }
                }
            }

            chunkSections[i] = chunkSection;
        }

        this.chunkData.setSections(chunkSections);
    }

    @Override
    public ChunkData getEmptyChunk(int xPos, int zPos) {
        this.chunkData.setxPos(xPos);
        this.chunkData.setzPos(zPos);

        return this.chunkData;
    }
}
