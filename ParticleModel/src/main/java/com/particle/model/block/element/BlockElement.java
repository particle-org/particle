package com.particle.model.block.element;

/**
 * 参考：https://minecraft.gamepedia.com/Breaking
 */
public enum BlockElement {
    PLANT(0, "AXE", false),
    WOOD(0, "AXE", false),
    ICE(0, "PICKAXE", false),
    PICKAXE(0, "PICKAXE", false),
    METAL0(0, "PICKAXE", false),
    METAL1(1, "PICKAXE", false),
    METAL2(2, "PICKAXE", false),
    METAL3(3, "PICKAXE", false),
    RAIL(0, "PICKAXE", false),
    ROCK0(0, "PICKAXE", false),
    ROCK1(1, "PICKAXE", false),
    ROCK2(2, "PICKAXE", false),
    ROCK3(3, "PICKAXE", false),
    ROCK4(4, "PICKAXE", false),
    SAND(0, "SHOVEL", false),
    DIRT(0, "SHOVEL", false),
    SNOW(1, "SHOVEL", false),
    LEAVES(1, "SHEARS", false),
    GLASS(5, "NULL", false),
    DEFAULT(0, "NULL", false),
    BEDROCK(999, "NULL", false),
    LIQUID(0, "NULL", true);

    private int level;
    private String toolName;
    private boolean isLiquid;

    BlockElement(int level, String toolName, boolean isLiquid) {
        this.level = level;
        this.toolName = toolName;
        this.isLiquid = isLiquid;
    }

    public int getLevel() {
        return level;
    }

    public String getToolName() {
        return toolName;
    }

    public boolean isLiquid() {
        return isLiquid;
    }
}
