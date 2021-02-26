package com.particle.api.entity.function;

import com.particle.model.level.Chunk;
import com.particle.model.level.Level;

public interface ISpawnEntityProcessor {
    void spawn(Level level, Chunk chunk);

    void respawn(Level level, Chunk from, Chunk to);

    void despawn(Chunk chunk);
}