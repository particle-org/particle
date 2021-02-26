package com.particle.core.ecs.module;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.system.ECSSystemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ECSModuleHandler<T extends ECSModule> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ECSModuleHandler.class);


    /**
     * ECSModuleHandler 缓存
     * <p>
     * 减少内存分配
     */
    private static final Map<Class, ECSModuleHandler> QUERY_CACHE = new HashMap<>();

    /**
     * 获取指定ECS组件的索引器
     *
     * @param clazz
     * @return
     */
    public static <T extends ECSModule> ECSModuleHandler<T> buildHandler(Class<T> clazz) {
        // 检查缓存，如果有缓存则直接返回
        ECSModuleHandler<T> ecsModuleHandler = QUERY_CACHE.get(clazz);
        if (ecsModuleHandler == null) {
            // 重新生成Query组件
            // 这里不同的class会生成不同的query组件，但是不同的query组件可能会有相同的index
            ecsModuleHandler = new ECSModuleHandler<>(clazz, ECSModuleMapper.getECSModuleIndex(clazz));

            QUERY_CACHE.put(clazz, ecsModuleHandler);
        }

        return ecsModuleHandler;
    }

    /**
     * 索引类
     */
    private final Class<T> moduleClass;

    /**
     * 组件索引
     */
    private final Integer moduleIndex;

    /**
     * 构造组件
     *
     * @param moduleClass
     * @param moduleIndex
     */
    private ECSModuleHandler(Class<T> moduleClass, Integer moduleIndex) {
        this.moduleClass = moduleClass;
        this.moduleIndex = moduleIndex;
    }

    /**
     * 查询组件
     *
     * @param gameObject
     * @return
     */
    public T getModule(GameObject gameObject) {
        // 获取容器
        AModuleContainer moduleContainer = gameObject.getModuleContainer();

        // 查询组件
        return (T) moduleContainer.getModule(this.moduleIndex);
    }

    /**
     * 绑定module
     *
     * @param gameObject
     */
    public T bindModule(GameObject gameObject) {
        // 获取容器
        AModuleContainer moduleContainer = gameObject.getModuleContainer();

        // 如果模组已经绑定，则直接返回
        T module = (T) moduleContainer.getModule(this.moduleIndex);
        if (module != null) {
            return module;
        }

        // 创建组件
        try {
            module = moduleClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Fail to create module {}!", moduleClass.getSimpleName(), e);

            throw new RuntimeException(e);
        }

        // 校验组件是否存在
        for (Class<? extends ECSComponent> requestComponent : module.requestComponents()) {
            ECSComponentHandler.buildHandler(requestComponent).getOrCreateComponent(gameObject);
        }

        // 绑定module
        module.bindGameObject(gameObject);
        moduleContainer.setModule(ECSModuleMapper.getECSModuleIndex(moduleClass), module);

        // 刷新Tick列表
        ECSSystemManager.buildECSSystemTickList(gameObject);

        return module;
    }

    public void importModule(GameObject gameObject, ECSModule ecsModule) {
        // 校验模组
        if (!ecsModule.getClass().equals(this.moduleClass)) {
            LOGGER.error("Fail to import module {} because of module type not match {}!", ecsModule.getClass(), this.moduleClass.getSimpleName());

            throw new RuntimeException();
        }

        // 获取容器
        AModuleContainer moduleContainer = gameObject.getModuleContainer();

        // 校验组件是否存在
        for (Class<? extends ECSComponent> requestComponent : ecsModule.requestComponents()) {
            ECSComponentHandler.buildHandler(requestComponent).getOrCreateComponent(gameObject);
        }

        // 绑定module
        ecsModule.bindGameObject(gameObject);
        moduleContainer.setModule(ECSModuleMapper.getECSModuleIndex(moduleClass), ecsModule);
    }

    /**
     * 移除module
     *
     * @param gameObject
     */
    public T removeModule(GameObject gameObject) {
        ECSModule ecsModule = gameObject.getModuleContainer().resetModule(this.moduleIndex);

        // 刷新Tick列表
        ECSSystemManager.buildECSSystemTickList(gameObject);

        return (T) ecsModule;
    }

    public boolean hasModule(GameObject gameObject) {
        return gameObject.getModuleContainer().hasModule(this.moduleIndex);
    }

    public Class<T> getModuleClass() {
        return moduleClass;
    }

    public Integer getModuleIndex() {
        return moduleIndex;
    }
}
