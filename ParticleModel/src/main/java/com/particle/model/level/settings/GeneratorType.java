package com.particle.model.level.settings;

import java.util.HashMap;
import java.util.Map;

public enum GeneratorType {
    Legacy(0, "Legacy"),
    Overworld(1, "Overworld"),
    Flat(2, "Flat"),
    Nether(3, "Nether"),
    TheEnd(4, "TheEnd");

    private static final Map<Integer, GeneratorType> dict = new HashMap<>();

    static {
        for (GeneratorType item : GeneratorType.values()) {
            dict.put(item.getType(), item);
        }
    }

    public static GeneratorType valueOf(int key) {
        return dict.get(key);
    }


    GeneratorType(int type, String toast) {
        this.type = type;
        this.toast = toast;
    }

    private int type;

    private String toast;

    public int getType() {
        return type;
    }

    public String getToast() {
        return toast;
    }
}
