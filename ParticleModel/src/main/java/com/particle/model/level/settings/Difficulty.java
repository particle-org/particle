package com.particle.model.level.settings;

import java.util.HashMap;
import java.util.Map;

public enum Difficulty {
    Peaceful(0, "Peaceful"),
    Easy(1, "Easy"),
    Normal(2, "Normal"),
    Hard(3, "Hard"),
    Count(4, "Count");

    private static final Map<Integer, Difficulty> dict = new HashMap<>();

    static {
        for (Difficulty item : Difficulty.values()) {
            dict.put(item.getMode(), item);
        }
    }

    public static Difficulty valueOf(int key) {
        return dict.get(key);
    }

    Difficulty(int mode, String toast) {
        this.mode = mode;
        this.toast = toast;
    }

    private int mode;

    private String toast;

    public int getMode() {
        return mode;
    }

    public String getToast() {
        return toast;
    }
}
