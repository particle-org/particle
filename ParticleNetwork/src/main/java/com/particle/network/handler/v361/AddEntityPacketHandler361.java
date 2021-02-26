package com.particle.network.handler.v361;

import com.particle.model.entity.link.EntityLink;
import com.particle.model.network.packets.data.AddEntityPacket;
import com.particle.network.encoder.*;
import com.particle.network.handler.AbstractPacketHandler;

import java.util.List;

public class AddEntityPacketHandler361 extends AbstractPacketHandler<AddEntityPacket> {

    private EntityAttributesEncoder entityAttributesEncoder = EntityAttributesEncoder.getInstance();

    private EntityMetaDataEncoderV361 entityMetaDataEncoder = EntityMetaDataEncoderV361.getInstance();

    private PositionFEncoder positionFEncoder = PositionFEncoder.getInstance();

    private DirectionFEncoder directionFEncoder = DirectionFEncoder.getInstance();

    private EntityLinkEncoder entityLinkEncoder = EntityLinkEncoder.getInstance();

    @Override
    protected void doDecode(AddEntityPacket dataPacket, int version) {
        dataPacket.setEntityUniqueId(dataPacket.readSignedVarLong().longValue());
        dataPacket.setEntityRuntimeId(dataPacket.readUnsignedVarLong());

        // 1.8
        String actorType = dataPacket.readString();
        dataPacket.setActorType(actorType);

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

        // 1.8
        dataPacket.writeString(dataPacket.getActorType());

        positionFEncoder.encode(dataPacket, dataPacket.getPosition(), version);
        dataPacket.writeLFloat(dataPacket.getSpeedX());
        dataPacket.writeLFloat(dataPacket.getSpeedY());
        dataPacket.writeLFloat(dataPacket.getSpeedZ());
        directionFEncoder.encode(dataPacket, dataPacket.getDirection(), version);

        entityAttributesEncoder.encode(dataPacket, dataPacket.getAttribute(), version);
        entityMetaDataEncoder.encode(dataPacket, dataPacket.getMetadata(), version);

        // entitylink
        dataPacket.writeUnsignedVarInt(dataPacket.getEntityLinks().size());
        for (EntityLink link : dataPacket.getEntityLinks()) {
            this.entityLinkEncoder.encode(dataPacket, link, version);
        }
    }
}
