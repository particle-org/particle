package com.particle.util.deflater;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DeflaterBufferPool {

    private static final int BUFFER_SIZE = 2000;
    private static final int POOL_SIZE = 30;

    private Queue<byte[]> pool = new ConcurrentLinkedQueue<>();

    /**
     * 申请Buffer
     *
     * @return
     */
    public byte[] requestBuffer() {
        byte[] buffer = this.pool.poll();

        return buffer == null ? new byte[BUFFER_SIZE] : buffer;
    }

    /**
     * 返回Buffer
     */
    public void returnBuffer(byte[] buffer) {
        // 避免内存泄漏
        if (pool.size() > POOL_SIZE) {
            return;
        } else {
            pool.add(buffer);
        }
    }
}
