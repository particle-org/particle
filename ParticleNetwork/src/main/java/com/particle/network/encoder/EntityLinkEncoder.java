package com.particle.network.encoder;

import com.particle.model.entity.link.EntityLink;
import com.particle.model.entity.link.EntityLinkType;
import com.particle.model.network.packets.DataPacket;

public class EntityLinkEncoder extends ModelHandler<EntityLink> {

    /**
     * 单例对象
     */
    private static final EntityLinkEncoder INSTANCE = new EntityLinkEncoder();

    /**
     * 获取单例
     */
    public static EntityLinkEncoder getInstance() {
        return EntityLinkEncoder.INSTANCE;
    }

    @Override
    public EntityLink decode(DataPacket dataPacket, int version) {
        EntityLink link = new EntityLink();
        link.setEntityUniqueIdA(dataPacket.readSignedVarLong().longValue());
        link.setEntityUniqueIdB(dataPacket.readSignedVarLong().longValue());
        link.setLinkType(EntityLinkType.valueOf(dataPacket.readByte()));
        link.setUnknownByte(dataPacket.readByte());
        link.setImmediate(dataPacket.readBoolean());

        return link;
    }

    @Override
    public void encode(DataPacket dataPacket, EntityLink entityLink, int version) {
        dataPacket.writeSignedVarLong(entityLink.getEntityUniqueIdA());
        dataPacket.writeSignedVarLong(entityLink.getEntityUniqueIdB());
        dataPacket.writeByte((byte) entityLink.getLinkType().getId());
        dataPacket.writeByte(entityLink.getUnknownByte());
        dataPacket.writeBoolean(entityLink.isImmediate());
    }
}
