package com.particle.network.handler.v313;

import com.particle.model.network.packets.data.ResourcePackStackPacket;
import com.particle.model.resources.ResourcePackInfo;
import com.particle.network.handler.AbstractPacketHandler;

public class ResourcePackStackPacketHandler303 extends AbstractPacketHandler<ResourcePackStackPacket> {

    @Override
    protected void doDecode(ResourcePackStackPacket dataPacket, int version) {

    }

    @Override
    protected void doEncode(ResourcePackStackPacket dataPacket, int version) {
        dataPacket.writeBoolean(dataPacket.isTextureRequired());
        dataPacket.writeUnsignedVarInt(dataPacket.getAddOnPacks().length);
        for (ResourcePackInfo resourcePackInfo : dataPacket.getAddOnPacks()) {
            dataPacket.writeString(resourcePackInfo.getId());
            dataPacket.writeString(resourcePackInfo.getVersion());
            // subPackName
            dataPacket.writeString("");
        }
        dataPacket.writeUnsignedVarInt(dataPacket.getTexturePacks().length);
        for (ResourcePackInfo resourcePackInfo : dataPacket.getTexturePacks()) {
            dataPacket.writeString(resourcePackInfo.getId());
            dataPacket.writeString(resourcePackInfo.getVersion());
            // subPackName
            dataPacket.writeString("");
        }
        dataPacket.writeBoolean(dataPacket.isExperimental());
    }
}
