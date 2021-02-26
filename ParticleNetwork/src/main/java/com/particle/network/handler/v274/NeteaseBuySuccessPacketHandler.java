package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.NeteaseBuySuccessPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class NeteaseBuySuccessPacketHandler extends AbstractPacketHandler<NeteaseBuySuccessPacket> {
    @Override
    protected void doDecode(NeteaseBuySuccessPacket dataPacket, int version) {
        dataPacket.setContent(dataPacket.readString());
    }

    @Override
    protected void doEncode(NeteaseBuySuccessPacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getContent());
    }
}
