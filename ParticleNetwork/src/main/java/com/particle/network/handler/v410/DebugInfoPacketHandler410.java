package com.particle.network.handler.v410;

import com.particle.model.network.packets.data.DebugInfoPacket;
import com.particle.network.handler.AbstractPacketHandler;


public class DebugInfoPacketHandler410 extends AbstractPacketHandler<DebugInfoPacket> {

    @Override
    protected void doDecode(DebugInfoPacket dataPacket, int version) {
        dataPacket.setEntityId(dataPacket.readSignedVarLong().longValue());
        dataPacket.setData(dataPacket.readString());
    }

    @Override
    protected void doEncode(DebugInfoPacket dataPacket, int version) {
        dataPacket.writeSignedVarLong(dataPacket.getEntityId());
        dataPacket.writeString(dataPacket.getData());
    }
}
