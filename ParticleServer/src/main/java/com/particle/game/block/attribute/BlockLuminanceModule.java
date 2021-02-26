package com.particle.game.block.attribute;

import com.particle.core.ecs.module.BehaviorModule;

public class BlockLuminanceModule extends BehaviorModule {

    /**
     * 本身亮度
     * 0-15
     */
    private int luminance;

    public int getLuminance() {
        return luminance;
    }

    public void setLuminance(int luminance) {
        this.luminance = luminance;
    }
}
