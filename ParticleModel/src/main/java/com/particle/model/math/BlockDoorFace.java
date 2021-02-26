package com.particle.model.math;

public enum BlockDoorFace {
    WEST(0, "west"),
    NORTH(1, "north"),
    EAST(2, "east"),
    SOUTH(3, "south");

    private final int index;

    private final String name;

    private static BlockDoorFace[] blockDoorFaces = new BlockDoorFace[]{WEST, NORTH, EAST, SOUTH};

    BlockDoorFace(int index, String name) {
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

    public static BlockDoorFace getBlockDoorFace(int index) {
        if (index >= 0 && index < 4) {
            return blockDoorFaces[index];
        }

        return null;
    }
}
