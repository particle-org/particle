package com.particle.network.handler.v388;

import com.particle.model.network.packets.data.ResourcePackChunkDataPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class ResourcePackChunkDataPacketHandler388 extends AbstractPacketHandler<ResourcePackChunkDataPacket> {

    @Override
    protected void doDecode(ResourcePackChunkDataPacket dataPacket, int version) {
        dataPacket.setId(dataPacket.readString());
        dataPacket.setChunkIndex(dataPacket.readLInt());
        dataPacket.setOffset(dataPacket.readLLong());
        dataPacket.setChunkData(dataPacket.readBytesWithLength());
    }

    @Override
    protected void doEncode(ResourcePackChunkDataPacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getId());
        dataPacket.writeLInt(dataPacket.getChunkIndex());
        dataPacket.writeLLong(dataPacket.getOffset());
        dataPacket.writeBytesWithLength(dataPacket.getChunkData());
    }
}
