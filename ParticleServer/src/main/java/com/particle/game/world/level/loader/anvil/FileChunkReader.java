package com.particle.game.world.level.loader.anvil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileChunkReader {
    private File dir;

    private Map<Long, RegionFile> regionFileMap = new HashMap<>();

    public FileChunkReader(String level) {
        this.dir = new File("worlds/" + level);
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

    public DataOutputStream getChunkOutputStream(int x, int z) {
        int chunkX = x % 32;
        if (chunkX < 0) chunkX = chunkX + 32;
        int chunkZ = z % 32;
        if (chunkZ < 0) chunkZ = chunkZ + 32;

        RegionFile regionFile = this.getRegionFile(x, z);

        synchronized (regionFile) {
            if (regionFile.chunkExists(chunkX, chunkZ)) {
                return regionFile.getChunkDataOutputStream(chunkX, chunkZ);
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
