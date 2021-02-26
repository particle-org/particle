package com.particle.network.udp;

import com.particle.network.AdvancedNetInterface;
import com.particle.route.jraknet.RakNetPacket;
import com.particle.route.jraknet.protocol.message.EncapsulatedPacket;
import com.particle.route.jraknet.protocol.message.acknowledge.Record;
import com.particle.route.jraknet.server.RakNetServerListener;
import com.particle.route.jraknet.server.ServerPing;
import com.particle.route.jraknet.session.RakNetClientSession;
import com.particle.route.jraknet.session.RakNetState;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class DispatcherRaknetListener implements RakNetServerListener {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherRaknetListener.class);

    private AdvancedNetInterface advancedSourceInterface;

    public DispatcherRaknetListener(AdvancedNetInterface advancedSourceInterface) {
        this.advancedSourceInterface = advancedSourceInterface;
    }

    @Override
    public void onServerStart() {
        logger.info("particle udp server start success!");
    }

    @Override
    public void onServerShutdown() {
        logger.info("raknet server shutdown!");
    }

    @Override
    public void handlePing(ServerPing ping) {
    }

    @Override
    public void onClientPreConnect(InetSocketAddress address) {
        logger.info("raknet server first connect!");
    }

    /**
     * 未完全建立连接，就断开连接
     *
     * @param address the address of the client.
     * @param reason
     */
    @Override
    public void onClientPreDisconnect(InetSocketAddress address, String reason) {
        advancedSourceInterface.onCloseSession(address, reason);
    }

    /**
     * 建立连接
     *
     * @param session
     */
    @Override
    public void onClientConnect(RakNetClientSession session) {
        Object result = advancedSourceInterface.onOpenSession(session.getAddress(), session.getGloballyUniqueId());
        session.setContext(result);
    }

    /**
     * 断开完整的连接
     *
     * @param session the client that disconnected.
     * @param reason
     */
    @Override
    public void onClientDisconnect(RakNetClientSession session, String reason) {
        advancedSourceInterface.onCloseSession(session.getAddress(), reason);
        session.setContext(null);
    }

    @Override
    public void onSessionException(RakNetClientSession session, Throwable throwable) {
        logger.info("raknet server onSessionException!");
    }

    @Override
    public void onAddressBlocked(InetAddress address, String reason, long time) {
        logger.info("raknet server onAddressBlocked!");
    }

    @Override
    public void onAddressUnblocked(InetAddress address) {
        logger.info("raknet server onAddressUnblocked!");
    }

    @Override
    public void onAcknowledge(RakNetClientSession session, Record record, EncapsulatedPacket packet) {

    }

    @Override
    public void onNotAcknowledge(RakNetClientSession session, Record record, EncapsulatedPacket packet) {

    }

    /**
     * 收游戏包
     *
     * @param session the client that sent the packet.
     * @param packet  the packet received from the client.
     * @param channel
     */
    @Override
    public void handleMessage(RakNetClientSession session, RakNetPacket packet, int channel) {
        if (session == null || session.getState() != RakNetState.CONNECTED) {
            logger.debug(String.format("the session[%s] is closed!", session));
            return;
        }
        if (packet == null) {
            return;
        }
        packet.buffer().readerIndex(0);
        ByteBuf byteBuf = packet.buffer();
        UdpPackageHandlerQueue.offer(session.getContext(), this.readByteBuf(byteBuf));
    }

    @Override
    public void handleUnknownMessage(RakNetClientSession session, RakNetPacket packet, int channel) {

    }

    @Override
    public void handleNettyMessage(ByteBuf buf, InetSocketAddress address) {

    }

    @Override
    public void onHandlerException(InetSocketAddress address, Throwable throwable) {

    }

    @Override
    public void onThreadException(Throwable throwable) {

    }

    @Override
    public void handleRawMessage(RakNetPacket packet, InetSocketAddress sender) {

    }

    /**
     * netty中的directBuffer，不是存在堆中的，没有array方法，需要业务拷贝一次
     *
     * @param byteBuf
     * @return
     */
    private byte[] readByteBuf(ByteBuf byteBuf) {
        if (byteBuf.hasArray()) {
            return byteBuf.array();
        } else {
            byte[] buffer = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(buffer);
            return buffer;
        }
    }
}
