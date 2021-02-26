package com.particle.network;

import com.particle.model.network.packets.data.BatchPacket;

import java.net.InetSocketAddress;

/**
 * 业务层调用网络端的接口
 */
public interface NetInterface {

    /**
     * 发送原始消息
     *
     * @param address
     * @param payload
     */
    public void sendRawPacket(InetSocketAddress address, byte[] payload);

    /**
     * 发送dataPackage
     *
     * @param address
     * @param packet
     * @return
     */
    public void sendPacket(InetSocketAddress address, BatchPacket packet);

    /**
     * ban掉address
     *
     * @param address
     * @param timeout
     */
    public void blockAddress(String address, int timeout);

    /**
     * 解ban掉
     *
     * @param address
     */
    public void unblockAddress(String address);

    /**
     * 主动关闭
     *
     * @param address
     * @param reason
     */
    public void initiativeClose(InetSocketAddress address, String reason);

    /**
     * 处理报文
     *
     * @return
     */
    public boolean process();

    /**
     * 关闭
     */
    public void shutdown();

    /**
     * 服务端主动去除某个玩家
     *
     * @param reason
     */
    public void removePlayer(InetSocketAddress address, String reason);

}
