package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.TakeItemEntityPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class TakeItemEntityPacketHandler extends AbstractPacketHandler<TakeItemEntityPacket> {
    @Override
    protected void doDecode(TakeItemEntityPacket dataPacket, int version) {
        dataPacket.setEntityRuntimeId(dataPacket.readUnsignedVarLong());
        dataPacket.setPlayerRuntimeId(dataPacket.readUnsignedVarLong());
    }

    @Override
    protected void doEncode(TakeItemEntityPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getEntityRuntimeId());
        dataPacket.writeUnsignedVarLong(dataPacket.getPlayerRuntimeId());
    }
}
