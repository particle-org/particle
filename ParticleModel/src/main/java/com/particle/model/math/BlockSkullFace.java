package com.particle.model.math;

public enum BlockSkullFace {
    NORTH(0, "north"),
    EAST_NORTH_NORTH(1, "eastNorthNorth"),
    EAST_NORTH(2, "eastNorth"),
    EAST_NORTH_EAST(3, "eastNorthEast"),
    EAST(4, "east"),
    EAST_SOUTH_EAST(5, "eastSouthEast"),
    EAST_SOUTH(6, "eastSouth"),
    EAST_SOUTH_SOUTH(7, "eastSouthSouth"),
    SOUTH(8, "south"),
    WEST_SOUTH_SOUTH(9, "westSouthSouth"),
    WEST_SOUTH(10, "westSouth"),
    WEST_SOUTH_WEST(11, "westSouthWest"),
    WEST(12, "west"),
    WEST_NORTH_WEST(13, "westNorthWest"),
    WEST_NORTH(14, "westNorth"),
    WEST_NORTH_NORTH(15, "westNorthNorth");


    private final int index;

    private final String name;

    BlockSkullFace(int index, String name) {
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
