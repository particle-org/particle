package com.particle.model.network.packets.data;

import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class UpdateAttributesPacket extends DataPacket {

    private long entityId;
    private EntityAttribute[] attributes;

    // 1.16 china branch
    private int tick = 0;

    @Override
    public int pid() {
        return ProtocolInfo.UPDATE_ATTRIBUTES_PACKET;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public EntityAttribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(EntityAttribute[] attributes) {
        this.attributes = attributes;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
