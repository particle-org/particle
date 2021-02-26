package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.SetPlayerGameTypePacket;
import com.particle.network.handler.AbstractPacketHandler;

public class SetPlayerGameTypePacketHandler extends AbstractPacketHandler<SetPlayerGameTypePacket> {

    @Override
    protected void doDecode(SetPlayerGameTypePacket dataPacket, int version) {
        dataPacket.setGameMode(dataPacket.readSignedVarInt());
    }

    @Override
    protected void doEncode(SetPlayerGameTypePacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getGameMode());

    }
}
