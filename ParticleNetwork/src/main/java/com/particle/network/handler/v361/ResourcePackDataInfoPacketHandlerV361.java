package com.particle.network.handler.v361;

import com.particle.model.network.packets.data.ResourcePackDataInfoPacket;
import com.particle.model.resources.ResourcePackType;
import com.particle.network.handler.AbstractPacketHandler;

public class ResourcePackDataInfoPacketHandlerV361 extends AbstractPacketHandler<ResourcePackDataInfoPacket> {

    @Override
    protected void doDecode(ResourcePackDataInfoPacket dataPacket, int version) {
        dataPacket.setId(dataPacket.readString());
        dataPacket.setChunkSize(dataPacket.readLInt());
        dataPacket.setChunkCounts(dataPacket.readLInt());
        dataPacket.setFileSize(dataPacket.readLLong());
        byte[] hash = new byte[dataPacket.readUnsignedVarInt()];
        dataPacket.readBytes(hash);
        dataPacket.setFileHash(hash);
        dataPacket.setPremium(dataPacket.readBoolean());
        dataPacket.setPackType(ResourcePackType.getTypeFromV361(dataPacket.readByte()));
    }

    @Override
    protected void doEncode(ResourcePackDataInfoPacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getId());
        dataPacket.writeLInt(dataPacket.getChunkSize());
        dataPacket.writeLInt(dataPacket.getChunkCounts());
        dataPacket.writeLLong(dataPacket.getFileSize());
        dataPacket.writeUnsignedVarInt(dataPacket.getFileHash().length);
        dataPacket.writeBytes(dataPacket.getFileHash());
        dataPacket.writeBoolean(dataPacket.isPremium());
        dataPacket.writeByte(dataPacket.getPackType().getIdV361());
    }
}
