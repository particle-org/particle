package com.particle.model.math;

public class Vector3f {
    private float x;
    private float y;
    private float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3 position) {
        this(position.getX(), position.getY(), position.getZ());
    }

    public Vector3f(Vector3f position) {
        this(position.getX(), position.getY(), position.getZ());
    }

    //-------------- get and set ---------


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public int getFloorX() {
        return MathUtils.floorFloat(this.x);
    }

    public int getFloorY() {
        return MathUtils.floorFloat(this.y);
    }

    public int getFloorZ() {
        return MathUtils.floorFloat(this.z);
    }

    //-------------- 四则运算 -------------
    public Vector3f add(float x, float y, float z) {
        return new Vector3f(this.x + x, this.y + y, this.z + z);
    }

    public Vector3f add(Vector3f vector3f) {
        return new Vector3f(this.x + vector3f.getX(), this.y + vector3f.getY(), this.z + vector3f.getZ());
    }

    public Vector3f add(Vector3 position) {
        return new Vector3f(this.x + position.getX(), this.y + position.getY(), this.z + position.getZ());
    }

    public Vector3f subtract(Vector3f vector3f) {
        return this.add(-vector3f.getX(), -vector3f.getY(), -vector3f.getZ());
    }

    public Vector3f subtract(float x, float y, float z) {
        return this.add(-x, -y, -z);
    }

    public void changeAdd(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public Vector3f multiply(float number) {
        return new Vector3f(this.x * number, this.y * number, this.z * number);
    }

    public Vector3f divide(float number) {
        return new Vector3f(this.x / number, this.y / number, this.z / number);
    }

    public Vector3 toVector3() {
        return new Vector3(this.getFloorX(), this.getFloorY(), this.getFloorZ());
    }

    //----------- 空间运算 ---------------
    public Vector3f revert() {
        return new Vector3f(-this.x, -this.y, -this.z);
    }

    public boolean hasLength() {
        return this.x != 0 || this.y != 0 || this.z != 0;
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public float lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector3f normalize() {
        float len = this.lengthSquared();
        if (len > 0) {
            return this.divide((float) Math.sqrt(len));
        }
        return new Vector3f(0, 0, 0);
    }

    public boolean isZero() {
        return this.x == 0 && this.y == 0 && this.z == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Vector3f) {
            Vector3f vector3f = (Vector3f) obj;
            return vector3f.getX() == getX() &&
                    vector3f.getY() == getY() &&
                    vector3f.getZ() == getZ();
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Vector3d(x=" + this.x + ",y=" + this.y + ",z=" + this.z + ")";
    }

    @Override
    public Vector3f clone() {
        return new Vector3f(this.x, this.y, this.z);
    }
}
