package com.particle.inputs.map;

import com.particle.game.world.map.MapService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.MapInfoRequestPacket;
import com.particle.model.player.Player;

import javax.inject.Inject;

public class MapRequestHandle extends PlayerPacketHandle<MapInfoRequestPacket> {

    @Inject
    private MapService mapService;

    @Override
    protected void handlePacket(Player player, MapInfoRequestPacket packet) {
        this.mapService.sendPlayerMapData(player, packet.getMapId());
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.MAP_INFO_REQUEST_PACKET;
    }
}
