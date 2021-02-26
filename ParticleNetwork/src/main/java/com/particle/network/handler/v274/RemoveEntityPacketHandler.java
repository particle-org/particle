package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.RemoveEntityPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class RemoveEntityPacketHandler extends AbstractPacketHandler<RemoveEntityPacket> {
    @Override
    protected void doDecode(RemoveEntityPacket dataPacket, int version) {
        dataPacket.setEntityUniqueId(dataPacket.readSignedVarLong().longValue());
    }

    @Override
    protected void doEncode(RemoveEntityPacket dataPacket, int version) {
        dataPacket.writeSignedVarLong(dataPacket.getEntityUniqueId());
    }
}
