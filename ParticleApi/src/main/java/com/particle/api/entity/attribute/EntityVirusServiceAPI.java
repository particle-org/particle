package com.particle.api.entity.attribute;

import com.particle.model.entity.Entity;

public interface EntityVirusServiceAPI {
    /**
     * 生物是否感染病毒
     *
     * @param entity 操作的生物
     */
    boolean isInfectVirus(Entity entity);

    /**
     * 生物感染病毒
     *
     * @param entity          操作的生物
     * @param sourceEntityId  源病毒的entityId
     * @param infectDistance  病毒感染距离
     * @param initVirusValue  初始感染值
     * @param refreshInterval 刷新间隔
     * @param infectSpeed     每隔刷新间隔增加的感染值
     * @param maxVirusValue   最大感染病毒数
     */
    void infectVirus(Entity entity, long sourceEntityId, float infectDistance, int initVirusValue, int refreshInterval, int infectSpeed, int maxVirusValue);

    /**
     * 清除生物感染病毒
     *
     * @param entity 操作的生物
     */
    void clearVirus(Entity entity);

    /**
     * 获得生物的病毒值
     *
     * @param entity 操作的生物
     */
    int getVirusValue(Entity entity);
}