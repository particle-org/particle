package com.particle.network.handler.v274;

import com.particle.model.entity.EntityType;
import com.particle.model.entity.link.EntityLink;
import com.particle.model.network.packets.data.AddEntityPacket;
import com.particle.network.encoder.*;
import com.particle.network.handler.AbstractPacketHandler;

import java.util.List;

public class AddEntityPacketHandler extends AbstractPacketHandler<AddEntityPacket> {

    private EntityMetaDataEncoder entityMetaDataEncoder = EntityMetaDataEncoder.getInstance();

    private PositionFEncoder positionFEncoder = PositionFEncoder.getInstance();

    private DirectionFEncoder directionFEncoder = DirectionFEncoder.getInstance();

    private EntityAttributesEncoder entityAttributesEncoder = EntityAttributesEncoder.getInstance();

    private EntityLinkEncoder entityLinkEncoder = EntityLinkEncoder.getInstance();

    @Override
    protected void doDecode(AddEntityPacket dataPacket, int version) {
        dataPacket.setEntityUniqueId(dataPacket.readSignedVarLong().longValue());
        dataPacket.setEntityRuntimeId(dataPacket.readUnsignedVarLong());

        EntityType entityType = EntityType.fromValue(dataPacket.readUnsignedVarInt());
        dataPacket.setActorType(entityType == null ? ":" : entityType.actorType());

        dataPacket.setPosition(positionFEncoder.decode(dataPacket, version));
        dataPacket.setSpeedX(dataPacket.readLFloat());
        dataPacket.setSpeedY(dataPacket.readLFloat());
        dataPacket.setSpeedZ(dataPacket.readLFloat());
        dataPacket.setDirection(directionFEncoder.decode(dataPacket, version));

        dataPacket.setAttribute(entityAttributesEncoder.decode(dataPacket, version));
        dataPacket.setMetadata(entityMetaDataEncoder.decode(dataPacket, version));

        int size = dataPacket.readUnsignedVarInt();
        List<EntityLink> links = dataPacket.getEntityLinks();
        for (int i = 0; i < size; i++) {
            links.add(entityLinkEncoder.decode(dataPacket, version));
        }
    }

    @Override
    protected void doEncode(AddEntityPacket dataPacket, int version) {
        dataPacket.writeSignedVarLong(dataPacket.getEntityUniqueId());
        dataPacket.writeUnsignedVarLong(dataPacket.getEntityRuntimeId());

        EntityType entityType = EntityType.fromValue(dataPacket.getActorType());
        dataPacket.writeUnsignedVarInt(entityType == null ? 0 : entityType.type());

        positionFEncoder.encode(dataPacket, dataPacket.getPosition(), version);
        dataPacket.writeLFloat(dataPacket.getSpeedX());
        dataPacket.writeLFloat(dataPacket.getSpeedY());
        dataPacket.writeLFloat(dataPacket.getSpeedZ());
        directionFEncoder.encode(dataPacket, dataPacket.getDirection(), version);

        entityAttributesEncoder.encode(dataPacket, dataPacket.getAttribute(), version);
        entityMetaDataEncoder.encode(dataPacket, dataPacket.getMetadata(), version);

        dataPacket.writeUnsignedVarInt(0);

        // entitylink
        dataPacket.writeUnsignedVarInt(dataPacket.getEntityLinks().size());
        for (EntityLink link : dataPacket.getEntityLinks()) {
            this.entityLinkEncoder.encode(dataPacket, link, version);
        }
    }
}
