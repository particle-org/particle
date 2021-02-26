package com.particle.inputs.netease;

import com.particle.game.server.recharge.PayService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.NeteaseBuySuccessPacket;
import com.particle.model.player.Player;

import javax.inject.Singleton;

@Singleton
public class NeteaseBuySuccessOperation extends PlayerPacketHandle<NeteaseBuySuccessPacket> {
    @Override
    protected void handlePacket(Player player, NeteaseBuySuccessPacket packet) {
        PayService.handleBuySuccessOperation(player, packet.getContent());
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.NETEASE_BUY_SUCCESS_PACKET;
    }
}
