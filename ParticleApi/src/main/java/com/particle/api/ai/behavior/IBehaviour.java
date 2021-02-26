package com.particle.api.ai.behavior;

import com.particle.model.entity.Entity;

public interface IBehaviour {

    /**
     * 节点初始化后的操作
     */
    void onInitialize();

    /**
     * Tick决策树的操作
     *
     * @param entity
     * @return
     */
    EStatus tick(Entity entity);

    /**
     * 子节点运行结束后调用
     *
     * @param entity
     * @param status
     */
    void onTicked(Entity entity, EStatus status);

    /**
     * 节点关闭后调用
     */
    void onRelease();

    /**
     * 配置节点
     *
     * @param key
     * @param val
     */
    void config(String key, Object val);
}
