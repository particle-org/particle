package com.particle.game.world.level;

import com.particle.model.level.Chunk;
import com.particle.model.level.Level;

@FunctionalInterface
public interface IChunkUnloadCallback {
    void onunload(Level level, Chunk chunk);
}
