package com.particle.game.block.attribute;

import com.particle.core.ecs.module.BehaviorModule;

public class BlockTransparencyModule extends BehaviorModule {

    /**
     * 透明度
     * 0表示透明
     * 1表示完全不透明
     * 2表示局部透明或可变的
     * 3表示未知
     */
    private int transparency;

    public int getTransparency() {
        return transparency;
    }

    public void setTransparency(int transparency) {
        this.transparency = transparency;
    }

}
