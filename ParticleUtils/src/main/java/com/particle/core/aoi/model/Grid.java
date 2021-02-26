package com.particle.core.aoi.model;

import com.particle.core.aoi.container.GridDataContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * AOI网格节点
 */
public class Grid {

    private static final Logger LOGGER = LoggerFactory.getLogger(Grid.class);

    public static final Grid EMPTY_GRID = new Grid(Scene.EMPTY_SCENE, 0, 0);

    // 节点宽度
    public static final int GRID_WIDTH = 16;

    // 当前Grid所处的scene
    private Scene scene;

    // 节点id
    private long id;

    // 节点x坐标
    private int x;

    // 节点z坐标
    private int z;

    // 节点数据容器
    private GridDataContainer gridDataContainer = new GridDataContainer();

    // 节点中的订阅者
    private Set<InetSocketAddress> subscribers = new HashSet<>();

    public Grid(Scene scene, int x, int z) {
        this.scene = scene;
        this.x = x;
        this.z = z;

        this.id = Grid.buildIndex(x, z);
    }

    public Scene getScene() {
        return scene;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public long getId() {
        return id;
    }

    // ----- subscriber 操作 -----
    public boolean addSubscriber(InetSocketAddress address) {
        return this.subscribers.add(address);
    }

    public boolean removeSubscriber(InetSocketAddress address) {
        return this.subscribers.remove(address);
    }

    public Set<InetSocketAddress> getSubscribers() {
        return Collections.unmodifiableSet(this.subscribers);
    }

    public int getSubscribersSize() {
        return this.subscribers.size();
    }

    // ----- index 构建 -----

    public static long buildIndex(int x, int z) {
        return (((long) x) << 32) | (z & 0xFFFFFFFFL);
    }

    // ----- 节点数据操作 ---
    public GridDataContainer getGridDataContainer() {
        return gridDataContainer;
    }
}
