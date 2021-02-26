package com.particle.model.inventory.common;

public enum ItemReleaseInventoryType {
    RELEASE(0, "release"),
    USE(1, "use");

    private int type;

    private String name;

    ItemReleaseInventoryType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    private static final ItemReleaseInventoryType[] types = ItemReleaseInventoryType.values();

    public static ItemReleaseInventoryType valueOf(int type) {
        return types[type];
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
