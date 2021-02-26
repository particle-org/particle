package com.particle.model.inventory.common;

public enum ItemUseInventoryActionType {
    PLACE(0, "place"),
    USE(1, "use"),
    DESTROY(2, "destroy");

    private int type;

    private String name;

    ItemUseInventoryActionType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    private static final ItemUseInventoryActionType[] types = ItemUseInventoryActionType.values();

    public static final ItemUseInventoryActionType valueOf(int type) {
        return types[type];
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ItemUseInventoryActionType{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
