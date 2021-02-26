package com.particle.model.network.packets.data;

import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.player.action.InteractPacketAction;

public class InteractPacket extends DataPacket {

    private InteractPacketAction interactPacketAction;

    private long targetRuntimeId;

    private Vector3f position;

    @Override
    public int pid() {
        return ProtocolInfo.INTERACT_PACKET;
    }

    public InteractPacketAction getInteractPacketAction() {
        return interactPacketAction;
    }

    public void setInteractPacketAction(InteractPacketAction interactPacketAction) {
        this.interactPacketAction = interactPacketAction;
    }

    public long getTargetRuntimeId() {
        return targetRuntimeId;
    }

    public void setTargetRuntimeId(long targetRuntimeId) {
        this.targetRuntimeId = targetRuntimeId;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "InteractPacket{" +
                "interactPacketAction=" + interactPacketAction +
                ", targetRuntimeId=" + targetRuntimeId +
                ", position=" + position +
                '}';
    }
}
