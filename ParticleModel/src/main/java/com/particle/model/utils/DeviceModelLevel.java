package com.particle.model.utils;

import java.util.HashMap;

public enum DeviceModelLevel {
    LOW(0),
    MID_LOW(1),
    MID(2),
    MID_HIGH(3),
    HIGH(4);

    private int level;

    private static HashMap<Integer, DeviceModelLevel> dict = new HashMap<>();

    static {
        for (DeviceModelLevel level1 : DeviceModelLevel.values()) {
            dict.put(level1.getLevel(), level1);
        }
    }

    DeviceModelLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    /**
     * 获取机型的级别
     *
     * @param level
     * @return
     */
    public static DeviceModelLevel from(int level) {
        DeviceModelLevel modelLevel = dict.get(level);
        if (modelLevel == null) {
            return DeviceModelLevel.LOW;
        }
        return modelLevel;
    }
}
