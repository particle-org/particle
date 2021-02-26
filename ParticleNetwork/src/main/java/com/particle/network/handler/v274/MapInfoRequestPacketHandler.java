package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.MapInfoRequestPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class MapInfoRequestPacketHandler extends AbstractPacketHandler<MapInfoRequestPacket> {
    @Override
    protected void doDecode(MapInfoRequestPacket dataPacket, int version) {
        dataPacket.setMapId(dataPacket.readSignedVarLong().longValue());
    }

    @Override
    protected void doEncode(MapInfoRequestPacket dataPacket, int version) {
        dataPacket.writeSignedVarLong(dataPacket.getMapId());
    }
}
