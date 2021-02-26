package com.particle.network.udp;


import com.particle.model.utils.Pair;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UdpPackageHandlerQueue {

    /**
     * 从socket获取数据后，包装后，存在此队列里面
     */
    private static ConcurrentLinkedQueue<Pair<Object, byte[]>> receivePackageQueue = new ConcurrentLinkedQueue<Pair<Object, byte[]>>();


    /**
     * 推送数据到收包队列中
     */
    public static void offer(Object context, byte[] data) {
        receivePackageQueue.offer(new Pair<>(context, data));
    }

    /**
     * 从收包队列头中获取数据,同时会删除头
     * 若为空，返回null
     *
     * @return
     */
    public static Pair<Object, byte[]> poll() {
        return receivePackageQueue.poll();
    }
}
