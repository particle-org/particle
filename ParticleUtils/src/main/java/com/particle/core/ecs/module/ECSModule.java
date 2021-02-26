package com.particle.core.ecs.module;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;

public abstract class ECSModule {

    /**
     * 获取Service绑定的组件
     *
     * @return
     */
    public abstract Class<? extends ECSComponent>[] requestComponents();

    /**
     * 绑定GameObject
     *
     * @param gameObject
     */
    protected abstract void bindGameObject(GameObject gameObject);
}
