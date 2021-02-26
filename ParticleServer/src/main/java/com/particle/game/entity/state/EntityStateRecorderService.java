package com.particle.game.entity.state;

import com.particle.game.entity.state.handle.EmptyHandle;
import com.particle.game.entity.state.handle.EntityStateHandle;

import java.util.HashMap;
import java.util.Map;

public class EntityStateRecorderService {

    private static Map<String, EntityStateHandle> entityStateHandles = new HashMap<>();
    private static EntityStateHandle defaultHandle = new EmptyHandle();

    /**
     * 注册处理器
     *
     * @param key
     * @param handle
     */
    public static void register(String key, EntityStateHandle handle) {
        EntityStateRecorderService.entityStateHandles.put(key, handle);
    }

    /**
     * 查询处理器
     *
     * @param key
     * @return
     */
    public static EntityStateHandle get(String key) {
        return EntityStateRecorderService.entityStateHandles.getOrDefault(key, defaultHandle);
    }

}
