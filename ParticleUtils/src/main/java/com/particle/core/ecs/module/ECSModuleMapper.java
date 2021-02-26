package com.particle.core.ecs.module;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ECSModuleMapper {

    /**
     * 组件ID生成器
     */
    private static final AtomicInteger COMPONENT_ID_GENERATE = new AtomicInteger(1);

    /**
     * 不同Class到对应组件Class的缓存
     * <p>
     * 主要用于处理传入接口时寻找对应的组件
     */
    private static final Map<Class, Integer> CLASS_MAP = new HashMap<>();

    /**
     * 注册ECS组件
     *
     * @param clazz
     */
    static synchronized <T extends ECSModule> Integer getECSModuleIndex(Class<T> clazz) {
        // 获取索引类
        Class keyClass = getECSClassKey(clazz);

        Integer index = CLASS_MAP.get(keyClass);
        if (index == null) {
            index = COMPONENT_ID_GENERATE.getAndIncrement();

            CLASS_MAP.put(keyClass, index);
        }

        // 返回索引
        return index;
    }

    /**
     * 获取该ECS组件的Key
     *
     * @param clazz
     * @return
     */
    private static Class getECSClassKey(Class clazz) {
        // 如果父类是ECS组件的接口，则key为当前类
        if (clazz.getSuperclass() == ECSModule.class) {
            return clazz;
        }

        // 如果父类是抽象类，则key为当前类，不再往上查找
        if (Modifier.isAbstract(clazz.getSuperclass().getModifiers())) {
            return clazz;
        }

        // 往上查找
        return getECSClassKey(clazz.getSuperclass());
    }

}
