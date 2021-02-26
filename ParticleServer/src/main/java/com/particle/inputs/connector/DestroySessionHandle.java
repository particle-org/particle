package com.particle.inputs.connector;

import com.particle.game.server.Server;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.OnDestroySessionPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DestroySessionHandle extends PlayerPacketHandle<OnDestroySessionPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestroySessionHandle.class);

    @Inject
    private Server server;

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.ON_DESTROY_SESSION_HEAD;
    }

    @Override
    protected void handlePacket(Player player, OnDestroySessionPacket onDestroySessionPacket) {
        player.getLevel().getLevelSchedule().scheduleSimpleTask("ClosePlayer", () -> {
            this.server.onPlayerConnectionDestroyed(player.getClientAddress());
        });
    }
}
