package com.particle.model.network.packets.data;

import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class AvailableEntityIdentifiersPacket extends DataPacket {

    private NBTTagCompound actorInfoList;

    @Override
    public int pid() {
        return ProtocolInfo.AVAILABLE_ACTOR_IDENTIFIERS_PACKET;
    }

    public NBTTagCompound getActorInfoList() {
        return actorInfoList;
    }

    public void setActorInfoList(NBTTagCompound actorInfoList) {
        this.actorInfoList = actorInfoList;
    }
}
