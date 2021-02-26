package com.particle.model.entity.id;

import java.util.concurrent.atomic.AtomicInteger;

public class EntityIDManage {
    private static AtomicInteger index = new AtomicInteger();

    public static long getID() {
        return IDAllocation.ENTITY_BASE_ID + index.getAndIncrement();
    }
}
