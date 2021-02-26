package com.particle.network.encoder;

import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;

public class PositionFEncoder extends ModelHandler<Vector3f> {

    /**
     * 单例对象
     */
    private static final PositionFEncoder INSTANCE = new PositionFEncoder();

    /**
     * 获取单例
     */
    public static PositionFEncoder getInstance() {
        return PositionFEncoder.INSTANCE;
    }

    @Override
    public Vector3f decode(DataPacket dataPacket, int version) {
        return new Vector3f(dataPacket.readLFloat(), dataPacket.readLFloat(), dataPacket.readLFloat());
    }

    @Override
    public void encode(DataPacket dataPacket, Vector3f vector3f, int version) {
        dataPacket.writeLFloat(vector3f.getX());
        dataPacket.writeLFloat(vector3f.getY());
        dataPacket.writeLFloat(vector3f.getZ());
    }
}
