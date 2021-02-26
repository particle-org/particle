package com.particle.model.ui.score;

import java.util.HashMap;
import java.util.Map;

public enum ScoreboardIdentityPacketType {
    Update(0),
    Remove(1);

    private int type;

    private static final Map<Integer, ScoreboardIdentityPacketType> types = new HashMap<>();

    static {
        for (ScoreboardIdentityPacketType item : ScoreboardIdentityPacketType.values()) {
            types.put(item.getType(), item);
        }
    }

    ScoreboardIdentityPacketType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static ScoreboardIdentityPacketType from(int type) {
        return types.get(type);
    }
}
