package com.particle.network.handler.v282;

import com.particle.model.network.packets.data.ResourcePacksInfoPacket;
import com.particle.model.resources.ResourcePackInfo;
import com.particle.network.handler.AbstractPacketHandler;

public class ResourcePacksInfoPacketHandler282 extends AbstractPacketHandler<ResourcePacksInfoPacket> {

    @Override
    protected void doDecode(ResourcePacksInfoPacket dataPacket, int version) {
    }

    @Override
    protected void doEncode(ResourcePacksInfoPacket dataPacket, int version) {
        dataPacket.writeBoolean(dataPacket.isTextureRequired());
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
        }
    }
}
