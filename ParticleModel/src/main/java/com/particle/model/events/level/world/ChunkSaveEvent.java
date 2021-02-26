package com.particle.model.events.level.world;

import com.particle.model.events.level.LevelEvent;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;

public class ChunkSaveEvent extends LevelEvent {

    private Chunk chunk;

    public ChunkSaveEvent(Level level, Chunk chunk) {
        super(level);
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
