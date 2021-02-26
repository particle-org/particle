package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.PlayerStatusPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class PlayerStatusPacketHandler extends AbstractPacketHandler<PlayerStatusPacket> {

    @Override
    protected void doDecode(PlayerStatusPacket dataPacket, int version) {
        dataPacket.setStatus(dataPacket.readInt());
    }

    @Override
    protected void doEncode(PlayerStatusPacket dataPacket, int version) {
        dataPacket.writeInt(dataPacket.getStatus());
    }
}
