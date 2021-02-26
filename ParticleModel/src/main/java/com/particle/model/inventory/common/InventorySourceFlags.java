package com.particle.model.inventory.common;

public enum InventorySourceFlags {
    NO_FLAG(0, "no_flag"),
    WORLD_INTERACTION(1, "world_interaction_random");

    private int flag;

    private String name;

    private static final InventorySourceFlags[] allFlags = InventorySourceFlags.values();

    InventorySourceFlags(int flag, String name) {
        this.flag = flag;
        this.name = name;
    }

    public static InventorySourceFlags valueOf(int flag) {
        return allFlags[flag];
    }

    public int getFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }
}
