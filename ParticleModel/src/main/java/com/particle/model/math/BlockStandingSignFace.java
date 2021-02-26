package com.particle.model.math;

public enum BlockStandingSignFace {
    SOUTH(0, "south"),
    WEST_SOUTH_SOUTH(1, "westSouthSouth"),
    WEST_SOUTH(2, "westSouth"),
    WEST_SOUTH_WEST(3, "westSouthWest"),
    WEST(4, "west"),
    WEST_NORTH_WEST(5, "westNorthWest"),
    WEST_NORTH(6, "westNorth"),
    WEST_NORTH_NORTH(7, "westNorthNorth"),
    NORTH(8, "north"),
    EAST_NORTH_NORTH(9, "eastNorthNorth"),
    EAST_NORTH(10, "eastNorth"),
    EAST_NORTH_EAST(11, "eastNorthEast"),
    EAST(12, "east"),
    EAST_SOUTH_EAST(13, "eastSouthEast"),
    EAST_SOUTH(14, "eastSouth"),
    EAST_SOUTH_SOUTH(15, "eastSouthSouth");

    private final int index;

    private final String name;

    BlockStandingSignFace(int index, String name) {
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
