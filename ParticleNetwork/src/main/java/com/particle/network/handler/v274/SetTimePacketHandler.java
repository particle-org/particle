package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.SetTimePacket;
import com.particle.network.handler.AbstractPacketHandler;

public class SetTimePacketHandler extends AbstractPacketHandler<SetTimePacket> {

    @Override
    protected void doDecode(SetTimePacket dataPacket, int version) {
        dataPacket.setTime(dataPacket.readSignedVarInt());
    }

    @Override
    protected void doEncode(SetTimePacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getTime());
    }
}
