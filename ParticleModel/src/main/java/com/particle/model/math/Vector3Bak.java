package com.particle.model.math;

public class Vector3Bak implements Cloneable {
    protected int x;
    protected int y;
    protected int z;

    public Vector3Bak(Vector3f vector3f) {
        this((int) Math.floor(vector3f.getX()),
                (int) Math.floor(vector3f.getY()),
                (int) Math.floor(vector3f.getZ()));
    }

    public Vector3Bak(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3Bak setComponents(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Vector3d add(double x) {
        return this.add(x, 0, 0);
    }

    public Vector3d add(double x, double y) {
        return this.add(x, y, 0);
    }

    public Vector3d add(double x, double y, double z) {
        return new Vector3d(this.x + x, this.y + y, this.z + z);
    }

    public Vector3d add(Vector3d x) {
        return new Vector3d(this.x + x.getX(), this.y + x.getY(), this.z + x.getZ());
    }

    public Vector3d subtract(double x) {
        return this.subtract(x, 0, 0);
    }

    public Vector3d subtract(double x, double y) {
        return this.subtract(x, y, 0);
    }

    public Vector3d subtract(double x, double y, double z) {
        return this.add(-x, -y, -z);
    }

    public Vector3d subtract(Vector3d x) {
        return this.add(-x.getX(), -x.getY(), -x.getZ());
    }

    public Vector3Bak add(int x) {
        return this.add(x, 0, 0);
    }

    public Vector3Bak add(int x, int y) {
        return this.add(x, y, 0);
    }

    public Vector3Bak add(int x, int y, int z) {
        return new Vector3Bak(this.x + x, this.y + y, this.z + z);
    }

    public Vector3Bak add(Vector3Bak x) {
        return new Vector3Bak(this.x + x.getX(), this.y + x.getY(), this.z + x.getZ());
    }

    public Vector3Bak subtract() {
        return this.subtract(0, 0, 0);
    }

    public Vector3Bak subtract(int x) {
        return this.subtract(x, 0, 0);
    }

    public Vector3Bak subtract(int x, int y) {
        return this.subtract(x, y, 0);
    }

    public Vector3Bak subtract(int x, int y, int z) {
        return this.add(-x, -y, -z);
    }

    public Vector3Bak subtract(Vector3Bak x) {
        return this.add(-x.getX(), -x.getY(), -x.getZ());
    }

    public Vector3Bak multiply(int number) {
        return new Vector3Bak(this.x * number, this.y * number, this.z * number);
    }

    public Vector3Bak divide(int number) {
        return new Vector3Bak(this.x / number, this.y / number, this.z / number);
    }

    public Vector3Bak getSide(BlockFace face) {
        return this.getSide(face, 1);
    }

    public Vector3Bak getSide(BlockFace face, int step) {
        return new Vector3Bak(this.getX() + face.getXOffset() * step, this.getY() + face.getYOffset() * step, this.getZ() + face.getZOffset() * step);
    }

    public Vector3Bak up() {
        return up(1);
    }

    public Vector3Bak up(int step) {
        return getSide(BlockFace.UP, step);
    }

    public Vector3Bak down() {
        return down(1);
    }

    public Vector3Bak down(int step) {
        return getSide(BlockFace.DOWN, step);
    }

    public Vector3Bak north() {
        return north(1);
    }

    public Vector3Bak north(int step) {
        return getSide(BlockFace.NORTH, step);
    }

    public Vector3Bak south() {
        return south(1);
    }

    public Vector3Bak south(int step) {
        return getSide(BlockFace.SOUTH, step);
    }

    public Vector3Bak east() {
        return east(1);
    }

    public Vector3Bak east(int step) {
        return getSide(BlockFace.EAST, step);
    }

    public Vector3Bak west() {
        return west(1);
    }

    public Vector3Bak west(int step) {
        return getSide(BlockFace.WEST, step);
    }

    public double distance(Vector3d pos) {
        return Math.sqrt(this.distanceSquared(pos));
    }

    public double distance(Vector3Bak pos) {
        return Math.sqrt(this.distanceSquared(pos));
    }

    public double distanceSquared(Vector3d pos) {
        return distanceSquared(pos.x, pos.y, pos.z);
    }

    public double distanceSquared(Vector3Bak pos) {
        return distanceSquared(pos.x, pos.y, pos.z);
    }

    public double distanceSquared(double x, double y, double z) {
        return Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2) + Math.pow(this.z - z, 2);
    }

    @Override
    public boolean equals(Object ob) {
        if (ob == null) return false;
        if (ob == this) return true;

        if (!(ob instanceof Vector3Bak)) return false;

        return
                this.x == ((Vector3Bak) ob).x &&
                        this.y == ((Vector3Bak) ob).y &&
                        this.z == ((Vector3Bak) ob).z;
    }

    @Override
    public final int hashCode() {
        return (x ^ (z << 12)) ^ (y << 24);
    }

    @Override
    public String toString() {
        return "BlockPosition(level=" + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ")";
    }

    @Override
    public Vector3Bak clone() {
        try {
            return (Vector3Bak) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public Vector3Bak asVector3() {
        return new Vector3Bak(this.x, this.y, this.z);
    }

    public Vector3fBak asVector3f() {
        return new Vector3fBak(this.x, this.y, this.z);
    }
}
