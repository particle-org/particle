package com.particle.model.network.packets.data;

import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class MoveEntityPacket extends DataPacket {
    private long entityId;
    private Vector3f vector3f;
    private Direction direction;
    private boolean onGround;
    private boolean teleport;

    @Override
    public int pid() {
        return ProtocolInfo.MOVE_ENTITY_PACKET;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public Vector3f getVector3f() {
        return vector3f;
    }

    public void setVector3f(Vector3f vector3f) {
        this.vector3f = vector3f;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isTeleport() {
        return teleport;
    }

    public void setTeleport(boolean teleport) {
        this.teleport = teleport;
    }
}
