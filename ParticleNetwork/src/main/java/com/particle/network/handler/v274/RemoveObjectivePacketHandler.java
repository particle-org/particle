package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.RemoveObjectivePacket;
import com.particle.network.handler.AbstractPacketHandler;

public class RemoveObjectivePacketHandler extends AbstractPacketHandler<RemoveObjectivePacket> {

    @Override
    protected void doDecode(RemoveObjectivePacket dataPacket, int version) {
        dataPacket.setObjectiveName(dataPacket.readString());
    }

    @Override
    protected void doEncode(RemoveObjectivePacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getObjectiveName());
    }
}
