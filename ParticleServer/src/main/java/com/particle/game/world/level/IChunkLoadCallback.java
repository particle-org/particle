package com.particle.game.world.level;

import com.particle.model.level.Chunk;
import com.particle.model.level.Level;

@FunctionalInterface
public interface IChunkLoadCallback {
    void onload(Level level, Chunk chunk);
}
