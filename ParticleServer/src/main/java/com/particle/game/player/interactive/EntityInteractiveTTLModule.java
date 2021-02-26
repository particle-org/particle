package com.particle.game.player.interactive;

import com.particle.core.ecs.module.BehaviorModule;

public class EntityInteractiveTTLModule extends BehaviorModule {

    private long playerPlaceBlockTTL = 200;
    private long playerPlaceBlockTimestamp;

    public boolean requestPlaceBlockValid() {
        long timestamp = System.currentTimeMillis();
        if (timestamp - playerPlaceBlockTimestamp > playerPlaceBlockTTL) {
            playerPlaceBlockTimestamp = timestamp;
            return true;
        }

        return false;
    }

}
