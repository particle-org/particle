package com.particle.game.world.level.generate;

import com.particle.game.world.level.convert.ChunkSection2NBTTagCompound;
import com.particle.game.world.level.loader.anvil.RegionFile;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.IChunkGenerate;
import com.particle.model.level.chunk.ChunkData;
import com.particle.model.level.chunk.ChunkSection;
import com.particle.model.nbt.CompressedStreamTools;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTTagList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CustomFileChunkGenerate implements IChunkGenerate {
    private static Logger logger = LoggerFactory.getLogger(CustomFileChunkGenerate.class);

    private ChunkData chunkData;
    private File dir;

    private Map<Long, RegionFile> regionFileMap = new HashMap<>();

    public CustomFileChunkGenerate(String fileLocation) {
        this.chunkData = buildGrassChunkData();
        this.dir = new File(fileLocation);
    }

    @Override
    public ChunkData getEmptyChunk(int xPos, int zPos) {
        ChunkData chunkData = buildChunkData(xPos, zPos);

        if (chunkData == null) {
            chunkData = this.chunkData.clone();
        }

        chunkData.setxPos(xPos);
        chunkData.setzPos(zPos);

        return chunkData;
    }

    private ChunkData buildChunkData(int x, int z) {
        DataInputStream chunkInputStream = getChunkInputStream(x, z);

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

        return fromNBT(nbtTagCompound);
    }

    public ChunkData fromNBT(NBTTagCompound chunkTag) {
        ChunkData chunkData = new ChunkData();

        NBTTagCompound level = chunkTag.getCompoundTag("Level");

        chunkData.setxPos(level.getInteger("xPos"));
        chunkData.setzPos(level.getInteger("zPos"));

        //填充biome
        byte[] biomeColors = new byte[256];
        chunkData.setBiomColors(biomeColors);

        int[] biomeColorsArray = level.getIntArray("BiomeColors");
        if (biomeColorsArray != null) {
            for (int i = 0; i < biomeColorsArray.length; i++) {
                biomeColors[i] = (byte) (biomeColorsArray[i] & 0xff);
            }
        }

        //填充HeightMap
        byte[] heightMap = new byte[256];
        chunkData.setHeightMap(heightMap);

        int[] heightMapArray = level.getIntArray("HeightMap");
        if (heightMapArray != null) {
            for (int i = 0; i < heightMapArray.length; i++) {
                heightMap[i] = (byte) (heightMapArray[i] & 0xff);
            }
        }

        //填充Sections
        ChunkSection[] chunkSections = new ChunkSection[16];
        chunkData.setSections(chunkSections);

        NBTTagList sectionLists = level.getTagList("Sections", 10);
        if (sectionLists != null) {
            for (int i = 0; i < sectionLists.tagCount(); i++) {
                ChunkSection chunkSection = ChunkSection2NBTTagCompound.fromNBT(sectionLists.getCompoundTagAt(i));

                chunkSections[chunkSection.getY()] = chunkSection;
            }
        }
        for (byte i = 0; i < 16; i++) {
            if (chunkSections[i] == null) {
                chunkSections[i] = new ChunkSection(i);
            }
        }


        //填充ExtraData
        chunkData.setExtraData(level.getByteArray("ExtraData"));

        //填充ItemEntities
        List<ItemEntity> itemEntitiesList = new LinkedList<>();
        chunkData.setItemEntities(itemEntitiesList);

        //填充Entities
        List<MobEntity> entitiesList = new LinkedList<>();
        chunkData.setMobEntities(entitiesList);

        //填充TileEntities
        List<TileEntity> tileEntitiesList = new LinkedList<>();
        chunkData.setTileEntities(tileEntitiesList);

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

    private ChunkData buildGrassChunkData() {
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
            if (i * 16 < 64) {
                for (int y = 0; y < 16 && y + 16 * i < 64; y++) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            chunkSection.setBlock(x, y, z, (short) BlockPrototype.DIRT.getId());
                        }
                    }
                }
            }

            // 填充草地
            if (i * 16 == 64) {
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

    public DataInputStream getChunkInputStream(int x, int z) {
        int chunkX = x % 32;
        if (chunkX < 0) chunkX = chunkX + 32;
        int chunkZ = z % 32;
        if (chunkZ < 0) chunkZ = chunkZ + 32;

        RegionFile regionFile = this.getRegionFile(x, z);

        synchronized (regionFile) {
            if (regionFile.chunkExists(chunkX, chunkZ)) {
                return regionFile.getChunkDataInputStream(chunkX, chunkZ);
            }

            return null;
        }
    }

    private RegionFile getRegionFile(int x, int z) {
        int reginX = x / 32;
        int reginZ = z / 32;

        if (x < 0)
            reginX--;

        if (z < 0)
            reginZ--;

        RegionFile regionFile = null;

        synchronized (this) {
            long index = hashIndex(reginX, reginZ);

            if (regionFileMap.containsKey(index)) {
                regionFile = regionFileMap.get(index);
            } else {
                regionFile = new RegionFile(new File(dir, "r." + reginX + "." + reginZ + ".mca"));

                this.regionFileMap.put(index, regionFile);
            }
        }

        return regionFile;
    }

    protected long hashIndex(int x, int z) {
        return (((long) x) << 32) | (z & 0xffffffffL);
    }
}
