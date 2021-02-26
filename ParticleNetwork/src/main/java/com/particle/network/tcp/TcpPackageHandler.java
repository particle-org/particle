package com.particle.network.tcp;

import com.particle.model.network.packets.data.BatchPacket;
import com.particle.model.utils.Pair;
import com.particle.network.AdvancedNetInterface;
import com.particle.network.NetworkManager;
import com.particle.network.PlayerConnection;
import com.particle.route.jraknet.Packet;
import com.particle.route.jraknet.RakNet;
import com.particle.route.jraknet.protocol.Reliability;
import com.particle.route.jraknet.tcpnet.server.DecoServer;
import com.particle.route.jraknet.tcpnet.session.LogicClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class TcpPackageHandler implements AdvancedNetInterface {

    private static final Logger logger = LoggerFactory.getLogger(TcpPackageHandler.class);

    private DecoServer decoServer = null;

    private NetworkManager networkManager;

    /**
     * todo 统计消息
     */
    private Map<InetSocketAddress, Integer> totalIndexs = new HashMap<>();

    private int bubleIndex(InetSocketAddress address) {
        Integer value = this.totalIndexs.get(address);
        if (value == null) {
            value = 1;
        }
        value = value + 1;
        this.totalIndexs.put(address, value);
        return value;
    }

    public TcpPackageHandler(DecoServer decoServer, NetworkManager networkManager) {
        this.decoServer = decoServer;
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
        this.totalIndexs.remove(address);
    }

    @Override
    public void onCloseSession(InetSocketAddress address, String reason) {
        this.networkManager.onCloseSession(address, reason);
        this.totalIndexs.remove(address);
    }

    @Override
    public void sendRawPacket(InetSocketAddress address, byte[] payload) {
        if (address == null || payload == null) {
            logger.error("param is null, so can't not send raw packet!");
            return;
        }
        try {
            LogicClientSession session = decoServer.getLogicSession(address);
            if (session == null || session.getPhysicalSession() == null) {
                String errorMsg = "can't get session ,so notify prophet close session!";
                logger.error(errorMsg);
                this.onCloseSession(address, errorMsg);
                return;
            }
            this.decoServer.sendNettyMessage(session.getPhysicalSession().getChannel(), new Packet(payload), address);
        } catch (Exception e) {
            logger.error("send raw packet failed!", e);
        }
    }

    @Override
    public void sendPacket(InetSocketAddress address, BatchPacket packet) {
        if (address == null || packet == null) {
            return;
        }
        LogicClientSession session = decoServer.getLogicSession(address);
        if (session == null) {
            String errorMsg = "when sendPacket can't get session ,so notify prophet close session!";
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
                new Packet(packet.getBuffer()));
        return;
    }

    @Override
    public void blockAddress(String address, int timeout) {
        try {
            InetAddress addr = InetAddress.getByName(address);
            this.decoServer.blockAddress(addr, timeout);
            return;
        } catch (UnknownHostException uhe) {
            return;
        }
    }

    @Override
    public void unblockAddress(String address) {
        try {
            InetAddress addr = InetAddress.getByName(address);
            decoServer.unblockAddress(addr);
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
        while ((receivePackagePair = TcpPackageHandlerQueue.poll()) != null) {
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
        this.decoServer.shutdown("shut down!");
    }

    @Override
    public void removePlayer(InetSocketAddress address, String reason) {
        this.decoServer.removeSession(address, reason);
    }
}
