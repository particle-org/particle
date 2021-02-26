package com.particle.model.network.packets.data;

import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class LevelSoundEventV2Packet extends DataPacket {

    private int eventId;

    private Vector3f position;

    private int data;

    private String actorIdentifier;

    private boolean babyMob;

    private boolean global;

    @Override
    public int pid() {
        return ProtocolInfo.LEVEL_SOUND_EVENT_V2_PACKET;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
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

    public String getActorIdentifier() {
        return actorIdentifier;
    }

    public void setActorIdentifier(String actorIdentifier) {
        this.actorIdentifier = actorIdentifier;
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
}
