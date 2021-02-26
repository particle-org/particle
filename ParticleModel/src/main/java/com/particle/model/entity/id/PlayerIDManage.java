package com.particle.model.entity.id;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayerIDManage {
    private static AtomicInteger index = new AtomicInteger();

    public static long getID() {
        return IDAllocation.PLAYER_BASE_ID + index.getAndIncrement();
    }
}
