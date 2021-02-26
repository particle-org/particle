package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.ContainerClosePacket;
import com.particle.network.handler.AbstractPacketHandler;

public class ContainerClosePacketHandler extends AbstractPacketHandler<ContainerClosePacket> {

    @Override
    protected void doDecode(ContainerClosePacket dataPacket, int version) {
        dataPacket.setContainerId(dataPacket.readByte());
    }

    @Override
    protected void doEncode(ContainerClosePacket dataPacket, int version) {
        dataPacket.writeByte((byte) (dataPacket.getContainerId() & 0xff));
    }
}
