package com.particle.inputs;

import com.particle.game.server.Server;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.InetSocketAddress;

public abstract class PlayerPacketHandle<T extends DataPacket> extends ServerPacketHandle<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerPacketHandle.class);

    @Inject
    private Server server;

    @Override
    public void handle(InetSocketAddress address, DataPacket packet) {
        T castedPacket = castPacket(packet);

        if (castedPacket == null) {
            LOGGER.error("Fail to case packet");

            return;
        }

        Player player = server.getOnlinePlayer(address);

        if (player == null) {
            LOGGER.error("Player packet received without entity session");

            return;
        }

        if (isRealTimeHandle()) {
            player.getLevel().getLevelSchedule().scheduleRealtimeTask("PlayerPacketHandle_" + packet.getClass().getSimpleName(), () -> this.handlePacket(player, castedPacket), 1000);
        } else {
            player.getLevel().getLevelSchedule().scheduleSimpleTask("PlayerPacketHandle_" + packet.getClass().getSimpleName(), () -> this.handlePacket(player, castedPacket));
        }
    }

    protected boolean isRealTimeHandle() {
        return false;
    }
}
