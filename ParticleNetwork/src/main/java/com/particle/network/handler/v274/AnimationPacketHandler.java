package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.AnimationPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class AnimationPacketHandler extends AbstractPacketHandler<AnimationPacket> {
    @Override
    protected void doDecode(AnimationPacket dataPacket, int version) {
        dataPacket.setAction(dataPacket.readSignedVarInt());
        dataPacket.setEntityId(dataPacket.readUnsignedVarLong());
        if ((dataPacket.getAction() & 0x80) != 0) {
            dataPacket.setData(dataPacket.readLFloat());
        }
    }

    @Override
    protected void doEncode(AnimationPacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getAction());
        dataPacket.writeUnsignedVarLong(dataPacket.getEntityId());
        if ((dataPacket.getAction() & 0x80) != 0) {
            dataPacket.writeLFloat(dataPacket.getData());
        }
    }
}
