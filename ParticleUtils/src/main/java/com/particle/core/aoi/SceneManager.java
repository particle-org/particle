package com.particle.core.aoi;

import com.particle.core.aoi.container.SceneDataProvider;
import com.particle.core.aoi.model.Broadcaster;
import com.particle.core.aoi.model.Grid;
import com.particle.core.aoi.model.Scene;
import com.particle.core.aoi.model.Subscriber;

import java.net.InetSocketAddress;
import java.util.*;

public class SceneManager {
    /**
     * 单例对象
     */
    private static final SceneManager INSTANCE = new SceneManager();

    private Map<String, Scene> sceneMap = new HashMap<>();

    /**
     * 获取单例
     */
    public static SceneManager getInstance() {
        return SceneManager.INSTANCE;
    }

    private SceneManager() {
    }

    /**
     * 添加一个场景
     *
     * @param name
     */
    public Scene createScene(String name, int xMin, int xMax, int zMin, int zMax) {
        Scene scene = new Scene(name, xMin, xMax, zMin, zMax);
        this.sceneMap.put(name, scene);

        return scene;
    }

    /**
     * 移除一个场景
     *
     * @param scene
     */
    public void destroyScene(Scene scene) {
        this.sceneMap.remove(scene.getName());
    }

    /**
     * 获取一个场景
     *
     * @param name
     */
    public Scene getScene(String name) {
        return this.sceneMap.get(name);
    }

    /**
     * 获取Grid
     *
     * @param scene
     * @param x
     * @param z
     * @return
     */
    public Grid getGridNode(Scene scene, int x, int z, boolean createIfNotExist) {
        // 获取grid节点
        Grid grid = scene.getGridNode(Grid.buildIndex(x, z));

        // 若没有该节点，则创建
        if (grid == null && createIfNotExist) {
            grid = new Grid(scene, x, z);
            scene.putGridNode(grid);

            // 回调激活
            for (SceneDataProvider sceneDataProvider : scene.getResponsiveDataProvider()) {
                sceneDataProvider.onGridActive(grid);
            }
        }

        return grid;
    }

    /**
     * @param scene
     * @param x
     * @param z
     * @return
     */
    public Grid recycleGridNode(Scene scene, int x, int z) {
        // 获取grid节点
        Grid grid = scene.removeGridNode(Grid.buildIndex(x, z));

        // 若没有该节点，则创建
        if (grid != null) {
            // 回调激活
            for (SceneDataProvider sceneDataProvider : scene.getResponsiveDataProvider()) {
                sceneDataProvider.onGridInactive(grid);
            }
        }

        return grid;
    }

    /**
     * 刷新订阅者订阅的区块列表
     *
     * @param subscriber
     * @param newGrid
     */
    public void refreshSubscriptGrids(Subscriber subscriber, Grid newGrid) {
        // 获取玩家所在的scene
        Scene scene = subscriber.getScene();

        // 玩家不再场景中
        if (scene == null) return;

        // 判断是否需要刷新
        Grid baseGrid = subscriber.getBaseGrid();
        if (baseGrid == newGrid) return;

        subscriber.setBaseGrid(newGrid);

        // 读取玩家配置
        int loadRadius = subscriber.getLoadRadius();
        int unloadRadius = subscriber.getUnloadRadius();

        // 计算当前Index
        int indexX = newGrid.getX();
        int indexZ = newGrid.getZ();

        // 计算扫描区域
        int areaXMin = Math.max(indexX - loadRadius, scene.getxMin());
        int areaXMax = Math.min(indexX + loadRadius, scene.getxMax());
        int areaZMin = Math.max(indexZ - loadRadius, scene.getzMin());
        int areaZMax = Math.min(indexZ + loadRadius, scene.getzMax());

        // 获取已经订阅的节点
        Set<Grid> subscriptNodes = new HashSet<>(subscriber.getSubscriptGrids());

        // 扫描待订阅节点，处理玩家订阅信息
        for (int x = areaXMin; x <= areaXMax; x++) {
            for (int z = areaZMin; z <= areaZMax; z++) {

                // --------------------- 处理区块缓存 ---------------------
                // 查询节点
                Grid grid = this.getGridNode(scene, x, z, true);

                // 检测是否已经加载
                boolean hasLoaded = subscriptNodes.remove(grid);

                // 如果没有加载，则加载
                if (!hasLoaded) {
                    // 缓存订阅信息
                    grid.addSubscriber(subscriber.getAddress());
                    subscriber.getSubscriptGrids().add(grid);

                    // 回调
                    for (SceneDataProvider sceneDataProvider : scene.getResponsiveDataProvider()) {
                        sceneDataProvider.onSubscribed(grid, subscriber);
                    }
                }
            }
        }

        // 取消订阅过远的节点，该步骤可以保证仅需要处理radius范围以外的区块
        for (Grid grid : subscriptNodes) {
            // 检测是否需要移除
            if (Math.abs(indexX - grid.getX()) > unloadRadius || Math.abs(indexZ - grid.getZ()) > unloadRadius) {
                // 移除订阅缓存
                grid.removeSubscriber(subscriber.getAddress());
                subscriber.getSubscriptGrids().remove(grid);

                // 回调
                for (SceneDataProvider sceneDataProvider : scene.getResponsiveDataProvider()) {
                    sceneDataProvider.onUnsubscribed(grid, subscriber);
                }
            }
        }
    }

    /**
     * 清理区块广播列表
     *
     * @param subscriber
     */
    public void clearSubscriptData(Subscriber subscriber) {
        Set<Grid> subscriptGrids = subscriber.getSubscriptGrids();
        for (Grid subscriptGrid : subscriptGrids) {
            subscriptGrid.removeSubscriber(subscriber.getAddress());
        }
    }

    /**
     * 刷新广播者广播列表
     *
     * @param broadcaster
     */
    public void refreshSubscribers(Broadcaster broadcaster, Set<InetSocketAddress> subscribers) {
        // 过滤白名单
        if (broadcaster.isEnableWhiteList()) {
            Set<InetSocketAddress> resultSet = new HashSet<>();
            for (InetSocketAddress inetSocketAddress : subscribers) {
                if (broadcaster.getSubscriberWhiteList().contains(inetSocketAddress)) {
                    resultSet.add(inetSocketAddress);
                }
            }
            subscribers = resultSet;
        }

        // 计算需要增加的gameobject
        Set<InetSocketAddress> newSubscribers = new HashSet<>(subscribers);
        newSubscribers.removeAll(broadcaster.getSubscribers());

        // 执行增加回调
        for (InetSocketAddress newObj : newSubscribers) {
            broadcaster.getOnGameObjectSubscript().handle(newObj);
        }

        // 计算需要移除的gameobject
        Set<InetSocketAddress> oldSubscribers = new HashSet<>(broadcaster.getSubscribers());
        oldSubscribers.removeAll(subscribers);

        // 执行移除回调
        for (InetSocketAddress oldObj : oldSubscribers) {
            broadcaster.getOnGameObjectUnsubscript().handle(oldObj);
        }

        // 更新订阅列表
        broadcaster.setSubscribers(subscribers);
        broadcaster.setSubscribersList(new ArrayList<>(subscribers));
    }

    /**
     * 清理区块广播列表
     *
     * @param broadcaster
     */
    public void clearSubscriberData(Broadcaster broadcaster) {
        for (InetSocketAddress subscriber : broadcaster.getSubscribers()) {
            broadcaster.getOnGameObjectUnsubscript().handle(subscriber);
        }

        broadcaster.getSubscribers().clear();
        broadcaster.getSubscribersList().clear();
    }
}
