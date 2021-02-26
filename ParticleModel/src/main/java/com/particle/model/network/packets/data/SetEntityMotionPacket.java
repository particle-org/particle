package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class SetEntityMotionPacket extends DataPacket {

    private long eid;
    private float motionX;
    private float motionY;
    private float motionZ;

    @Override
    public int pid() {
        return ProtocolInfo.SET_ENTITY_MOTION_PACKET;
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public float getMotionX() {
        return motionX;
    }

    public void setMotionX(float motionX) {
        this.motionX = motionX;
    }

    public float getMotionY() {
        return motionY;
    }

    public void setMotionY(float motionY) {
        this.motionY = motionY;
    }

    public float getMotionZ() {
        return motionZ;
    }

    public void setMotionZ(float motionZ) {
        this.motionZ = motionZ;
    }
}
