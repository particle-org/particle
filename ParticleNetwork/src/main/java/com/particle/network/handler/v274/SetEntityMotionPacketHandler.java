package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.SetEntityMotionPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class SetEntityMotionPacketHandler extends AbstractPacketHandler<SetEntityMotionPacket> {

    @Override
    protected void doDecode(SetEntityMotionPacket dataPacket, int version) {
        dataPacket.setEid(dataPacket.readUnsignedVarLong());
        dataPacket.setMotionX(dataPacket.readLFloat());
        dataPacket.setMotionY(dataPacket.readLFloat());
        dataPacket.setMotionZ(dataPacket.readLFloat());
    }

    @Override
    protected void doEncode(SetEntityMotionPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getEid());
        dataPacket.writeLFloat(dataPacket.getMotionX());
        dataPacket.writeLFloat(dataPacket.getMotionY());
        dataPacket.writeLFloat(dataPacket.getMotionZ());
    }
}
