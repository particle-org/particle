package com.particle.game.scene.module;

import com.particle.core.aoi.model.Grid;
import com.particle.core.aoi.model.Scene;
import com.particle.core.aoi.model.Subscriber;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.scene.components.GridBinderComponent;
import com.particle.game.scene.components.SubscriptComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class SubscriberModule extends ECSModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberModule.class);

    private SubscriptComponent subscriptComponent;

    /**
     * 玩家游戏版本号，有时候发包需要在业务层判断
     */
    private int clientVersion = 0;

    /**
     * 初始化GameObject
     *
     * @param scene
     * @param loadRadius
     * @param unloadRadius
     */
    public void changeScene(Scene scene, int loadRadius, int unloadRadius, InetSocketAddress address) {
        // 初始化组件
        this.subscriptComponent.setScene(scene);
        this.subscriptComponent.setLoadRadius(loadRadius);
        this.subscriptComponent.setUnloadRadius(unloadRadius);
        this.subscriptComponent.setAddress(address);
    }

    public void leaveScene() {
        this.subscriptComponent.setScene(null);
        this.subscriptComponent.setLoadRadius(0);
        this.subscriptComponent.setUnloadRadius(0);
        this.subscriptComponent.setAddress(null);
    }

    public Subscriber getSubscriber() {
        return this.subscriptComponent;
    }

    public void setLoadRadius(int loadRadius) {
        this.subscriptComponent.setLoadRadius(loadRadius);
    }

    public void setUnloadRadius(int unloadRadius) {
        this.subscriptComponent.setUnloadRadius(unloadRadius);
    }

    public int getLoadRadius() {
        return this.subscriptComponent.getLoadRadius();
    }

    public int getUnloadRadius() {
        return this.subscriptComponent.getUnloadRadius();
    }


    public InetSocketAddress getAddress() {
        return this.subscriptComponent.getAddress();
    }

    public void clearSubscript() {
        // 取消订阅节点
        for (Grid node : this.subscriptComponent.getSubscriptGrids()) {
            // 移除订阅缓存
            node.removeSubscriber(this.subscriptComponent.getAddress());
        }

        this.subscriptComponent.getSubscriptGrids().clear();
    }

    public void setClientVersion(int clientVersion) {
        this.clientVersion = clientVersion;
    }

    public int getClientVersion() {
        return clientVersion;
    }

    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return new Class[]{SubscriptComponent.class, GridBinderComponent.class};
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.subscriptComponent = ECSComponentHandler.buildHandler(SubscriptComponent.class).getOrCreateComponent(gameObject);
    }
}
