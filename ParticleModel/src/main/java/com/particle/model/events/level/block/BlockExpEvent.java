package com.particle.model.events.level.block;

import com.particle.model.level.Level;

public class BlockExpEvent extends LevelBlockEvent {

    /**
     * 经验
     */
    private int exp;

    public BlockExpEvent(Level level) {
        super(level);
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
