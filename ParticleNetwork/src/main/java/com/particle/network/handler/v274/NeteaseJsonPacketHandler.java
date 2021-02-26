package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.NeteaseJsonPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class NeteaseJsonPacketHandler extends AbstractPacketHandler<NeteaseJsonPacket> {
    @Override
    protected void doDecode(NeteaseJsonPacket dataPacket, int version) {
        dataPacket.setContent(dataPacket.readString());
    }

    @Override
    protected void doEncode(NeteaseJsonPacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getContent());
    }
}
