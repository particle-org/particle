package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class MapInfoRequestPacket extends DataPacket {

    private long mapId;

    @Override
    public int pid() {
        return ProtocolInfo.MAP_INFO_REQUEST_PACKET;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }
}
