package com.particle.api.entity;

import com.particle.model.entity.Entity;
import com.particle.model.entity.component.ECSComponent;

public interface ECSComponentServiceAPI {

    /**
     * 判断Entity是否拥有组件
     *
     * @param entity
     * @param id
     * @return
     */
    boolean hasComponent(Entity entity, int id);

    /**
     * 设置组件
     *
     * @param entity        待设置生物
     * @param ecsComponents 组件列表
     */
    void setComponents(Entity entity, ECSComponent[] ecsComponents);

    /**
     * 设置组件
     *
     * @param entity       待设置生物
     * @param ecsComponent 组件列表
     */
    void setComponent(Entity entity, ECSComponent ecsComponent);

    /**
     * 移除组件
     *
     * @param entity 待设置生物
     * @param id     组件ID
     */
    void removeComponent(Entity entity, int id);

    /**
     * 获取组件
     *
     * @param entity
     * @param id
     * @param clazz
     * @param <T>
     * @return
     */
    <T extends ECSComponent> T getComponent(Entity entity, int id, Class<T> clazz);
}
