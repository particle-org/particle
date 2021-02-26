package com.particle.game.player.components;

import com.particle.core.ecs.module.BehaviorModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlineTimeRecorderModule extends BehaviorModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineTimeRecorderModule.class);

    private long onlineTime = 0;
    private long loginTime = -1;

    public long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }
}
