package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.ResourcePackChunkRequestPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class ResourcePackChunkRequestPacketHandler extends AbstractPacketHandler<ResourcePackChunkRequestPacket> {

    @Override
    protected void doDecode(ResourcePackChunkRequestPacket dataPacket, int version) {
        dataPacket.setId(dataPacket.readString());
        dataPacket.setChunkIndex(dataPacket.readLInt());
    }

    @Override
    protected void doEncode(ResourcePackChunkRequestPacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getId());
        dataPacket.writeLInt(dataPacket.getChunkIndex());
    }
}
