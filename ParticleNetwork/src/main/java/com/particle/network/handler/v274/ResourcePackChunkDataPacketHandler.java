package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.ResourcePackChunkDataPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class ResourcePackChunkDataPacketHandler extends AbstractPacketHandler<ResourcePackChunkDataPacket> {

    @Override
    protected void doDecode(ResourcePackChunkDataPacket dataPacket, int version) {
        dataPacket.setId(dataPacket.readString());
        dataPacket.setChunkIndex(dataPacket.readLInt());
        dataPacket.setOffset(dataPacket.readLLong());
        dataPacket.setChunkData(new byte[dataPacket.readLInt()]);
        dataPacket.readBytes(dataPacket.getChunkData());
    }

    @Override
    protected void doEncode(ResourcePackChunkDataPacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getId());
        dataPacket.writeLInt(dataPacket.getChunkIndex());
        dataPacket.writeLLong(dataPacket.getOffset());
        dataPacket.writeLInt(dataPacket.getChunkData().length);
        dataPacket.writeBytes(dataPacket.getChunkData());
    }
}
