package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.ContainerSetDataPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class ContainerSetDataPacketHandler extends AbstractPacketHandler<ContainerSetDataPacket> {

    @Override
    protected void doDecode(ContainerSetDataPacket dataPacket, int version) {
        dataPacket.setContainerId(dataPacket.readByte());
        dataPacket.setProperty(dataPacket.readSignedVarInt());
        dataPacket.setProperty(dataPacket.readSignedVarInt());
    }

    @Override
    protected void doEncode(ContainerSetDataPacket dataPacket, int version) {
        dataPacket.writeByte((byte) dataPacket.getContainerId());
        dataPacket.writeSignedVarInt(dataPacket.getProperty());
        dataPacket.writeSignedVarInt(dataPacket.getValue());
    }
}
