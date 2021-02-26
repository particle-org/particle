package com.particle.model.entity.id;

import java.util.concurrent.atomic.AtomicInteger;

public class ItemEntityIDManage {
    private static AtomicInteger index = new AtomicInteger();

    public static long getID() {
        return IDAllocation.ITEM_ENTITY_BASE_ID + index.getAndIncrement();
    }
}
