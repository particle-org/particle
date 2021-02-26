package com.particle.core.ecs.system;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;

public interface ECSSystemFactory<T extends ECSSystem> {

    /**
     * 获取Service绑定的组件
     *
     * @return
     */
    Class<? extends ECSModule>[] getRequestServices();

    /**
     * 绑定GameObject
     *
     * @param gameObject
     */
    T buildECSSystem(GameObject gameObject);

    default T getOrBuildECSSystem(GameObject gameObject) {
        T ecsSystem = gameObject.getSystemContainer().getECSSystem(this.getSystemClass());
        if (ecsSystem == null) {
            return this.buildECSSystem(gameObject);
        } else {
            return ecsSystem;
        }
    }

    /**
     * 获取系统的类
     *
     * @return
     */
    Class<T> getSystemClass();

}
