package com.particle.network;

import java.net.InetSocketAddress;

/**
 * 网络端向上传递的接口
 */
public interface AdvancedNetInterface extends NetInterface {
    /**
     * 接收到报文打开session
     *
     * @param address
     * @param clientId
     */
    public Object onOpenSession(InetSocketAddress address, long clientId);

    /**
     * 接收到报文，closeSession
     *
     * @param address
     */
    public void onCloseSession(InetSocketAddress address);

    /**
     * 接收到报文，closeSession
     *
     * @param address
     * @param reason
     */
    public void onCloseSession(InetSocketAddress address, String reason);
}
