package com.particle.model.network.packets.data;

import com.particle.model.entity.link.EntityLink;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class SetEntityLinkPacket extends DataPacket {

    private EntityLink entityLink;

    public EntityLink getEntityLink() {
        return entityLink;
    }

    public void setEntityLink(EntityLink entityLink) {
        this.entityLink = entityLink;
    }

    @Override
    public int pid() {
        return ProtocolInfo.SET_ENTITY_LINK_PACKET;
    }
}
