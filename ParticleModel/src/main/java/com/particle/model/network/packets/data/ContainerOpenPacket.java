package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class ContainerOpenPacket extends DataPacket {

    private int containerId;

    private int containType;

    private int x = 0;

    private int y = 0;

    private int z = 0;

    private long targetEntityId = -1;

    @Override
    public int pid() {
        return ProtocolInfo.CONTAINER_OPEN_PACKET;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int getContainType() {
        return containType;
    }

    public void setContainType(int containType) {
        this.containType = containType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public long getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(long targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    @Override
    public String toString() {
        return "ContainerOpenPacket{" +
                "containerId=" + containerId +
                ", containType=" + containType +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", targetEntityId=" + targetEntityId +
                '}';
    }
}
