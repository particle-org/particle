package com.particle.game.world.aoi.components;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.math.Vector3f;

public class PlayerAutoChunkSubscriberModule extends BehaviorModule {

    private int lastChunkX = Integer.MIN_VALUE;
    private int lastChunkZ = Integer.MIN_VALUE;

    /**
     * 检查是否需要更新
     *
     * @param position
     * @return 是否需要更新
     */
    public boolean checkForUpdate(Vector3f position) {
        int chunkX = (int) Math.floor(position.getX() / 16);
        int chunkZ = (int) Math.floor(position.getZ() / 16);

        if (lastChunkX != chunkX || lastChunkZ != chunkZ) {
            lastChunkX = chunkX;
            lastChunkZ = chunkZ;

            return true;
        }

        return false;
    }
}
