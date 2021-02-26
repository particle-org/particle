package com.particle.model.entity.component.position;

import com.particle.model.network.packets.DataPacket;

@FunctionalInterface
public interface IMoveEntityPacketBuilder {
    DataPacket build();
}