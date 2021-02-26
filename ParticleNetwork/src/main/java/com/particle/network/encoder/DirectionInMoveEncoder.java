package com.particle.network.encoder;

import com.particle.model.math.Direction;
import com.particle.model.network.packets.DataPacket;

public class DirectionInMoveEncoder extends ModelHandler<Direction> {

    /**
     * 单例对象
     */
    private static final DirectionInMoveEncoder INSTANCE = new DirectionInMoveEncoder();

    /**
     * 获取单例
     */
    public static DirectionInMoveEncoder getInstance() {
        return DirectionInMoveEncoder.INSTANCE;
    }

    @Override
    public Direction decode(DataPacket dataPacket, int version) {
        return new Direction((float) (dataPacket.readByte() * (360d / 256d)), (float) (dataPacket.readByte() * (360d / 256d)), (float) (dataPacket.readByte() * (360d / 256d)));
    }

    @Override
    public void encode(DataPacket dataPacket, Direction direction, int version) {
        dataPacket.writeByte((byte) (direction.getPitch() / (360d / 256d)));
        dataPacket.writeByte((byte) (direction.getYaw() / (360d / 256d)));
        dataPacket.writeByte((byte) (direction.getYawHead() / (360d / 256d)));
    }
}
