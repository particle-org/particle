package com.particle.model.math;

public enum BlockStairsFace {
    DOWN_EAST(0, "downEast"),
    DOWN_WEST(1, "downWest"),
    DOWN_SOUTH(2, "downSouth"),
    DOWN_NORTH(3, "downNorth"),

    UP_EAST(4, "upEast"),
    UP_WEST(5, "upWest"),
    UP_SOUTH(6, "upSouth"),
    UP_NORTH(7, "upNorth");

    private final int index;

    private final String name;

    BlockStairsFace(int index, String name) {
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
}
