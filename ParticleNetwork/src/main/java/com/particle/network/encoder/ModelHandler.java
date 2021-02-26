package com.particle.network.encoder;

import com.particle.model.network.packets.DataPacket;

public abstract class ModelHandler<T> {

    public abstract T decode(DataPacket dataPacket, int version);

    public abstract void encode(DataPacket dataPacket, T t, int version);
}
