package com.particle.game.block.attribute;

import com.particle.core.ecs.module.BehaviorModule;

public class BlockFlammableModule extends BehaviorModule {

    /**
     * 是否可燃
     */
    private boolean isFlammable;

    public boolean isFlammable() {
        return isFlammable;
    }

    public void setFlammable(boolean flammable) {
        isFlammable = flammable;
    }
}
