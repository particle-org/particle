package com.particle.network.tcp;

import com.particle.network.AdvancedNetInterface;
import com.particle.route.jraknet.RakNetPacket;
import com.particle.route.jraknet.protocol.message.EncapsulatedPacket;
import com.particle.route.jraknet.protocol.message.acknowledge.Record;
import com.particle.route.jraknet.server.ServerPing;
import com.particle.route.jraknet.session.RakNetState;
import com.particle.route.jraknet.tcpnet.server.DecoServerListener;
import com.particle.route.jraknet.tcpnet.session.LogicClientSession;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class DispatcherDecoListener implements DecoServerListener {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherDecoListener.class);

    private AdvancedNetInterface advancedSourceInterface;

    public DispatcherDecoListener(AdvancedNetInterface advancedSourceInterface) {
        this.advancedSourceInterface = advancedSourceInterface;
    }

    /**
     * server启动的时候回被调用
     */
    @Override
    public void onServerStart() {
        logger.info("particle tcp server start success!");
    }

    /**
     * server被shundown的时候会被调用
     */
    @Override
    public void onServerShutdown() {
        logger.info("decoServer onServerShutdown!");
    }

    /**
     * 当收到客户端packageId为0x01的时候会被调用
     *
     * @param ping
     */
    @Override
    public void handlePing(ServerPing ping) {
        logger.info("decoServer handlePing!");
    }

    /**
     * 当收到连接请求0x07后，会被调用，此时最后一次连接请求已完成，稍后服务端会建立session会话
     *
     * @param address
     */
    @Override
    public void onClientPreConnect(InetSocketAddress address) {

    }

    /**
     * 当先前建立的session被从hashmap中去除的时候，会被调用。（此时的session的状态不为已连接）
     * 此时应该通知到业务层close消息
     *
     * @param address
     * @param reason
     */
    @Override
    public void onClientPreDisconnect(InetSocketAddress address, String reason) {
        advancedSourceInterface.onCloseSession(address, reason);
    }

    /**
     * 当收到custom包下的具体业务包的包头是0x13的时候，会被调用，表示握手阶段结束
     * 此时应该通知业务层open消息
     *
     * @param session
     */
    @Override
    public void onClientConnect(LogicClientSession session) {
        Object result = advancedSourceInterface.onOpenSession(session.getAddress(), session.getGloballyUniqueId());
        session.setContext(result);
    }

    /**
     * 当先前建立的session被从hashmap中去除的时候，会被调用。（此时的session的状态为已连接）
     * 此时应该通知到业务层close消息
     *
     * @param session
     * @param reason
     */
    @Override
    public void onClientDisconnect(LogicClientSession session, String reason) {
        advancedSourceInterface.onCloseSession(session.getAddress(), reason);
        session.setContext(null);
    }

    /**
     * 更新session的操作出现异常，会被调用
     *
     * @param session
     * @param throwable
     */
    @Override
    public void onSessionException(LogicClientSession session, Throwable throwable) {

    }

    /**
     * 当address block会被调用
     *
     * @param address
     * @param reason
     * @param time
     */
    @Override
    public void onAddressBlocked(InetAddress address, String reason, long time) {

    }

    /**
     * 当address umblock会被调用
     *
     * @param address
     */
    @Override
    public void onAddressUnblocked(InetAddress address) {

    }

    /**
     * 当收到ack包的时候，会被调用
     * tcp没有ack消息
     *
     * @param session
     * @param record
     * @param packet
     */
    @Override
    public void onAcknowledge(LogicClientSession session, Record record, EncapsulatedPacket packet) {

    }

    /**
     * 当收到not ack包的时候，会被调用
     * tcp没有ack消息
     *
     * @param session
     * @param record
     * @param packet
     */
    @Override
    public void onNotAcknowledge(LogicClientSession session, Record record, EncapsulatedPacket packet) {

    }

    /**
     * 处理真正的message
     *
     * @param session
     * @param packet
     * @param channel
     */
    @Override
    public void handleMessage(LogicClientSession session, RakNetPacket packet, int channel) {
        if (session == null || session.getState() != RakNetState.CONNECTED) {
            logger.debug(String.format("the session[%s] is closed!", session));
            return;
        }
        if (packet == null) {
            return;
        }
        packet.buffer().readerIndex(0);
        ByteBuf byteBuf = packet.buffer();
        TcpPackageHandlerQueue.offer(session.getContext(), this.readByteBuf(byteBuf));
    }

    /**
     * 处理未知的message
     *
     * @param session the client that sent the packet.
     * @param packet  the packet received from the client.
     * @param channel
     */
    @Override
    public void handleUnknownMessage(LogicClientSession session, RakNetPacket packet, int channel) {

    }

    /**
     * 处理所有的原生netty消息
     *
     * @param buf     the packet buffer.
     * @param address
     */
    @Override
    public void handleNettyMessage(ByteBuf buf, InetSocketAddress address) {

    }

    /**
     * netty线程出现异常的时候调用
     *
     * @param address   the address that caused the exception.
     * @param throwable
     */
    @Override
    public void onHandlerException(InetSocketAddress address, Throwable throwable) {

    }

    /**
     * 启动tcpserver的时候出现异常，会被调用
     *
     * @param throwable
     */
    @Override
    public void onThreadException(Throwable throwable) {

    }

    /**
     * 暂没有逻辑处理
     *
     * @param packet
     * @param sender
     */
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
