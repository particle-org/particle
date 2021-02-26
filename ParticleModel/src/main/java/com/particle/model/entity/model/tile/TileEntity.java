package com.particle.model.entity.model.tile;


import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3;

public abstract class TileEntity extends Entity {

    public abstract String getName();

    public abstract boolean needNoticeClient();

    @Override
    protected long generateRuntimeId() {
        return 0;
    }

    public void onCreated(Vector3 position) {
        this.entityId = position2Id(position);
    }

    public static int position2Id(Vector3 position) {
        return ((position.getX() & 15) << 12) | ((position.getY() & 255) << 4) | position.getZ() & 15;
    }
}
