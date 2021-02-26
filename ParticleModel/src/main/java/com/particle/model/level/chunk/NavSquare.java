package com.particle.model.level.chunk;

import com.particle.model.math.Vector3;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NavSquare {
    private int xMin;
    private int zMin;
    private int xMax;
    private int zMax;
    private int y;

    private List<NavSquare> connectTo = new LinkedList<>();
    private List<NavSquare> connectFrom = new LinkedList<>();

    public int getxMin() {
        return xMin;
    }

    public void setxMin(int xMin) {
        this.xMin = xMin;
    }

    public int getzMin() {
        return zMin;
    }

    public void setzMin(int zMin) {
        this.zMin = zMin;
    }

    public int getxMax() {
        return xMax;
    }

    public void setxMax(int xMax) {
        this.xMax = xMax;
    }

    public int getzMax() {
        return zMax;
    }

    public void setzMax(int zMax) {
        this.zMax = zMax;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Vector3 getCenter() {
        return new Vector3((this.xMin + this.xMax) >> 1, this.y, (this.zMin + this.zMax) >> 1);
    }

    public boolean removeConnectTo(NavSquare connectToSquare) {
        return connectTo.remove(connectToSquare);
    }

    public void removeAllConnectionTo() {
        this.connectTo.clear();
    }

    public void addConnectTo(NavSquare connectToSquare) {
        if (!this.connectTo.contains(connectToSquare)) {
            this.connectTo.add(connectToSquare);
        }
    }

    public boolean removeConnectFrom(NavSquare connectToSquare) {
        return connectFrom.remove(connectToSquare);
    }

    public void addConnectFrom(NavSquare connectFromSquare) {
        if (!this.connectFrom.contains(connectFromSquare)) {
            this.connectFrom.add(connectFromSquare);
        }
    }

    public List<NavSquare> getConnectTo() {
        return Collections.unmodifiableList(this.connectTo);
    }

    public List<NavSquare> getConnectFrom() {
        return Collections.unmodifiableList(this.connectFrom);
    }

    @Override
    public String toString() {
        return String.format("xMin:%d xMax:%d zMin:%d zMax:%d y:%d", xMin, xMax, zMin, zMax, y);
    }
}
