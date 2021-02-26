package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class ChunkRadiusUpdatePacket extends DataPacket {

    private int radius;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public int pid() {
        return ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET;
    }
}
