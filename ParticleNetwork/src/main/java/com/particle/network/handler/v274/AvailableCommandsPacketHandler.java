package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.AvailableCommandsPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class AvailableCommandsPacketHandler extends AbstractPacketHandler<AvailableCommandsPacket> {

    @Override
    protected void doDecode(AvailableCommandsPacket dataPacket, int version) {

    }

    @Override
    protected void doEncode(AvailableCommandsPacket dataPacket, int version) {
        // enum values
        dataPacket.writeUnsignedVarInt(0);
        // post fixes
        dataPacket.writeUnsignedVarInt(0);
        // enum data
        dataPacket.writeUnsignedVarInt(0);
        // commands
        dataPacket.writeUnsignedVarInt(0);
    }
}
