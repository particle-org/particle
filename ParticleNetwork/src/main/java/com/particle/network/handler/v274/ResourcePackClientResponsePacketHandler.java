package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.ResourcePackClientResponsePacket;
import com.particle.network.handler.AbstractPacketHandler;

public class ResourcePackClientResponsePacketHandler extends AbstractPacketHandler<ResourcePackClientResponsePacket> {

    @Override
    protected void doDecode(ResourcePackClientResponsePacket dataPacket, int version) {
        dataPacket.setResponse(dataPacket.readByte());
        short length = dataPacket.readLShort();
        dataPacket.setDownloadingPackIds(new String[length]);
        for (int i = 0; i < length; i++) {
            dataPacket.getDownloadingPackIds()[i] = dataPacket.readString();
        }
    }

    @Override
    protected void doEncode(ResourcePackClientResponsePacket dataPacket, int version) {
        dataPacket.writeByte(dataPacket.getResponse());
        dataPacket.writeLShort((short) dataPacket.getDownloadingPackIds().length);
        for (String id : dataPacket.getDownloadingPackIds()) {
            dataPacket.writeString(id);
        }
    }
}
