package com.particle.inputs.action;

import com.particle.game.entity.link.EntityMountControlService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.PlayerInputPacket;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerInputPacketHandle extends PlayerPacketHandle<PlayerInputPacket> {

    @Inject
    private EntityMountControlService entityMountControlService;

    @Override
    protected void handlePacket(Player player, PlayerInputPacket packet) {
        this.entityMountControlService.setAim(player, packet.getMoveVector());
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.PLAYER_INPUT_PACKET;
    }
}
