package com.particle.model.math;

public class Vector3 {
    private int x;
    private int y;
    private int z;

    public Vector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3f vector3f) {
        this(vector3f.getFloorX(), vector3f.getFloorY(), vector3f.getFloorZ());
    }

    public Vector3 add(Vector3 position) {
        return new Vector3(this.x + position.getX(), this.y + position.getY(), this.z + position.getZ());
    }

    public Vector3f add(Vector3f position) {
        return new Vector3f(this.x + position.getX(), this.y + position.getY(), this.z + position.getZ());
    }

    public Vector3 add(int x, int y, int z) {
        return new Vector3(this.x + x, this.y + y, this.z + z);
    }

    public Vector3 subtract(Vector3 position) {
        return new Vector3(this.x - position.getX(), this.y - position.getY(), this.z - position.getZ());
    }

    public Vector3 subtract(int x, int y, int z) {
        return new Vector3(this.x - x, this.y - y, this.z - z);
    }

    public Vector3 multiply(int number) {
        return new Vector3(this.x * number, this.y * number, this.z * number);
    }

    public Vector3 multiply(double number) {
        return new Vector3((int) (this.x * number), (int) (this.y * number), (int) (this.z * number));
    }

    public Vector3 divide(int number) {
        return new Vector3(this.x / number, this.y / number, this.z / number);
    }

    public boolean isAllZero() {
        return this.x == 0 && this.y == 0 && this.z == 0;
    }

    public boolean AllLengthLess(int length) {
        return this.x < length && this.x > -length && this.y < length && this.y > -length && this.z < length && this.z > -length;
    }

    public Vector3 getSide(BlockFace face, int step) {
        return new Vector3(this.getX() + face.getXOffset() * step, this.getY() + face.getYOffset() * step, this.getZ() + face.getZOffset() * step);
    }

    public Vector3 getSide(BlockFace face) {
        return this.getSide(face, 1);
    }


    //-------------- 方向相关 ------------------
    public Vector3 up() {
        return up(1);
    }

    public Vector3 up(int step) {
        return getSide(BlockFace.UP, step);
    }

    public Vector3 down() {
        return down(1);
    }

    public Vector3 down(int step) {
        return getSide(BlockFace.DOWN, step);
    }

    public Vector3 north() {
        return north(1);
    }

    public Vector3 north(int step) {
        return getSide(BlockFace.NORTH, step);
    }

    public Vector3 south() {
        return south(1);
    }

    public Vector3 south(int step) {
        return getSide(BlockFace.SOUTH, step);
    }

    public Vector3 east() {
        return east(1);
    }

    public Vector3 east(int step) {
        return getSide(BlockFace.EAST, step);
    }

    public Vector3 west() {
        return west(1);
    }

    public Vector3 west(int step) {
        return getSide(BlockFace.WEST, step);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    //数学运算
    public int pointMultiply(Vector3 line) {
        return this.x * line.getX() + this.y * line.getY() + this.z * line.getZ();
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double lengthSquare() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    // 平面線性方程式(不考慮 Y 值)
    public double getEquationZ(Vector3 targetPoint, int xValue) {
        // z = ax + b;
        // 取得 a
        int xVector = targetPoint.getX() - this.getX();
        if (xVector == 0) {
            xVector = 1;
        }

        double a = (double) (targetPoint.getZ() - this.getZ()) / (double) xVector;

        // 取得 b
        double b = (double) targetPoint.getZ() - a * targetPoint.getX();

        return a * xValue + b;
    }

    // 平面線性方程式(不考慮 Y 值)
    public double getEquationX(Vector3 targetPoint, int zValue) {
        // z = ax + b;
        int xVector = targetPoint.getX() - this.getX();
        if (xVector == 0) {
            return this.getX();
        }

        // 取得 a
        double a = (double) (targetPoint.getZ() - this.getZ()) / (double) xVector;

        // 取得 b
        double b = (double) targetPoint.getZ() - a * targetPoint.getX();

        return (zValue - b) / a;
    }


    /**
     * 求垂直最短距离
     *
     * @param vector2
     * @return
     */
    public double getDistanceToMidLine(Vector3 vector2) {
        double distance = vector2.length() * Math.sin(this.getAngle(vector2));

        return distance;
    }

    public double getAngle(Vector3 vector2) {
        return Math.acos(this.pointMultiply(vector2) / this.length() / vector2.length());
    }

    /**
     * 求垂直交点到向量起点的距离
     *
     * @param vector2
     * @return
     */
    public double getDistanceOfProjection(Vector3 vector2) {
        return -((0 - vector2.getX()) * this.x + (0 - vector2.getY()) * this.y + (0 - vector2.getZ()) * this.z) * 1.0 / (this.x * this.x + this.y * this.y + this.z * this.z);
    }

    @Override
    public String toString() {
        return "Vector3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object ob) {
        if (ob instanceof Vector3) {
            Vector3 from = (Vector3) ob;
            return from.getX() == this.x && from.getY() == this.y && from.getZ() == this.getZ();
        }

        return false;
    }
}
