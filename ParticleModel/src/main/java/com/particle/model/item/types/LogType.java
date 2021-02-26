package com.particle.model.item.types;

public enum LogType {
    OAK_LOG(17, 0, "oakLog"),
    SPRUCE_LOG(17, 1, "spruceLog"),
    BIRCH_LOG(17, 2, "birchLog"),
    JUNGLE_LOG(17, 3, "jungleLog"),
    ACACIA_LOG(162, 0, "acaciaLog"),
    DARK_OAK_LOG(162, 1, "darkOakLog");


    private int id;
    private int meta;
    private String name;

    LogType(int id, int meta, String name) {
        this.id = id;
        this.meta = meta;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getMeta() {
        return meta;
    }

    public String getName() {
        return name;
    }
}
