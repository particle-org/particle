package com.particle.inputs.action;

import com.particle.game.player.PlayerService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.RespawnPacket;
import com.particle.model.player.Player;
import com.particle.model.player.spawn.PlayerRespawnState;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerRespawnPacketHandle extends PlayerPacketHandle<RespawnPacket> {

    @Inject
    private PlayerService playerService;

    @Override
    protected void handlePacket(Player player, RespawnPacket packet) {
        if (packet.getState() == PlayerRespawnState.CLIENT_READY_TO_SPAWN) {
            this.playerService.respawnPlayer(player);
        }
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.RESPAWN_PACKET;
    }
}
