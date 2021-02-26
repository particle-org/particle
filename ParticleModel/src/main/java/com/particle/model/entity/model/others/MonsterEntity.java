package com.particle.model.entity.model.others;

import com.particle.model.entity.LivingEntity;
import com.particle.model.entity.id.IDAllocation;

import java.util.concurrent.atomic.AtomicInteger;

public class MonsterEntity extends LivingEntity {

    private static AtomicInteger index = new AtomicInteger();

    private String actorType;
    private int networkId;

    public MonsterEntity(String actorType, int networkId) {
        this.actorType = actorType;
        this.networkId = networkId;
    }

    @Override
    public String getActorType() {
        return actorType;
    }

    public int getNetworkId() {
        return networkId;
    }

    @Override
    protected void onInit() {
    }

    @Override
    protected long generateRuntimeId() {
        return IDAllocation.MONSTER_BASE_ID + index.getAndIncrement();
    }
}