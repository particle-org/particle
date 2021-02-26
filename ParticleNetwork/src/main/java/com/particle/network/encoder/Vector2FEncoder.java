package com.particle.network.encoder;

import com.particle.model.math.Vector2f;
import com.particle.model.network.packets.DataPacket;

public class Vector2FEncoder extends ModelHandler<Vector2f> {

    /**
     * 单例对象
     */
    private static final Vector2FEncoder INSTANCE = new Vector2FEncoder();

    /**
     * 获取单例
     */
    public static Vector2FEncoder getInstance() {
        return Vector2FEncoder.INSTANCE;
    }

    @Override
    public Vector2f decode(DataPacket dataPacket, int version) {
        return new Vector2f(dataPacket.readLFloat(), dataPacket.readLFloat());
    }

    @Override
    public void encode(DataPacket dataPacket, Vector2f vector2f, int version) {
        dataPacket.writeLFloat(vector2f.getX());
        dataPacket.writeLFloat(vector2f.getY());
    }
}
