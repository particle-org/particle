package com.particle.model.inventory.common;

public enum InventoryTransactionType {
    NORMAL(0, "normal_transaction"),
    MISMATCH(1, "inventory_mismatch"),
    ITEM_USE(2, "item_use_transaction"),
    ITEM_USE_ON_ENTITY(3, "item_use_on_entity_transaction"),
    ITEM_RELEASE(4, "item_release_transaction");

    private int type;

    private String name;

    InventoryTransactionType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    private static final InventoryTransactionType[] types = InventoryTransactionType.values();

    public static InventoryTransactionType valueOf(int type) {
        return types[type];
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
