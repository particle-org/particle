package com.particle.model.entity.model.mob;

import com.particle.model.entity.LivingEntity;
import com.particle.model.entity.id.IDAllocation;

import java.util.concurrent.atomic.AtomicInteger;

public class MobEntity extends LivingEntity {

    private static AtomicInteger index = new AtomicInteger();

    private String actorType;
    private int networkId;

    public MobEntity(String actorType, int networkId) {
        this.actorType = actorType;
        this.networkId = networkId;
    }

    /**
     * 申请一个id，用于播放物品动画
     *
     * @return
     */
    public static long requestId() {
        return IDAllocation.ENTITY_BASE_ID + index.getAndIncrement();
    }

    @Override
    public String getActorType() {
        return actorType;
    }

    public int getNetworkId() {
        return networkId;
    }

    @Override
    public void onInit() {
    }

    protected long generateRuntimeId() {
        return IDAllocation.ENTITY_BASE_ID + index.getAndIncrement();
    }
}
