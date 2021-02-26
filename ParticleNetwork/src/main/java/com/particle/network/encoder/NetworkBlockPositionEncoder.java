package com.particle.network.encoder;

import com.particle.model.math.Vector3;
import com.particle.model.network.packets.DataPacket;

public class NetworkBlockPositionEncoder extends ModelHandler<Vector3> {

    /**
     * 单例对象
     */
    private static final NetworkBlockPositionEncoder INSTANCE = new NetworkBlockPositionEncoder();

    /**
     * 获取单例
     */
    public static NetworkBlockPositionEncoder getInstance() {
        return NetworkBlockPositionEncoder.INSTANCE;
    }

    @Override
    public Vector3 decode(DataPacket dataPacket, int version) {
        return new Vector3(dataPacket.readSignedVarInt(), dataPacket.readUnsignedVarInt(), dataPacket.readSignedVarInt());
    }

    @Override
    public void encode(DataPacket dataPacket, Vector3 positionF, int version) {
        dataPacket.writeSignedVarInt(positionF.getX());
        dataPacket.writeUnsignedVarInt(positionF.getY());
        dataPacket.writeSignedVarInt(positionF.getZ());
    }
}
