package com.particle.model.math;

public enum BlockTouchFace {
    EAST(1, "east"),
    WEST(2, "west"),
    SOUTH(3, "south"),
    NORTH(4, "north"),
    UP(5, "up");

    private final int index;

    private final String name;

    private static BlockTouchFace[] blockTouchFaces = new BlockTouchFace[]{EAST, WEST, SOUTH, NORTH, UP};

    BlockTouchFace(int index, String name) {
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

    public static BlockTouchFace getBlockTouchFace(int index) {
        if (index >= 0 && index < 6) {
            return blockTouchFaces[index - 1];
        }

        return null;
    }
}
