package com.particle.inputs;

import com.particle.event.handle.PacketEventHandle;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public abstract class ServerPacketHandle<T extends DataPacket> implements PacketEventHandle {

    private static final Logger logger = LoggerFactory.getLogger(ServerPacketHandle.class);

    @Override
    public void handle(InetSocketAddress address, DataPacket packet) {
        T castedPacket = castPacket(packet);

        if (castedPacket != null) {
            this.handlePacket(null, castPacket(packet));
        } else {
            logger.error("Fail to case packet");
        }
    }

    protected abstract void handlePacket(Player player, T packet);

    protected T castPacket(DataPacket packet) {
        if (packet == null) {
            return null;
        }

        T casedPacket = null;
        try {
            casedPacket = (T) packet;
        } catch (ClassCastException e) {
            logger.error("Fail to case packet", e);

            return null;
        }

        return casedPacket;
    }
}
