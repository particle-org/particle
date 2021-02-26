package com.particle.core.aoi.model;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * 订阅者
 */
public class Subscriber {

    // 所在场景
    private Scene scene;

    // 订阅者所在网格
    private Grid baseGrid;
    // 订阅网格
    private Set<Grid> subscriptGrids = new HashSet<>();
    // 加载半径
    private int loadRadius;
    // 卸载半径
    private int unloadRadius;
    // 订阅者通信地址
    private InetSocketAddress address;

    public void update(Scene scene, int loadRadius, int unloadRadius, InetSocketAddress address) {
        this.scene = scene;
        this.baseGrid = Grid.EMPTY_GRID;
        this.loadRadius = loadRadius;
        this.unloadRadius = unloadRadius;
        this.address = address;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Grid getBaseGrid() {
        return baseGrid;
    }

    public void setBaseGrid(Grid baseGrid) {
        this.baseGrid = baseGrid;
    }

    public Set<Grid> getSubscriptGrids() {
        return subscriptGrids;
    }

    public void setSubscriptGrids(Set<Grid> subscriptGrids) {
        this.subscriptGrids = subscriptGrids;
    }

    public int getLoadRadius() {
        return loadRadius;
    }

    public void setLoadRadius(int loadRadius) {
        this.loadRadius = loadRadius;
    }

    public int getUnloadRadius() {
        return unloadRadius;
    }

    public void setUnloadRadius(int unloadRadius) {
        this.unloadRadius = unloadRadius;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }
}
