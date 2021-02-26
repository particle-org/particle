package com.particle.core.aoi.model;

import com.particle.core.aoi.container.SceneDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Scene {

    public static final Scene EMPTY_SCENE = new Scene("EmptyScene", 0, 0, 0, 0);

    // 场景名称
    private String name;

    // 场景中的方格节点
    private Map<Long, Grid> gridNodeMap = new ConcurrentHashMap<>();

    // 该场景的被动数据提供器，当区块被加载时启动并广播
    private List<SceneDataProvider> responsiveDataProvider = new ArrayList<>();

    // 该场景的主动数据提供器，用于主动加载区块并广播
    private List<SceneDataProvider> initiativeDataProvider = new ArrayList<>();

    // 游戏区间
    private int xMin;
    private int xMax;
    private int zMin;
    private int zMax;

    public Scene(String name, int xMin, int xMax, int zMin, int zMax) {
        this.name = name;
        this.xMin = xMin;
        this.xMax = xMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    /**
     * 缓存方格节点
     *
     * @param grid
     */
    public void putGridNode(Grid grid) {
        this.gridNodeMap.put(grid.getId(), grid);
    }

    /**
     * 获取方格节点
     *
     * @param id
     * @return
     */
    public Grid getGridNode(long id) {
        return this.gridNodeMap.get(id);
    }

    /**
     * 移除方格节点
     *
     * @param id
     */
    public Grid removeGridNode(long id) {
        return this.gridNodeMap.remove(id);
    }

    /**
     * 获取节点列表
     *
     * @return
     */
    public Map<Long, Grid> getGridNodeMap() {
        return Collections.unmodifiableMap(gridNodeMap);
    }

    /**
     * 获取场景名称
     *
     * @return
     */
    public String getName() {
        return name;
    }

    public int getxMin() {
        return xMin;
    }

    public int getxMax() {
        return xMax;
    }

    public int getzMin() {
        return zMin;
    }

    public int getzMax() {
        return zMax;
    }


    // ----- 场景管理器 -----

    /**
     * 注册被动数据提供器
     *
     * @param sceneDataProvider
     */
    public void registerSceneDataProvider(SceneDataProvider sceneDataProvider) {
        this.responsiveDataProvider.add(sceneDataProvider);
    }

    public List<SceneDataProvider> getResponsiveDataProvider() {
        return responsiveDataProvider;
    }
}
