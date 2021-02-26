package com.particle.model.entity.model.others;

import com.particle.model.entity.LivingEntity;
import com.particle.model.entity.id.IDAllocation;

import java.util.concurrent.atomic.AtomicInteger;

public class NpcEntity extends LivingEntity {

    private static AtomicInteger index = new AtomicInteger();

    @Override
    public void onInit() {
    }

    protected long generateRuntimeId() {
        return IDAllocation.NPC_BASE_ID + index.getAndIncrement();
    }
}
