package com.particle.model.player.settings;

import java.util.HashMap;
import java.util.Map;

public enum PlayerPermissionLevel {
    Visitor(0),
    Member(1),
    Operator(2),
    Custom(3);

    PlayerPermissionLevel(int level) {
        this.level = level;
    }

    private static final Map<Integer, PlayerPermissionLevel> dict = new HashMap<>();

    static {
        for (PlayerPermissionLevel item : PlayerPermissionLevel.values()) {
            dict.put(item.getLevel(), item);
        }
    }

    public static PlayerPermissionLevel valueOf(int key) {
        return dict.get(key);
    }

    private int level;

    public int getLevel() {
        return level;
    }
}
