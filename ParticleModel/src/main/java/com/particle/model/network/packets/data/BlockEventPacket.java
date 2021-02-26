package com.particle.model.network.packets.data;

import com.particle.model.math.Vector3;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class BlockEventPacket extends DataPacket {

    private Vector3 position;

    private int eventType;

    private int eventValue;

    @Override
    public int pid() {
        return ProtocolInfo.BLOCK_EVENT_PACKET;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getEventValue() {
        return eventValue;
    }

    public void setEventValue(int eventValue) {
        this.eventValue = eventValue;
    }
}
