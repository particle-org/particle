package com.particle.model.math;

public enum BlockLoomFace {
    NORTH(0, "north"),
    SOUTH(2, "south"),
    WEST(3, "west"),
    EAST(1, "east");

    private final int index;

    private final String name;

    private static BlockLoomFace[] blockLoomFaces = new BlockLoomFace[]{NORTH, SOUTH, WEST, EAST};

    BlockLoomFace(int index, String name) {
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

    public static BlockLoomFace getBlockLoomFace(int index) {
        if (index >= 0 && index < 6) {
            return blockLoomFaces[index - 2];
        }

        return null;
    }
}
