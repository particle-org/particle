package com.particle.network.handler.v274;

import com.particle.model.entity.EntityType;
import com.particle.model.network.packets.data.LevelSoundEventV1Packet;
import com.particle.network.encoder.PositionFEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class LevelSoundEventV1PacketHandler extends AbstractPacketHandler<LevelSoundEventV1Packet> {

    private PositionFEncoder positionFEncoder = PositionFEncoder.getInstance();

    @Override
    protected void doDecode(LevelSoundEventV1Packet dataPacket, int version) {
        dataPacket.setEventId(dataPacket.readByte());
        dataPacket.setPosition(positionFEncoder.decode(dataPacket, version));
        dataPacket.setData(dataPacket.readSignedVarInt());
        dataPacket.setEntityType(EntityType.fromValue(dataPacket.readSignedVarInt()));
        dataPacket.setBabyMob(dataPacket.readBoolean());
        dataPacket.setGlobal(dataPacket.readBoolean());
    }

    @Override
    protected void doEncode(LevelSoundEventV1Packet dataPacket, int version) {
        dataPacket.writeByte(dataPacket.getEventId());
        positionFEncoder.encode(dataPacket, dataPacket.getPosition(), version);
        dataPacket.writeSignedVarInt(dataPacket.getData());
        dataPacket.writeSignedVarInt(dataPacket.getEntityType().type());
        dataPacket.writeBoolean(dataPacket.isBabyMob());
        dataPacket.writeBoolean(dataPacket.isGlobal());
    }
}
