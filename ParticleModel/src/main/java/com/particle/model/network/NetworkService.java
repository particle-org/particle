package com.particle.model.network;

import com.particle.model.network.packets.DataPacket;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public interface NetworkService {

    /**
     * 对外提供接口，发送消息
     *
     * @param address    地址
     * @param dataPacket 数据包
     * @return 结果
     */
    public boolean sendMessage(InetSocketAddress address, DataPacket dataPacket);


    /**
     * 对外提供接口，发送消息
     *
     * @param address     地址
     * @param dataPackets 数据包
     * @return 结果
     */
    public boolean sendMessage(InetSocketAddress address, List<DataPacket> dataPackets);

    /**
     * 对外提供接口，发送消息
     *
     * @param address     地址
     * @param dataPackets 数据包
     * @return 结果
     */
    public boolean sendMessage(InetSocketAddress address, DataPacket[] dataPackets);

    /**
     * 对外提供接口，发送消息
     * 批量发送建议采用该方式
     *
     * @param addresses  地址
     * @param dataPacket 数据包
     * @return 结果
     */
    public boolean broadcastMessage(ArrayList<InetSocketAddress> addresses, DataPacket dataPacket);

    /**
     * 对外提供接口，发送消息
     * 批量发送建议使用该接口
     *
     * @param addresses   地址
     * @param dataPackets 数据包
     * @return 结果
     */
    public boolean broadcastMessage(ArrayList<InetSocketAddress> addresses, List<DataPacket> dataPackets);

    /**
     * ban ip
     *
     * @param address 地址
     */
    public void blockAddress(String address);

    /**
     * ban ip
     *
     * @param address 地址
     * @param timeout 时间，单位秒
     */
    public void blockAddress(String address, int timeout);

    /**
     * 解ban ip
     *
     * @param address 地址
     */
    public void unblockAddress(String address);

    /**
     * 获取网络配置
     *
     * @return 结果
     */
    public NetworkInfo getNetworkInfo();
}
