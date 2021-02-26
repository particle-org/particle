package com.particle.model.math;

public enum BlockLeverFace {
    DOWN_EAST(0, "downEast"),
    SIDE_EAST(1, "sideEast"),
    SIDE_WEST(2, "sideWest"),
    SIDE_SOUTH(3, "sideSouth"),
    SIDE_NORTH(4, "sideNorth"),
    UP_SOUTH(5, "upSouth"),
    UP_EAST(6, "upEast"),
    DOWN_SOUTH(7, "downSouth");

    private final int index;

    private final String name;

    private static BlockLeverFace[] blockLeverFaces = new BlockLeverFace[]{DOWN_EAST, SIDE_EAST, SIDE_WEST, SIDE_SOUTH, SIDE_NORTH, UP_SOUTH, UP_EAST, DOWN_SOUTH};

    BlockLeverFace(int index, String name) {
        this.index = index;
        this.name = name;
    }

    /**
     * Get the index of this BlockFace (0-5). The order is D-U-N-S-W-E
     */
    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public static BlockLeverFace getBlockLeverFace(int index) {
        if (index >= 0 && index < 8) {
            return blockLeverFaces[index];
        }

        return null;
    }
}
