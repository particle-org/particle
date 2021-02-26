package com.particle.game.block.attribute;

import com.particle.core.ecs.module.BehaviorModule;

public class BlockScourModule extends BehaviorModule {

    /**
     * 是否可被沖刷
     */
    private boolean isCanBeScour;

    public boolean isCanBeScour() {
        return isCanBeScour;
    }

    public void setCanBeScour(boolean canBeScour) {
        isCanBeScour = canBeScour;
    }

}