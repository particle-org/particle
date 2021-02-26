package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.MoveEntityPacket;
import com.particle.network.encoder.DirectionInMoveEncoder;
import com.particle.network.encoder.PositionFEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class MoveEntityPacketHandler extends AbstractPacketHandler<MoveEntityPacket> {

    private static final byte MOVE_ONGROUND = 0b1;
    private static final byte MOVE_TELEPORT = 0b10;

    private PositionFEncoder positionEncoder = PositionFEncoder.getInstance();

    private DirectionInMoveEncoder directionEncoder = DirectionInMoveEncoder.getInstance();

    @Override
    protected void doDecode(MoveEntityPacket dataPacket, int version) {
        dataPacket.setEntityId(dataPacket.readUnsignedVarLong());

        int flag = dataPacket.readByte();
        if ((flag & MOVE_ONGROUND) > 0) {
            dataPacket.setOnGround(true);
        } else {
            dataPacket.setOnGround(false);
        }

        if ((flag & MOVE_TELEPORT) > 0) {
            dataPacket.setTeleport(true);
        } else {
            dataPacket.setTeleport(false);
        }

        dataPacket.setVector3f(positionEncoder.decode(dataPacket, version));
        dataPacket.setDirection(directionEncoder.decode(dataPacket, version));
    }

    @Override
    protected void doEncode(MoveEntityPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getEntityId());

        byte flag = 0;
        if (dataPacket.isOnGround()) flag = (byte) (flag | MOVE_ONGROUND);
        if (dataPacket.isTeleport()) flag = (byte) (flag | MOVE_TELEPORT);
        dataPacket.writeByte(flag);

        positionEncoder.encode(dataPacket, dataPacket.getVector3f(), version);
        directionEncoder.encode(dataPacket, dataPacket.getDirection(), version);
    }
}
