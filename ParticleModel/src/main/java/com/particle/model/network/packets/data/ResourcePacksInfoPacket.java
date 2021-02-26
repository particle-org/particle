package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.resources.ResourcePackInfo;

public class ResourcePacksInfoPacket extends DataPacket {

    private boolean textureRequired;

    private ResourcePackInfo[] addOnPacks;

    private ResourcePackInfo[] texturePacks;

    @Override
    public int pid() {
        return ProtocolInfo.RESOURCE_PACKS_INFO_PACKET;
    }

    public boolean isTextureRequired() {
        return textureRequired;
    }

    public void setTextureRequired(boolean textureRequired) {
        this.textureRequired = textureRequired;
    }

    public ResourcePackInfo[] getAddOnPacks() {
        return addOnPacks;
    }

    public void setAddOnPacks(ResourcePackInfo[] addOnPacks) {
        this.addOnPacks = addOnPacks;
    }

    public ResourcePackInfo[] getTexturePacks() {
        return texturePacks;
    }

    public void setTexturePacks(ResourcePackInfo[] texturePacks) {
        this.texturePacks = texturePacks;
    }
}
