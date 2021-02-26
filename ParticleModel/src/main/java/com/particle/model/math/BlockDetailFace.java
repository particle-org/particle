package com.particle.model.math;


import java.util.Random;

public enum BlockDetailFace {
    SOUTH(0, 8, "south", new Vector3f(0, 0, 1)),
    WEST_SOUTH_SOUTH(1, 9, "westSouthSouth", new Vector3f(0, 0, 0)),
    WEST_SOUTH(2, 10, "westSouth", new Vector3f(0, 0, 0)),
    WEST_SOUTH_WEST(3, 11, "westSouthWest", new Vector3f(0, 0, 0)),
    WEST(4, 12, "west", new Vector3f(0, 0, 0)),
    WEST_NORTH_WEST(5, 13, "westNorthWest", new Vector3f(0, 0, 0)),
    WEST_NORTH(6, 14, "westNorth", new Vector3f(0, 0, 0)),
    WEST_NORTH_NORTH(7, 15, "westNorthNorth", new Vector3f(0, 0, 0)),
    NORTH(8, 0, "north", new Vector3f(0, 0, 0)),
    EAST_NORTH_NORTH(9, 1, "eastNorthNorth", new Vector3f(0, 0, 0)),
    EAST_NORTH(10, 2, "eastNorth", new Vector3f(0, 0, 0)),
    EAST_NORTH_EAST(11, 3, "eastNorthEast", new Vector3f(0, 0, 0)),
    EAST(12, 4, "east", new Vector3f(0, 0, 0)),
    EAST_SOUTH_EAST(13, 5, "eastSouthEast", new Vector3f(0, 0, 0)),
    EAST_SOUTH(14, 6, "eastSouth", new Vector3f(0, 0, 0)),
    EAST_SOUTH_SOUTH(15, 7, "eastSouthSouth", new Vector3f(0, 0, 0));

    /**
     * 朝向反序列化缓存
     */
    private static final BlockDetailFace[] FACE = new BlockDetailFace[16];

    static {
        for (BlockDetailFace face : values()) {
            FACE[face.index] = face;
        }
    }

    /**
     * 编号
     */
    private final int index;

    /**
     * 反方向
     */
    private final int opposite;

    /**
     * 名称
     */
    private final String name;

    /**
     * 单位向量
     */
    private final Vector3f unitVector;

    BlockDetailFace(int index, int opposite, String name, Vector3f unitVector) {
        this.opposite = opposite;
        this.index = index;
        this.name = name;
        this.unitVector = unitVector;
    }

    /**
     * 通过值获取朝向
     *
     * @param index
     * @return
     */
    public static BlockDetailFace fromIndex(int index) {
        return FACE[MathUtils.abs(index & 3)];
    }

    /**
     * 随机朝向
     */
    public static BlockDetailFace random(Random rand) {
        return FACE[rand.nextInt(FACE.length)];
    }

    /**
     * 获取编号
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取单位向量
     */
    public Vector3f getUnitVector() {
        return unitVector;
    }

    /**
     * 获取相反方向
     */
    public BlockDetailFace getOpposite() {
        return fromIndex(opposite);
    }

    /**
     * 获取相反方向的index
     */
    public int getOppositeIndex() {
        return this.opposite;
    }

    public String toString() {
        return name;
    }
}
