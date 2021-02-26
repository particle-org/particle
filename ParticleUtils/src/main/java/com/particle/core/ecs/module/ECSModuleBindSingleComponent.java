package com.particle.core.ecs.module;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ECSModuleBindSingleComponent<T extends ECSComponent> extends ECSModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ECSModuleBindSingleComponent.class);

    protected T component;

    /**
     * 返回该业务绑定组件的class，getClass一定拿到最终子类，当作component
     *
     * @return
     */
    @Override
    public final Class<T>[] requestComponents() {
        return new Class[]{this.getTypeClass()};
    }

    /**
     * 绑定GameObject
     *
     * @param gameObject
     */
    @Override
    protected final void bindGameObject(GameObject gameObject) {
        this.component = ECSComponentHandler.buildHandler(getTypeClass()).getComponent(gameObject);

        if (this.component == null) {
            LOGGER.warn("Fail to bind game object, because component not founded!");
        }

        this.onBindGameObject(gameObject);
    }

    /**
     * 初始化，在GameObject绑定后执行
     */
    protected void onBindGameObject(GameObject gameObject) {
    }

    protected abstract Class<T> getTypeClass();
}
