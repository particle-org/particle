package com.particle.event.handle;


import com.particle.model.network.packets.DataPacket;

import java.net.InetSocketAddress;

public interface PacketEventHandle extends EventHandle {
    int getTargetPacketID();

    void handle(InetSocketAddress address, DataPacket serverPacketEvent);
}
