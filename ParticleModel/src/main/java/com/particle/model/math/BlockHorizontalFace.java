package com.particle.model.math;


import java.util.Random;

public enum BlockHorizontalFace {
    NORTH(2, 0, "north", new Vector3f(0, 0, -1)),
    SOUTH(0, 2, "south", new Vector3f(0, 0, 1)),
    WEST(1, 3, "west", new Vector3f(-1, 0, 0)),
    EAST(3, 1, "east", new Vector3f(1, 0, 0));

    /**
     * 朝向反序列化缓存
     */
    private static final BlockHorizontalFace[] HORIZONTALS = new BlockHorizontalFace[4];

    static {
        for (BlockHorizontalFace face : values()) {
            HORIZONTALS[face.index] = face;
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

    BlockHorizontalFace(int index, int opposite, String name, Vector3f unitVector) {
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
    public static BlockHorizontalFace fromIndex(int index) {
        return HORIZONTALS[MathUtils.abs(index & 3)];
    }

    /**
     * 随机朝向
     */
    public static BlockHorizontalFace random(Random rand) {
        return HORIZONTALS[rand.nextInt(HORIZONTALS.length)];
    }

    /**
     * 获取编号
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取角度
     */
    public float getHorizontalAngle() {
        return (float) ((index & 3) * 90);
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
    public BlockHorizontalFace getOpposite() {
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
