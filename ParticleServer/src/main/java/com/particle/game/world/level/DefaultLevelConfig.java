package com.particle.game.world.level;

import com.particle.util.configer.anno.ConfigBean;

@ConfigBean(name = "DefaultLevelConfig")
public class DefaultLevelConfig {

    private int spawnX = 128;
    private int spawnY = 60;
    private int spawnZ = 128;

    private boolean forceSpawn = false;

    public int getSpawnX() {
        return spawnX;
    }

    public void setSpawnX(int spawnX) {
        this.spawnX = spawnX;
    }

    public int getSpawnY() {
        return spawnY;
    }

    public void setSpawnY(int spawnY) {
        this.spawnY = spawnY;
    }

    public int getSpawnZ() {
        return spawnZ;
    }

    public void setSpawnZ(int spawnZ) {
        this.spawnZ = spawnZ;
    }

    public boolean isForceSpawn() {
        return forceSpawn;
    }

    public void setForceSpawn(boolean forceSpawn) {
        this.forceSpawn = forceSpawn;
    }
}
