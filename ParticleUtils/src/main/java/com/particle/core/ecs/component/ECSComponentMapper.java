package com.particle.core.ecs.component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ECSComponentMapper {

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
     * @param keyClass
     */
    static synchronized <T extends ECSComponent> Integer getECSComponentIndex(Class<T> keyClass) {
        Integer index = CLASS_MAP.get(keyClass);
        if (index == null) {
            index = COMPONENT_ID_GENERATE.getAndIncrement();

            CLASS_MAP.put(keyClass, index);
        }

        // 返回索引
        return index;
    }

}
