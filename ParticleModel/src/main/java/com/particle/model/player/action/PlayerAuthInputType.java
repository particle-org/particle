package com.particle.model.player.action;

import java.util.HashMap;
import java.util.Map;

public enum PlayerAuthInputType {
    ActionStartSprinting(0x2000000L),
    ActionStopSprinting(0x4000000L),
    ActionStartSneaking(0x8000000L),
    ActionStopSneaking(0x10000000L),
    ActionStartSwimming(0x20000000L),
    ActionStopSwimming(0x40000000L),
    ActionStartJump(0x80000000L),
    ActionStartGliding(0x100000000L),
    ActionStopGliding(0x200000000L),

    ;

    private long status;

    PlayerAuthInputType(long status) {
        this.status = status;
    }

    private static Map<Long, PlayerAuthInputType> playerAuthInputTypeMap = new HashMap<>();

    static {
        for (PlayerAuthInputType value : PlayerAuthInputType.values()) {
            playerAuthInputTypeMap.put(value.status, value);
        }
    }

    public static PlayerAuthInputType fromStatus(long status) {
        return playerAuthInputTypeMap.get(status);
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }
}
