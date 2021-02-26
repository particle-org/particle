package com.particle.game.entity.spawn;

import com.particle.core.ecs.module.BehaviorModule;

public class AutoRemovedModule extends BehaviorModule {
    /*
     * 400 Tick 后移除
     */
    private long keepLiveTime = 400;

    public void ttl() {
        if (keepLiveTime > 0) {
            this.keepLiveTime--;
        }
    }

    public long getKeepLiveTime() {
        return keepLiveTime;
    }

    public void setKeepLiveTime(long keepLiveTime) {
        this.keepLiveTime = keepLiveTime;
    }
}
