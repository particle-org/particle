package com.particle.game.entity.attribute.virus;

import com.particle.core.ecs.module.BehaviorModule;

public class EntityVirusModule extends BehaviorModule {
    // 是否感染
    private boolean isInfectVirous;
    // 源病毒的entityId
    private long sourceEntityId;
    // 病毒感染距离
    private float infectDistance;
    // 感染的病毒值
    private int virusValue;
    // 感染的速度
    private int infectSpeed;
    // 最大感染病毒数
    private int maxVirusValue;
    // 刷新间隔，单位ms
    private int refreshInterval;
    // 刷新中毒值的时间
    private long lastRefreshTime;

    public boolean isInfectVirous() {
        return isInfectVirous;
    }

    public void setInfectVirous(boolean infectVirous) {
        isInfectVirous = infectVirous;
    }

    public long getSourceEntityId() {
        return sourceEntityId;
    }

    public void setSourceEntityId(long sourceEntityId) {
        this.sourceEntityId = sourceEntityId;
    }

    public float getInfectDistance() {
        return infectDistance;
    }

    public void setInfectDistance(float infectDistance) {
        this.infectDistance = infectDistance;
    }

    public int getVirusValue() {
        return virusValue;
    }

    public void setVirusValue(int virusValue) {
        this.virusValue = virusValue;
    }

    public void addVirusValue(int incValue) {
        this.virusValue = Math.min(this.virusValue + incValue, maxVirusValue);
    }

    public int getInfectSpeed() {
        return infectSpeed;
    }

    public void setInfectSpeed(int infectSpeed) {
        this.infectSpeed = infectSpeed;
    }

    public int getMaxVirusValue() {
        return maxVirusValue;
    }

    public void setMaxVirusValue(int maxVirusValue) {
        this.maxVirusValue = maxVirusValue;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public long getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(long lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }
}
