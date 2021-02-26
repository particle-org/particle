package com.particle.game.block.attribute;

import com.particle.core.ecs.module.BehaviorModule;

public class BlockCoverModule extends BehaviorModule {

    /**
     * 是否可被覆蓋
     */
    private boolean isCanBeCover;

    public boolean isCanBeCover() {
        return isCanBeCover;
    }

    public void setCanBeCover(boolean canBeCover) {
        isCanBeCover = canBeCover;
    }
}
