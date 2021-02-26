package com.particle.network.handler.v388;

import com.particle.model.network.packets.data.CompletedUsingItemPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class CompletedUsingItemPacketHandler388 extends AbstractPacketHandler<CompletedUsingItemPacket> {
    @Override
    protected void doDecode(CompletedUsingItemPacket dataPacket, int version) {
        dataPacket.setItemId(dataPacket.readShort());
        dataPacket.setItemUseMethod(dataPacket.readLInt());
    }

    @Override
    protected void doEncode(CompletedUsingItemPacket dataPacket, int version) {

    }
}
