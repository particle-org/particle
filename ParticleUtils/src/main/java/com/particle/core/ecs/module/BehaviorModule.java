package com.particle.core.ecs.module;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;

public abstract class BehaviorModule extends ECSModule implements ECSComponent {

    private GameObject gameObject;

    private Class<? extends ECSComponent>[] requestComponents;

    /**
     * 返回该业务绑定组件的class，getClass一定拿到最终子类，当作component
     *
     * @return
     */
    @Override
    public final Class<? extends ECSComponent>[] requestComponents() {
        if (this.requestComponents == null) {
            this.requestComponents = new Class[]{this.getClass()};
        }
        return this.requestComponents;
    }

    /**
     * 绑定GameObject
     * <p>
     * 因为Service自己就是Component，所以不需要绑定
     *
     * @param gameObject
     */
    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    protected GameObject getOwn() {
        return this.gameObject;
    }
}
