package com.particle.model.network.packets.data;

import com.particle.model.entity.EntityType;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class LevelSoundEventV1Packet extends DataPacket {

    private byte eventId;

    private Vector3f position;

    private int data;

    private EntityType entityType;

    private boolean babyMob;

    private boolean global;

    @Override
    public int pid() {
        return ProtocolInfo.LEVEL_SOUND_EVENT_V1_PACKET;
    }

    public byte getEventId() {
        return eventId;
    }

    public void setEventId(byte eventId) {
        this.eventId = eventId;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public boolean isBabyMob() {
        return babyMob;
    }

    public void setBabyMob(boolean babyMob) {
        this.babyMob = babyMob;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    @Override
    public String toString() {
        return "LevelSoundEventV1Packet{" +
                "eventId=" + eventId +
                ", position=" + position +
                ", data=" + data +
                ", entityType=" + entityType +
                ", babyMob=" + babyMob +
                ", global=" + global +
                '}';
    }
}
