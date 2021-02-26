package com.particle.model.inventory.common;

public enum ItemUseOnEntityInventoryType {
    INTERACT(0, "interact"),
    ATTACK(1, "attack"),
    ITEM_INTERACT(2, "item_interact");

    private int type;

    private String name;

    ItemUseOnEntityInventoryType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    private static final ItemUseOnEntityInventoryType[] types = ItemUseOnEntityInventoryType.values();

    public static ItemUseOnEntityInventoryType valueOf(int type) {
        return types[type];
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
