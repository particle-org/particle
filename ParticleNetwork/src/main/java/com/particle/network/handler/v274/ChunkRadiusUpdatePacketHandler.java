package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.ChunkRadiusUpdatePacket;
import com.particle.network.handler.AbstractPacketHandler;

public class ChunkRadiusUpdatePacketHandler extends AbstractPacketHandler<ChunkRadiusUpdatePacket> {
    @Override
    protected void doDecode(ChunkRadiusUpdatePacket dataPacket, int version) {
        dataPacket.setRadius(dataPacket.readSignedVarInt());
    }

    @Override
    protected void doEncode(ChunkRadiusUpdatePacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getRadius());
    }
}
