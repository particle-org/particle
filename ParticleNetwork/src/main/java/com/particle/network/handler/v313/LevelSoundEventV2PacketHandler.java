package com.particle.network.handler.v313;

import com.particle.model.network.packets.data.LevelSoundEventV2Packet;
import com.particle.network.encoder.PositionFEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class LevelSoundEventV2PacketHandler extends AbstractPacketHandler<LevelSoundEventV2Packet> {

    private PositionFEncoder positionFEncoder = PositionFEncoder.getInstance();

    @Override
    protected void doDecode(LevelSoundEventV2Packet dataPacket, int version) {
        dataPacket.setEventId(dataPacket.readByte());
        dataPacket.setPosition(positionFEncoder.decode(dataPacket, AbstractPacketHandler.VERSION_1_8));
        dataPacket.setData(dataPacket.readSignedVarInt());
        dataPacket.setActorIdentifier(dataPacket.readString());
        dataPacket.setBabyMob(dataPacket.readBoolean());
        dataPacket.setGlobal(dataPacket.readBoolean());
    }

    @Override
    protected void doEncode(LevelSoundEventV2Packet dataPacket, int version) {
        dataPacket.writeByte((byte) dataPacket.getEventId());
        positionFEncoder.encode(dataPacket, dataPacket.getPosition(), AbstractPacketHandler.VERSION_1_8);
        dataPacket.writeSignedVarInt(dataPacket.getData());
        dataPacket.writeString(dataPacket.getActorIdentifier());
        dataPacket.writeBoolean(dataPacket.isBabyMob());
        dataPacket.writeBoolean(dataPacket.isGlobal());
    }
}
