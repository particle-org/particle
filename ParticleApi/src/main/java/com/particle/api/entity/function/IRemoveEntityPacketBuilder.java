package com.particle.api.entity.function;

import com.particle.model.network.packets.DataPacket;

@FunctionalInterface
public interface IRemoveEntityPacketBuilder {
    DataPacket[] buildPacket();
}