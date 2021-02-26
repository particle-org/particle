package com.particle.model.network.packets.data;

import com.particle.model.entity.EntityType;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.player.PositionMode;
import com.particle.model.player.TeleportationCause;

public class MovePlayerPacket extends DataPacket {
    private long entityId;
    private Vector3f vector3f;
    private Direction direction;
    private PositionMode mode = PositionMode.NORMAL;
    private boolean onGround;
    private long ridingEntityId;
    private TeleportationCause teleportationCause;
    private EntityType sourceEntityType;

    // 1.16 china branch
    private int tick = 0;

    @Override
    public int pid() {
        return ProtocolInfo.MOVE_PLAYER_PACKET;
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

    public PositionMode getMode() {
        return mode;
    }

    public void setMode(PositionMode mode) {
        this.mode = mode;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public long getRidingEntityId() {
        return ridingEntityId;
    }

    public void setRidingEntityId(long ridingEntityId) {
        this.ridingEntityId = ridingEntityId;
    }

    public TeleportationCause getTeleportationCause() {
        return teleportationCause;
    }

    public void setTeleportationCause(TeleportationCause teleportationCause) {
        this.teleportationCause = teleportationCause;
    }

    public EntityType getSourceEntityType() {
        return sourceEntityType;
    }

    public void setSourceEntityType(EntityType sourceEntityType) {
        this.sourceEntityType = sourceEntityType;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
