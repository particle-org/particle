package com.particle.model.math;

import com.particle.model.utils.Pair;

public class Rect4d {

    private int minX;

    private int maxX;

    private int minZ;

    private int maxZ;

    public Rect4d(int minX, int maxX, int minZ, int maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public Rect4d() {

    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public void setMinZ(int minZ) {
        this.minZ = minZ;
    }

    public void setMaxZ(int maxZ) {
        this.maxZ = maxZ;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxZ() {
        return maxZ;
    }

    /**
     * 判断是否在里面
     *
     * @param x
     * @param z
     * @return
     */
    public boolean isInner(int x, int z) {
        if (x >= minX && x <= maxX && z >= minZ && z <= maxZ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取中心位置
     *
     * @return
     */
    public Pair<Integer, Integer> getInner() {
        return new Pair<>((this.minX + this.maxX) / 2, (this.minZ + this.maxZ) / 2);
    }

    @Override
    public String toString() {
        return "Rect4d{" +
                "minX=" + minX +
                ", maxX=" + maxX +
                ", minZ=" + minZ +
                ", maxZ=" + maxZ +
                '}';
    }
}
