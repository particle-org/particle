package com.particle.core.ecs.component;

import com.particle.core.ecs.GameObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ECSComponentHandler<T extends ECSComponent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ECSComponentHandler.class);

    /**
     * ECSComponentHandler 缓存
     * <p>
     * 减少内存分配
     */
    private static final Map<Class, ECSComponentHandler> QUERY_CACHE = new HashMap<>();

    /**
     * 获取指定ECS组件的索引器
     *
     * @param clazz
     * @return
     */
    public static <T extends ECSComponent> ECSComponentHandler<T> buildHandler(Class<T> clazz) {
        // 检查缓存，如果有缓存则直接返回
        ECSComponentHandler<T> ecsComponentHandler = QUERY_CACHE.get(clazz);
        if (ecsComponentHandler == null) {
            // 重新生成Query组件
            // 这里不同的class会生成不同的query组件，但是不同的query组件可能会有相同的index
            ecsComponentHandler = new ECSComponentHandler<>(clazz, ECSComponentMapper.getECSComponentIndex(clazz));

            QUERY_CACHE.put(clazz, ecsComponentHandler);
        }

        return ecsComponentHandler;
    }

    /**
     * 索引类
     */
    private final Class<T> componentClass;

    /**
     * 组件索引
     */
    private final Integer componentIndex;

    /**
     * 构造组件
     *
     * @param componentClass
     * @param componentIndex
     */
    private ECSComponentHandler(Class<T> componentClass, Integer componentIndex) {
        this.componentClass = componentClass;
        this.componentIndex = componentIndex;
    }

    public T getOrCreateComponent(GameObject gameObject) {
        // 获取容器
        AComponentContainer componentContainer = gameObject.getComponentContainer();

        // 查询组件
        T component = (T) componentContainer.getComponent(this.componentIndex);

        // 检查组件是否存在
        if (component == null) {
            try {
                component = componentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Fail to create component {}!", componentClass.getSimpleName(), e);

                throw new RuntimeException(e);
            }

            componentContainer.setComponent(this.componentIndex, component);
        }

        return component;
    }

    /**
     * 查询组件
     *
     * @param gameObject
     * @return
     */
    public T getComponent(GameObject gameObject) {
        // 获取容器
        AComponentContainer componentContainer = gameObject.getComponentContainer();

        // 查询组件
        return (T) componentContainer.getComponent(this.componentIndex);
    }

    /**
     * 设置组件
     *
     * @param gameObject
     */
    public T addComponent(GameObject gameObject) {
        // 获取容器
        AComponentContainer componentContainer = gameObject.getComponentContainer();

        // 如果组件已经绑定，则直接返回
        if (componentContainer.hasComponent(this.componentIndex)) {
            return (T) componentContainer.getComponent(this.componentIndex);
        }

        // 创建组件
        T component = null;
        try {
            component = componentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Fail to create component {}!", componentClass.getSimpleName(), e);

            throw new RuntimeException(e);
        }

        // 配置组件
        componentContainer.setComponent(this.componentIndex, component);

        return component;
    }

    /**
     * 导入组件
     *
     * @param gameObject
     * @return
     */
    public void importComponent(GameObject gameObject, ECSComponent component) {
        if (!component.getClass().equals(this.componentClass)) {
            LOGGER.error("Fail to import component {} because of component type not match {}!", component.getClass(), componentClass.getSimpleName());

            throw new RuntimeException();
        }

        // 获取容器
        AComponentContainer componentContainer = gameObject.getComponentContainer();

        // 配置组件
        componentContainer.setComponent(this.componentIndex, component);
    }

    /**
     * 清除组件
     *
     * @param gameObject
     */
    public void removeComponent(GameObject gameObject) {
        gameObject.getComponentContainer().resetComponent(this.componentIndex);
    }

    /**
     * 判断组件是否存在
     *
     * @param gameObject
     * @return
     */
    public boolean hadComponent(GameObject gameObject) {
        return gameObject.getComponentContainer().hasComponent(this.componentIndex);
    }
}
