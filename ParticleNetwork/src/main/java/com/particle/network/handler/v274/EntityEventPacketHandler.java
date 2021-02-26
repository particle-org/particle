package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.EntityEventPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class EntityEventPacketHandler extends AbstractPacketHandler<EntityEventPacket> {

    @Override
    protected void doDecode(EntityEventPacket dataPacket, int version) {
        dataPacket.setEntityRuntimeId(dataPacket.readUnsignedVarLong());
        dataPacket.setEventId(dataPacket.readByte());
        dataPacket.setData(dataPacket.readSignedVarInt());
    }

    @Override
    protected void doEncode(EntityEventPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getEntityRuntimeId());
        dataPacket.writeByte((byte) dataPacket.getEventId());
        dataPacket.writeSignedVarInt(dataPacket.getData());
    }
}
