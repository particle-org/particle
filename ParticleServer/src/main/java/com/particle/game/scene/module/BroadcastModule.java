package com.particle.game.scene.module;

import com.particle.core.aoi.api.OnGameObjectSubscript;
import com.particle.core.aoi.api.OnGameObjectUnsubscript;
import com.particle.core.aoi.model.Broadcaster;
import com.particle.core.aoi.model.Grid;
import com.particle.core.aoi.model.Scene;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModuleBindSingleComponent;
import com.particle.game.scene.components.BroadcastComponent;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 消息广播组件
 */
public class BroadcastModule extends ECSModuleBindSingleComponent<BroadcastComponent> {

    private GameObject gameObject;

    /**
     * 初始化GameObject
     *
     * @param scene
     * @param onGameObjectSubscript
     * @param onGameObjectUnsubscript
     */
    public void initGameObject(Scene scene, OnGameObjectSubscript onGameObjectSubscript, OnGameObjectUnsubscript onGameObjectUnsubscript) {
        // 配置广播组件
        this.component.setOnGameObjectSubscript(onGameObjectSubscript);
        this.component.setOnGameObjectUnsubscript(onGameObjectUnsubscript);
    }

    public Broadcaster getBroadcaster() {
        return this.component;
    }

    public Grid getCurrentGrid() {
        return this.component.getCurrentGrid();
    }

    public void setCurrentGrid(Grid grid) {
        this.component.setCurrentGrid(grid);
    }

    public InetSocketAddress getAddress() {
        return this.component.getAddress();
    }

    public void setAddress(InetSocketAddress address) {
        this.component.setAddress(address);
    }

    /**
     * 获取广播列表
     */
    public List<InetSocketAddress> getBroadcastList() {
        return this.getBroadcaster().getSubscribersList();
    }

    /**
     * 增加白名单
     *
     * @param address
     */
    public void addWhiteList(InetSocketAddress address) {
        this.component.getSubscriberWhiteList().add(address);
    }

    /**
     * 移除白名单
     *
     * @param address
     */
    public void removeWhiteList(InetSocketAddress address) {
        this.component.getSubscriberWhiteList().remove(address);
    }

    /**
     * 设置白名单是否开启
     *
     * @param enableWhiteList
     */
    public void setEnableWhiteList(boolean enableWhiteList) {
        this.component.setEnableWhiteList(enableWhiteList);
    }

    public void requestForceUpdate() {
        this.component.setForceUpdate(true);
    }

    public void cancelForceUpdate() {
        this.component.setForceUpdate(false);
    }

    public boolean isForceUpdate() {
        return this.component.isForceUpdate();
    }

    @Override
    protected void onBindGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    protected Class<BroadcastComponent> getTypeClass() {
        return BroadcastComponent.class;
    }
}
