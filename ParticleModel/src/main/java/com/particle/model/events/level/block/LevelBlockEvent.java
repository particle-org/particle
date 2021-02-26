package com.particle.model.events.level.block;

import com.particle.model.block.Block;
import com.particle.model.events.level.LevelEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;

public class LevelBlockEvent extends LevelEvent {
    private Vector3 position;
    private Block block;

    public LevelBlockEvent(Level level) {
        super(level);
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
