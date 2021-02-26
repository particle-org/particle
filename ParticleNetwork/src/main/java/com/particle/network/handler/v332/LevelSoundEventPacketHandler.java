package com.particle.network.handler.v332;

import com.particle.model.network.packets.data.LevelSoundEventPacket;
import com.particle.network.encoder.PositionFEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class LevelSoundEventPacketHandler extends AbstractPacketHandler<LevelSoundEventPacket> {

    private PositionFEncoder positionFEncoder = PositionFEncoder.getInstance();

    @Override
    protected void doDecode(LevelSoundEventPacket dataPacket, int version) {
        dataPacket.setEventId(dataPacket.readUnsignedVarInt());
        dataPacket.setPosition(positionFEncoder.decode(dataPacket, AbstractPacketHandler.VERSION_1_9));
        dataPacket.setData(dataPacket.readSignedVarInt());
        dataPacket.setActorIdentifier(dataPacket.readString());
        dataPacket.setBabyMob(dataPacket.readBoolean());
        dataPacket.setGlobal(dataPacket.readBoolean());
    }

    @Override
    protected void doEncode(LevelSoundEventPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarInt(dataPacket.getEventId());
        positionFEncoder.encode(dataPacket, dataPacket.getPosition(), AbstractPacketHandler.VERSION_1_9);
        dataPacket.writeSignedVarInt(dataPacket.getData());
        dataPacket.writeString(dataPacket.getActorIdentifier());
        dataPacket.writeBoolean(dataPacket.isBabyMob());
        dataPacket.writeBoolean(dataPacket.isGlobal());
    }
}
