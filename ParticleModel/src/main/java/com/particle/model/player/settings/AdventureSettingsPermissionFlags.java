package com.particle.model.player.settings;

import java.util.HashMap;
import java.util.Map;

public enum AdventureSettingsPermissionFlags {
    BuildAndMine(1 << 0),
    DoorsAndSwitches(1 << 1),
    OpenContainers(1 << 2),
    AttackPlayers(1 << 3),
    AttackMobs(1 << 4),
    OP(1 << 5),
    Teleport(1 << 7),
    DefaultLevelPermissions(1 << 8);

    private static final Map<Integer, AdventureSettingsPermissionFlags> dict = new HashMap<>();

    static {
        for (AdventureSettingsPermissionFlags item : AdventureSettingsPermissionFlags.values()) {
            dict.put(item.getMode(), item);
        }
    }

    public static AdventureSettingsPermissionFlags valueOf(int key) {
        return dict.get(key);
    }

    AdventureSettingsPermissionFlags(int mode) {
        this.mode = mode;
    }

    private int mode;

    public int getMode() {
        return mode;
    }
}
