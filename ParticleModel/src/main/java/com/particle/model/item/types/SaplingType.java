package com.particle.model.item.types;

public enum SaplingType {
    OAK_SAPLING(6, 0, "oakSapling"),
    SPRUCE_SAPLING(6, 1, "spruceSapling"),
    BIRCH_SAPLING(6, 2, "birchSapling"),
    JUNGLE_SAPLING(6, 3, "jungleSapling"),
    ACACIA_SAPLING(6, 4, "acaciaSapling"),
    DARK_OAK_SAPLING(6, 5, "darkOakSapling");


    private int id;
    private int meta;
    private String name;

    SaplingType(int id, int meta, String name) {
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
