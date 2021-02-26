package com.particle.model.ui.map;


import java.awt.*;

public class MapData {
    private long mapId;
    private long mapType;
    private byte scale;
    private int width;
    private int height;
    private int offsetX;
    private int offsetZ;

    // 地图对应世界实际中点坐标
    private int centerX;
    private int centerZ;
    private int radius;

    private Color[][] image;

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public long getMapType() {
        return mapType;
    }

    public void setMapType(long mapType) {
        this.mapType = mapType;
    }

    public byte getScale() {
        return scale;
    }

    public void setScale(byte scale) {
        this.scale = scale;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetZ() {
        return offsetZ;
    }

    public void setOffsetZ(int offsetZ) {
        this.offsetZ = offsetZ;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterZ() {
        return centerZ;
    }

    public void setCenterZ(int centerZ) {
        this.centerZ = centerZ;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Color[][] getImage() {
        return image;
    }

    public void setImage(Color[][] image) {
        this.image = image;
    }
}
