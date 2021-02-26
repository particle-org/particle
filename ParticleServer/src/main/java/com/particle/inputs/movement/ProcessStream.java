package com.particle.inputs.movement;

import com.particle.event.handle.PacketEventHandle;
import com.particle.game.server.Server;
import com.particle.model.level.Level;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.InetSocketAddress;

public abstract class ProcessStream<T extends DataPacket> implements PacketEventHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessStream.class);

    @Inject
    private Server server;

    @Override
    public void handle(InetSocketAddress address, DataPacket packet) {
        T castedPacket = castPacket(packet);

        if (castedPacket != null) {
            Player player = this.server.getOnlinePlayer(address);

            Level level = player.getLevel();
            if (level == null) {
                return;
            }

            if (isRealTimeHandle()) {
                level.getLevelSchedule().scheduleRealtimeTask("PlayerPacketHandle_" + packet.getClass().getSimpleName(), () -> this.process(player, castedPacket), 1000);
            } else {
                level.getLevelSchedule().scheduleSimpleTask("PlayerPacketHandle_" + packet.getClass().getSimpleName(), () -> this.process(player, castedPacket));
            }
        } else {
            LOGGER.error("Fail to case packet");
        }
    }

    protected abstract void process(Player player, T packet);

    private T castPacket(DataPacket packet) {
        if (packet == null) {
            return null;
        }

        T casedPacket = null;
        try {
            casedPacket = (T) packet;
        } catch (ClassCastException e) {
            LOGGER.error("Fail to case packet", e);

            return null;
        }

        return casedPacket;
    }

    protected boolean isRealTimeHandle() {
        return false;
    }
}
