package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class DebugInfoPacket extends DataPacket {
    private long entityId;
    private String data;

    @Override
    public int pid() {
        return ProtocolInfo.DEBUG_INFO_PACKET;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
