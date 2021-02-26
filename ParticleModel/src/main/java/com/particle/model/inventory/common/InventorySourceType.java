package com.particle.model.inventory.common;

import java.util.HashMap;
import java.util.Map;

public enum InventorySourceType {
    CONTAINER(0, "container_inventory"),
    GLOBAL(1, "global_inventory"),
    WORLD(2, "world_inventory"),
    CREATIVE(3, "creative_inventory"),
    CRAFTING_GRID(100, "crafting_grid"),
    INVALID(99999, "invalid_inventory");

    private int sourceType;

    private String name;

    private static final Map<Integer, InventorySourceType> allInventorySourceType = new HashMap<>();

    static {
        InventorySourceType[] types = InventorySourceType.values();
        for (InventorySourceType type : types) {
            allInventorySourceType.put(type.getSourceType(), type);
        }
    }

    public static InventorySourceType valueOf(int sourceType) {
        return allInventorySourceType.get(sourceType);
    }

    InventorySourceType(int sourceType, String name) {
        this.sourceType = sourceType;
        this.name = name;
    }

    public int getSourceType() {
        return sourceType;
    }

    public String getName() {
        return name;
    }
}
