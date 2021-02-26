package com.particle.network.handler.v313;

import com.particle.model.network.packets.data.NetworkChunkPublisherUpdatePacket;
import com.particle.network.handler.AbstractPacketHandler;

public class NetworkChunkPublisherUpdatePacketHandler extends AbstractPacketHandler<NetworkChunkPublisherUpdatePacket> {

    @Override
    protected void doDecode(NetworkChunkPublisherUpdatePacket dataPacket, int version) {
        dataPacket.setBlockX(dataPacket.readSignedVarInt());
        dataPacket.setBlockY(dataPacket.readUnsignedVarInt());
        dataPacket.setBlockZ(dataPacket.readUnsignedVarInt());
        dataPacket.setNewRadiusForView(dataPacket.readUnsignedVarInt());
    }

    @Override
    protected void doEncode(NetworkChunkPublisherUpdatePacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getBlockX());
        dataPacket.writeSignedVarInt(dataPacket.getBlockY());
        dataPacket.writeSignedVarInt(dataPacket.getBlockZ());
        dataPacket.writeUnsignedVarInt(dataPacket.getNewRadiusForView());
    }
}
