package com.particle.network.udp;

import com.particle.model.network.packets.data.BatchPacket;
import com.particle.model.utils.Pair;
import com.particle.network.AdvancedNetInterface;
import com.particle.network.NetworkManager;
import com.particle.network.PlayerConnection;
import com.particle.route.jraknet.Packet;
import com.particle.route.jraknet.RakNet;
import com.particle.route.jraknet.protocol.Reliability;
import com.particle.route.jraknet.server.RakNetServer;
import com.particle.route.jraknet.session.RakNetClientSession;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class UdpPackageHandler implements AdvancedNetInterface {

    private static final Logger logger = LoggerFactory.getLogger(UdpPackageHandler.class);

    private RakNetServer rakNetServer;

    private NetworkManager networkManager;

    public UdpPackageHandler(RakNetServer rakNetServer, NetworkManager networkManager) {
        this.rakNetServer = rakNetServer;
        this.networkManager = networkManager;
        this.networkManager.registerNetInterface(this);
    }

    @Override
    public Object onOpenSession(InetSocketAddress address, long clientId) {
        return this.networkManager.onCreateSession(this, address, clientId);
    }

    @Override
    public void onCloseSession(InetSocketAddress address) {
        this.networkManager.onCloseSession(address, null);
    }

    @Override
    public void onCloseSession(InetSocketAddress address, String reason) {
        this.networkManager.onCloseSession(address, reason);
    }

    @Override
    public void sendRawPacket(InetSocketAddress address, byte[] payload) {
        if (address == null || payload == null) {
            logger.error("param is null, so can't not send raw packet!");
            return;
        }
        try {
            RakNetClientSession session = this.rakNetServer.getSession(address);
            if (session == null) {
                String errorMsg = "can't get session ,so notify prophet close session!";
                logger.error(errorMsg);
                this.onCloseSession(address, errorMsg);
                return;
            }
            this.rakNetServer.sendNettyMessage(new Packet(payload), address);
        } catch (Exception e) {
            logger.error("send raw packet failed!", e);
        }
    }

    @Override
    public void sendPacket(InetSocketAddress address, BatchPacket packet) {
        if (address == null || packet == null) {
            return;
        }
        RakNetClientSession session = this.rakNetServer.getSession(address);
        if (session == null) {
            String errorMsg = "can't get session ,so notify prophet close session!";
            logger.error(errorMsg);
            this.onCloseSession(address, errorMsg);
            return;
        }

        if (!packet.isEncoded) {
            logger.error("batchPacket is not encode!");
            return;
        }

        session.sendMessage(
                Reliability.RELIABLE_ORDERED,
                RakNet.DEFAULT_CHANNEL,
                new Packet(Unpooled.copiedBuffer(packet.getBuffer())));
    }

    @Override
    public void blockAddress(String address, int timeout) {
        try {
            InetAddress addr = InetAddress.getByName(address);
            this.rakNetServer.blockAddress(addr, timeout);
            return;
        } catch (UnknownHostException uhe) {
            return;
        }
    }

    @Override
    public void unblockAddress(String address) {
        try {
            InetAddress addr = InetAddress.getByName(address);
            rakNetServer.unblockAddress(addr);
            return;
        } catch (UnknownHostException uhe) {
            return;
        }
    }

    @Override
    public void initiativeClose(InetSocketAddress address, String reason) {

    }

    @Override
    public boolean process() {
        Pair<Object, byte[]> receivePackagePair = null;
        while ((receivePackagePair = UdpPackageHandlerQueue.poll()) != null) {
            Object context = receivePackagePair.getKey();
            byte[] packageBuffer = receivePackagePair.getValue();
            if (context == null || packageBuffer == null) {
                logger.error("process failed! the received address or packets may be null!");
                continue;
            }
            PlayerConnection playerConnection = (PlayerConnection) context;
            playerConnection.handleSourceDataPacket(packageBuffer);
        }
        return true;
    }

    @Override
    public void shutdown() {
        this.rakNetServer.shutdown("shut down!");
    }

    @Override
    public void removePlayer(InetSocketAddress address, String reason) {
        this.rakNetServer.removeSession(address, reason);
    }
}
