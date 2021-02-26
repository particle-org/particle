package com.particle.model.ui.score;

import java.util.HashMap;
import java.util.Map;

public enum ScorePacketType {
    Change(0),
    Remove(1);

    private int type;

    private static final Map<Integer, ScorePacketType> types = new HashMap<>();

    static {
        for (ScorePacketType item : ScorePacketType.values()) {
            types.put(item.getType(), item);
        }
    }

    ScorePacketType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static ScorePacketType from(int type) {
        return types.get(type);
    }
}
