package com.particle.model.level.settings;

import java.util.HashMap;
import java.util.Map;

public enum Dimension {
    Overworld(0, "Overworld"),
    Nether(1, "Nether"),
    TheEnd(2, "TheEnd"),
    Count(3, "Count");

    private static final Map<Integer, Dimension> dict = new HashMap<>();

    static {
        for (Dimension item : Dimension.values()) {
            dict.put(item.getMode(), item);
        }
    }

    public static Dimension valueOf(int key) {
        return dict.get(key);
    }

    Dimension(int mode, String toast) {
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

