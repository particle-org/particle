package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.ContainerOpenPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class ContainerOpenPacketHandler extends AbstractPacketHandler<ContainerOpenPacket> {

    @Override
    protected void doDecode(ContainerOpenPacket dataPacket, int version) {
        dataPacket.setContainerId(dataPacket.readByte());
        dataPacket.setContainType(dataPacket.readByte());
        dataPacket.setX(dataPacket.readSignedVarInt());
        dataPacket.setY(dataPacket.readUnsignedVarInt());
        dataPacket.setZ(dataPacket.readSignedVarInt());
        dataPacket.setTargetEntityId(dataPacket.readSignedVarLong().longValue());
    }

    @Override
    protected void doEncode(ContainerOpenPacket dataPacket, int version) {
        dataPacket.writeByte((byte) (dataPacket.getContainerId() & 0xff));
        dataPacket.writeByte((byte) (dataPacket.getContainType() & 0xff));
        dataPacket.writeSignedVarInt(dataPacket.getX());
        dataPacket.writeUnsignedVarInt(dataPacket.getY());
        dataPacket.writeSignedVarInt(dataPacket.getZ());
        dataPacket.writeSignedVarLong(dataPacket.getTargetEntityId());
    }
}
