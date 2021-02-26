package com.particle.network.handler.v274;

import com.particle.model.entity.EntityType;
import com.particle.model.network.packets.data.MovePlayerPacket;
import com.particle.model.player.PositionMode;
import com.particle.model.player.TeleportationCause;
import com.particle.network.encoder.DirectionFEncoder;
import com.particle.network.encoder.PositionFEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class MovePlayerPacketHandler extends AbstractPacketHandler<MovePlayerPacket> {

    private PositionFEncoder positionEncoder = PositionFEncoder.getInstance();

    private DirectionFEncoder directionEncoder = DirectionFEncoder.getInstance();

    @Override
    protected void doDecode(MovePlayerPacket dataPacket, int version) {
        dataPacket.setEntityId(dataPacket.readUnsignedVarLong());
        dataPacket.setVector3f(positionEncoder.decode(dataPacket, version));
        dataPacket.setDirection(directionEncoder.decode(dataPacket, version));
        dataPacket.setMode(PositionMode.fromValue(dataPacket.readByte()));
        dataPacket.setOnGround(dataPacket.readBoolean());
        dataPacket.setRidingEntityId(dataPacket.readUnsignedVarLong());
        if (dataPacket.getMode() == PositionMode.TELEPORT) {
            dataPacket.setTeleportationCause(TeleportationCause.fromValue(dataPacket.readLInt()));
            dataPacket.setSourceEntityType(EntityType.fromValue(dataPacket.readLInt()));
        }
    }

    @Override
    protected void doEncode(MovePlayerPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getEntityId());
        positionEncoder.encode(dataPacket, dataPacket.getVector3f(), version);
        directionEncoder.encode(dataPacket, dataPacket.getDirection(), version);
        dataPacket.writeByte(dataPacket.getMode().value());
        dataPacket.writeBoolean(dataPacket.isOnGround());
        dataPacket.writeUnsignedVarLong(dataPacket.getRidingEntityId());
        if (dataPacket.getMode() == PositionMode.TELEPORT) {
            dataPacket.writeLInt(dataPacket.getTeleportationCause().value());
            dataPacket.writeLInt(dataPacket.getSourceEntityType().type());
        }
    }
}
