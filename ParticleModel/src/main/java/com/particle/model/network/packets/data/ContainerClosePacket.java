package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class ContainerClosePacket extends DataPacket {

    private int containerId;

    @Override
    public int pid() {
        return ProtocolInfo.CONTAINER_CLOSE_PACKET;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }
}
