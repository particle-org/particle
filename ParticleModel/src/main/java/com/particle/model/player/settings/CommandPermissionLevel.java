package com.particle.model.player.settings;

import java.util.HashMap;
import java.util.Map;

public enum CommandPermissionLevel {
    Any(0),
    GameMasters(1),
    Admin(2),
    Host(3),
    Owner(4),
    Internal(5);

    private static final Map<Integer, CommandPermissionLevel> dict = new HashMap<>();

    static {
        for (CommandPermissionLevel item : CommandPermissionLevel.values()) {
            dict.put(item.getLevel(), item);
        }
    }

    public static CommandPermissionLevel valueOf(int key) {
        return dict.get(key);
    }

    CommandPermissionLevel(int level) {
        this.level = level;
    }

    private int level;

    public int getLevel() {
        return level;
    }
}
