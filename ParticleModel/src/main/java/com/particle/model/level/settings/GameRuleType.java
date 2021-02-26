package com.particle.model.level.settings;

import java.util.HashMap;
import java.util.Map;

public enum GameRuleType {
    Invalid(0),
    Bool(1),
    Int(2),
    Float(3);

    private static final Map<Integer, GameRuleType> dict = new HashMap<>();

    static {
        for (GameRuleType item : GameRuleType.values()) {
            dict.put(item.getType(), item);
        }
    }

    public static GameRuleType valueOf(int key) {
        return dict.get(key);
    }

    GameRuleType(int type) {
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }
}
