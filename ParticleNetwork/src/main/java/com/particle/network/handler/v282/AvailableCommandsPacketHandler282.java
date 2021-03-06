package com.particle.network.handler.v282;

import com.particle.model.network.packets.data.AvailableCommandsPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class AvailableCommandsPacketHandler282 extends AbstractPacketHandler<AvailableCommandsPacket> {

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
        // soft enums
        dataPacket.writeUnsignedVarInt(0);
    }
}
