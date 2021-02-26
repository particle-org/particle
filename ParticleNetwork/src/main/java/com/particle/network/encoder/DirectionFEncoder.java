package com.particle.network.encoder;

import com.particle.model.math.Direction;
import com.particle.model.network.packets.DataPacket;

public class DirectionFEncoder extends ModelHandler<Direction> {

    /**
     * 单例对象
     */
    private static final DirectionFEncoder INSTANCE = new DirectionFEncoder();

    /**
     * 获取单例
     */
    public static DirectionFEncoder getInstance() {
        return DirectionFEncoder.INSTANCE;
    }

    @Override
    public Direction decode(DataPacket dataPacket, int version) {
        return new Direction(dataPacket.readLFloat(), dataPacket.readLFloat(), dataPacket.readLFloat());
    }

    @Override
    public void encode(DataPacket dataPacket, Direction direction, int version) {
        dataPacket.writeLFloat(direction.getPitch());
        dataPacket.writeLFloat(direction.getYaw());
        dataPacket.writeLFloat(direction.getYawHead());
    }
}
