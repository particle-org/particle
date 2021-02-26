package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.resources.ResourcePackInfo;

public class ResourcePackStackPacket extends DataPacket {

    private boolean textureRequired;

    private ResourcePackInfo[] addOnPacks;

    private ResourcePackInfo[] texturePacks;

    private boolean IsExperimental;

    private String baseGameVersion;

    @Override
    public int pid() {
        return ProtocolInfo.RESOURCE_PACK_STACK_PACKET;
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

    public boolean isExperimental() {
        return IsExperimental;
    }

    public void setExperimental(boolean experimental) {
        IsExperimental = experimental;
    }

    public String getBaseGameVersion() {
        return baseGameVersion;
    }

    public void setBaseGameVersion(String baseGameVersion) {
        this.baseGameVersion = baseGameVersion;
    }
}
