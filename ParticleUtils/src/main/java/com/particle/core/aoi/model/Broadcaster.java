package com.particle.core.aoi.model;

import com.particle.core.aoi.api.OnGameObjectSubscript;
import com.particle.core.aoi.api.OnGameObjectUnsubscript;

import java.net.InetSocketAddress;
import java.util.*;

public class Broadcaster {

    // 订阅者列表
    private Set<InetSocketAddress> subscribers = new HashSet<>();
    private List<InetSocketAddress> subscribersList = new ArrayList<>();

    // spawn回调
    private OnGameObjectSubscript onGameObjectSubscript;

    // despawn回调
    private OnGameObjectUnsubscript onGameObjectUnsubscript;

    private boolean forceUpdate = false;

    /**
     * 该生物可见玩家的白名单
     */
    private Set<InetSocketAddress> subscriberWhiteList = new HashSet<>();
    private Set<InetSocketAddress> subscriberWhiteListView = Collections.unmodifiableSet(subscriberWhiteList);
    private boolean enableWhiteList = false;

    public Broadcaster(OnGameObjectSubscript onGameObjectSubscript, OnGameObjectUnsubscript onGameObjectUnsubscript) {
        this.onGameObjectSubscript = onGameObjectSubscript;
        this.onGameObjectUnsubscript = onGameObjectUnsubscript;
    }

    // ----- 订阅列表操作 -----
    public Set<InetSocketAddress> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<InetSocketAddress> subscribers) {
        this.subscribers = subscribers;
    }

    public List<InetSocketAddress> getSubscribersList() {
        return subscribersList;
    }

    public void setSubscribersList(List<InetSocketAddress> subscribersList) {
        this.subscribersList = subscribersList;
    }

    // ----- 回调操作 -----
    public OnGameObjectSubscript getOnGameObjectSubscript() {
        return onGameObjectSubscript;
    }

    public void setOnGameObjectSubscript(OnGameObjectSubscript onGameObjectSubscript) {
        this.onGameObjectSubscript = onGameObjectSubscript;
    }

    public OnGameObjectUnsubscript getOnGameObjectUnsubscript() {
        return onGameObjectUnsubscript;
    }

    public void setOnGameObjectUnsubscript(OnGameObjectUnsubscript onGameObjectUnsubscript) {
        this.onGameObjectUnsubscript = onGameObjectUnsubscript;
    }

    // ----- 白名单操作 -----
    public Set<InetSocketAddress> getSubscriberWhiteList() {
        return subscriberWhiteList;
    }

    public void setSubscriberWhiteList(Set<InetSocketAddress> subscriberWhiteList) {
        this.subscriberWhiteList = subscriberWhiteList;
    }

    public Set<InetSocketAddress> getSubscriberWhiteListView() {
        return subscriberWhiteListView;
    }

    public boolean isEnableWhiteList() {
        return enableWhiteList;
    }

    public void setEnableWhiteList(boolean enableWhiteList) {
        this.enableWhiteList = enableWhiteList;
    }

    // ----- 强制刷新标识符 -----
    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
}
