package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class RemoveEntityPacket extends DataPacket {
    private long entityUniqueId;

    @Override
    public int pid() {
        return ProtocolInfo.REMOVE_ENTITY_PACKET;
    }

    public long getEntityUniqueId() {
        return entityUniqueId;
    }

    public void setEntityUniqueId(long entityUniqueId) {
        this.entityUniqueId = entityUniqueId;
    }
}
