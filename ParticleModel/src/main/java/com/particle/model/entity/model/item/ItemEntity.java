package com.particle.model.entity.model.item;

import com.particle.model.entity.Entity;
import com.particle.model.entity.id.IDAllocation;

import java.util.concurrent.atomic.AtomicInteger;

public class ItemEntity extends Entity {

    private static AtomicInteger index = new AtomicInteger();

    /**
     * 申请一个itementity的id，用于播放物品动画
     *
     * @return
     */
    public static long requestId() {
        return IDAllocation.ITEM_ENTITY_BASE_ID + index.getAndIncrement();
    }

    @Override
    protected void onInit() {
    }

    @Override
    protected long generateRuntimeId() {
        return IDAllocation.ITEM_ENTITY_BASE_ID + index.getAndIncrement();
    }
}
