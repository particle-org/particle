package com.particle.game.scene.module;

import com.particle.core.aoi.model.Grid;
import com.particle.core.ecs.module.BehaviorModule;

public class GridKeepAliveModule extends BehaviorModule {

    // 20s 回收
    private static final int MAX_TTL_TICK = 20 * 1000;

    private long currentTtl = MAX_TTL_TICK;

    private Grid grid;

    public boolean ttl(long interval) {
        if (grid != null) {
            boolean enabled = grid.getSubscribersSize() > 0;

            if (enabled) {
                currentTtl = MAX_TTL_TICK;
                return true;
            }

            this.currentTtl -= interval;

            return this.currentTtl > 0;
        }

        return true;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
