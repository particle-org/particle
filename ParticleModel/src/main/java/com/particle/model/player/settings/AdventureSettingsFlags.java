package com.particle.model.player.settings;

import java.util.HashMap;
import java.util.Map;

public enum AdventureSettingsFlags {
    WorldImmutable(1 << 0),
    NoPvM(1 << 1),
    NoMvP(1 << 2),
    Unused(1 << 3),
    ShowNameTags(1 << 4),
    AutoJump(1 << 5),
    PlayerMayFly(1 << 6),
    PlayerNoClip(1 << 7),
    PlayerWorldBuilder(1 << 8),
    PlayerFlying(1 << 9),
    PlayerMuted(1 << 10);

    private static final Map<Integer, AdventureSettingsFlags> dict = new HashMap<>();

    static {
        for (AdventureSettingsFlags item : AdventureSettingsFlags.values()) {
            dict.put(item.getMode(), item);
        }
    }

    public static AdventureSettingsFlags valueOf(int key) {
        return dict.get(key);
    }

    AdventureSettingsFlags(int mode) {
        this.mode = mode;
    }

    private int mode;

    public int getMode() {
        return mode;
    }
}
