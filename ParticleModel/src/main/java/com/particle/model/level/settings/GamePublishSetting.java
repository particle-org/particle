package com.particle.model.level.settings;

import java.util.HashMap;
import java.util.Map;

public enum GamePublishSetting {
    NoMultiPlay(0),
    InviteOnly(1),
    FriendsOnly(2),
    FriendsOfFriends(3),
    Public(4);

    private static final Map<Integer, GamePublishSetting> dict = new HashMap<>();

    static {
        for (GamePublishSetting item : GamePublishSetting.values()) {
            dict.put(item.getMode(), item);
        }
    }

    public static GamePublishSetting valueOf(int key) {
        return dict.get(key);
    }

    GamePublishSetting(int mode) {
        this.mode = mode;
    }

    private int mode;

    public int getMode() {
        return mode;
    }
}
