package com.particle.game.scene.components;

import com.particle.core.aoi.model.Broadcaster;
import com.particle.core.aoi.model.Grid;
import com.particle.core.ecs.component.ECSComponent;

import java.net.InetSocketAddress;

/**
 * 标识GameObject可以被其他GameObject订阅
 * AOI托管GameObject时会初始化该组件
 */
public class BroadcastComponent extends Broadcaster implements ECSComponent {

    /**
     * 当前节点
     */
    private Grid currentGrid;

    /**
     * 当前地址
     */
    private InetSocketAddress address;

    public BroadcastComponent() {
        super((object -> false), (object -> false));
    }

    // 节点操作
    public Grid getCurrentGrid() {
        return currentGrid;
    }

    public void setCurrentGrid(Grid currentGrid) {
        this.currentGrid = currentGrid;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

}
