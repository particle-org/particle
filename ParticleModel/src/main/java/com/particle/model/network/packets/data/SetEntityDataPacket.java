package com.particle.model.network.packets.data;

import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.type.EntityData;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.util.Map;

public class SetEntityDataPacket extends DataPacket {

    private long eid;
    private Map<EntityMetadataType, EntityData> metaData;

    // 1.16 china branch
    private int tick = 0;

    @Override
    public int pid() {
        return ProtocolInfo.SET_ENTITY_DATA_PACKET;
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public Map<EntityMetadataType, EntityData> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<EntityMetadataType, EntityData> metaData) {
        this.metaData = metaData;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
