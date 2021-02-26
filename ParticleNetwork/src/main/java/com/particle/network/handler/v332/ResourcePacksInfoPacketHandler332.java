package com.particle.network.handler.v332;

import com.particle.model.network.packets.data.ResourcePacksInfoPacket;
import com.particle.model.resources.ResourcePackInfo;
import com.particle.network.handler.AbstractPacketHandler;

public class ResourcePacksInfoPacketHandler332 extends AbstractPacketHandler<ResourcePacksInfoPacket> {

    @Override
    protected void doDecode(ResourcePacksInfoPacket dataPacket, int version) {
    }

    @Override
    protected void doEncode(ResourcePacksInfoPacket dataPacket, int version) {
        dataPacket.writeBoolean(dataPacket.isTextureRequired());
        dataPacket.writeBoolean(false);

        dataPacket.writeLShort((short) dataPacket.getAddOnPacks().length);
        for (ResourcePackInfo resourcePackInfo : dataPacket.getAddOnPacks()) {
            dataPacket.writeString(resourcePackInfo.getId());
            dataPacket.writeString(resourcePackInfo.getVersion());
            dataPacket.writeLLong(resourcePackInfo.getSize());
            // content key
            dataPacket.writeString("");
            // subPackName
            dataPacket.writeString("");
            // 1.6
            // content identity
            dataPacket.writeString("");
            // 1.9
            dataPacket.writeBoolean(false);
        }

        dataPacket.writeLShort((short) dataPacket.getTexturePacks().length);
        for (ResourcePackInfo resourcePackInfo : dataPacket.getTexturePacks()) {
            dataPacket.writeString(resourcePackInfo.getId());
            dataPacket.writeString(resourcePackInfo.getVersion());
            dataPacket.writeLLong(resourcePackInfo.getSize());
            // content key
            dataPacket.writeString("");
            // subPackName
            dataPacket.writeString("");
            // 1.6
            // content identity
            dataPacket.writeString("");
            // 1.9
            dataPacket.writeBoolean(false);
        }
    }
}
