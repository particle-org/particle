package com.particle.model.network.packets.data;

import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class BiomeDefinitionListPacket extends DataPacket {

    private NBTTagCompound biomeDefinitionData;

    @Override
    public int pid() {
        return ProtocolInfo.BIOME_DEFINITION_LIST_PACKET;
    }

    public NBTTagCompound getBiomeDefinitionData() {
        return biomeDefinitionData;
    }

    public void setBiomeDefinitionData(NBTTagCompound biomeDefinitionData) {
        this.biomeDefinitionData = biomeDefinitionData;
    }
}
