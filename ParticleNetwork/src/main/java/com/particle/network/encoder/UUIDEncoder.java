package com.particle.network.encoder;

import com.particle.model.network.packets.DataPacket;

import java.util.UUID;

public class UUIDEncoder extends ModelHandler<UUID> {

    /**
     * 单例对象
     */
    private static final UUIDEncoder INSTANCE = new UUIDEncoder();

    /**
     * 获取单例
     */
    public static UUIDEncoder getInstance() {
        return UUIDEncoder.INSTANCE;
    }

    @Override
    public UUID decode(DataPacket dataPacket, int version) {
        return new UUID(dataPacket.readLLong(), dataPacket.readLLong());
    }

    @Override
    public void encode(DataPacket dataPacket, UUID uuid, int version) {
        dataPacket.writeLLong(uuid.getMostSignificantBits());
        dataPacket.writeLLong(uuid.getLeastSignificantBits());
    }
}
