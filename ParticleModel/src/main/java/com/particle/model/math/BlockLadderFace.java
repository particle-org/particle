package com.particle.model.math;

public enum BlockLadderFace {
    NORTH(2, "north"),
    SOUTH(3, "south"),
    WEST(4, "west"),
    EAST(5, "east");

    private final int index;

    private final String name;

    private static BlockLadderFace[] blockLadderFaces = new BlockLadderFace[]{NORTH, SOUTH, WEST, EAST};

    BlockLadderFace(int index, String name) {
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

    public static BlockLadderFace getBlockLadderFace(int index) {
        if (index >= 0 && index < 6) {
            return blockLadderFaces[index - 2];
        }

        return null;
    }
}
