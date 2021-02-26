package com.particle.model.item.types;

public enum LeavesType {
    OAK_LEAVES(18, 0, "oakLeaves"),
    SPRUCE_LEAVES(18, 1, "spruceLeaves"),
    BIRCH_LEAVES(18, 2, "birchLeaves"),
    JUNGLE_LEAVES(18, 3, "jungleLeaves"),

    OAK_LEAVES_NO_ROT(18, 4, "oakLeavesNoRot"),
    SPRUCE_LEAVES_NO_ROT(18, 5, "spruceLeavesNoRot"),
    BIRCH_LEAVES_NO_ROT(18, 6, "birchLeavesNoRot"),
    JUNGLE_LEAVES_NO_ROT(18, 7, "jungleLeavesNoRot"),

    OAK_LEAVES_CHECK_ROT(18, 8, "oakLeavesCheckRot"),
    SPRUCE_LEAVES_CHECK_ROT(18, 9, "spruceLeavesCheckRot"),
    BIRCH_LEAVES_CHECK_ROT(18, 10, "birchLeavesCheckRot"),
    JUNGLE_LEAVES_CHECK_ROT(18, 11, "jungleLeavesCheckRot"),

    OAK_LEAVES_NO_AND_CHECK_ROT(18, 12, "oakLeavesNoAndCheckRot"),
    SPRUCE_LEAVES_NO_AND_CHECK_ROT(18, 13, "spruceLeavesNoAndCheckRot"),
    BIRCH_LEAVES_NO_AND_CHECK_ROT(18, 14, "birchLeavesNoAndCheckRot"),
    JUNGLE_LEAVES_NO_AND_CHECK_ROT(18, 15, "jungleLeavesNoAndCheckRot"),

    ACACIA_LEAVES(161, 0, "acaciaLeaves"),
    DARK_OAK_LEAVES(161, 1, "darkOakLeaves"),

    ACACIA_LEAVES_NO_ROT(161, 4, "acaciaLeavesNoRot"),
    DARK_OAK_LEAVES_NO_ROT(161, 5, "darkOakLeavesNoRot"),

    ACACIA_LEAVES_CHECK_ROT(161, 8, "acaciaLeavesCheckRot"),
    DARK_OAK_LEAVES_CHECK_ROT(161, 9, "darkOakLeavesCheckRot"),

    ACACIA_LEAVES_NO_AND_CHECK_ROT(161, 12, "acaciaLeavesNoAndCheckRot"),
    DARK_OAK_LEAVES_NO_AND_CHECK_ROT(161, 13, "darkOakLeavesNoAndCheckRot");

    private int id;
    private int meta;
    private String name;

    LeavesType(int id, int meta, String name) {
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
