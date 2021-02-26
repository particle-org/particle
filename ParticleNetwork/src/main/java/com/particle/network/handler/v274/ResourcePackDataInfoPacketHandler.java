package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.ResourcePackDataInfoPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class ResourcePackDataInfoPacketHandler extends AbstractPacketHandler<ResourcePackDataInfoPacket> {

    @Override
    protected void doDecode(ResourcePackDataInfoPacket dataPacket, int version) {
        dataPacket.setId(dataPacket.readString());
        dataPacket.setChunkSize(dataPacket.readLInt());
        dataPacket.setChunkCounts(dataPacket.readLInt());
        dataPacket.setFileSize(dataPacket.readLLong());
        byte[] hash = new byte[dataPacket.readUnsignedVarInt()];
        dataPacket.readBytes(hash);
        dataPacket.setFileHash(hash);
    }

    @Override
    protected void doEncode(ResourcePackDataInfoPacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getId());
        dataPacket.writeLInt(dataPacket.getChunkSize());
        dataPacket.writeLInt(dataPacket.getChunkCounts());
        dataPacket.writeLLong(dataPacket.getFileSize());
        dataPacket.writeUnsignedVarInt(dataPacket.getFileHash().length);
        dataPacket.writeBytes(dataPacket.getFileHash());
    }
}
