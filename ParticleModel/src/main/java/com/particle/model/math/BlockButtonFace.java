package com.particle.model.math;

public enum BlockButtonFace {
    DOWN(0, "down"),
    UP(1, "up"),
    NORTH(2, "north"),
    SOUTH(3, "south"),
    WEST(4, "west"),
    EAST(5, "east");

    private final int index;

    private final String name;

    private static BlockButtonFace[] blockButtonFaces = new BlockButtonFace[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};

    BlockButtonFace(int index, String name) {
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

    public static BlockButtonFace getBlockButtonFace(int index) {
        if (index >= 0 && index < 6) {
            return blockButtonFaces[index];
        }

        return null;
    }
}
