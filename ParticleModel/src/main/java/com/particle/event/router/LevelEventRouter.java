package com.particle.event.router;

import com.particle.event.handle.LevelEventHandle;
import com.particle.model.events.BaseEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 数据包路由.
 */
public class LevelEventRouter {
    private static final Map<Class, List<LevelEventHandle>> HANDLES = new ConcurrentHashMap<>();

    public static void subscript(LevelEventHandle handle) {
        List<LevelEventHandle> eventHandles = HANDLES.computeIfAbsent(
                handle.getListenerEvent(), k -> new LinkedList<>());

        for (int i = 0; i < eventHandles.size(); i++) {
            if (eventHandles.get(i).getEventRank().getLevel() < handle.getEventRank().getLevel()) {
                eventHandles.add(i, handle);
                return;
            }
        }

        eventHandles.add(handle);
    }

    public static List<LevelEventHandle> route(BaseEvent event) {
        return HANDLES.get(event.getClass());
    }
}
