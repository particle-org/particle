package com.particle.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UuidUtils {

    /**
     * 将byte数组转成uuid
     *
     * @param bytes
     * @return
     */
    public static UUID toUuid(byte[] bytes) {
        if (bytes.length != 16) {
            return null;
        }
        return new UUID(readLLong(bytes), readLLong(new byte[]{
                bytes[8],
                bytes[9],
                bytes[10],
                bytes[11],
                bytes[12],
                bytes[13],
                bytes[14],
                bytes[15]
        }));
    }


    /**
     * 字节数组转成uuid
     *
     * @param uuid
     * @return
     */
    public static byte[] toBytes(UUID uuid) {
        return appendBytes(writeLLong(uuid.getMostSignificantBits()), writeLLong(uuid.getLeastSignificantBits()));
    }

    /**
     * 小端转换
     *
     * @param l
     * @return
     */
    public static byte[] writeLLong(long l) {
        return new byte[]{
                (byte) (l),
                (byte) (l >>> 8),
                (byte) (l >>> 16),
                (byte) (l >>> 24),
                (byte) (l >>> 32),
                (byte) (l >>> 40),
                (byte) (l >>> 48),
                (byte) (l >>> 56),
        };
    }

    /**
     * 小端转换
     *
     * @param bytes
     * @return
     */
    public static long readLLong(byte[] bytes) {
        return (((long) bytes[7] << 56) +
                ((long) (bytes[6] & 0xFF) << 48) +
                ((long) (bytes[5] & 0xFF) << 40) +
                ((long) (bytes[4] & 0xFF) << 32) +
                ((long) (bytes[3] & 0xFF) << 24) +
                ((bytes[2] & 0xFF) << 16) +
                ((bytes[1] & 0xFF) << 8) +
                ((bytes[0] & 0xFF)));
    }

    /**
     * 字节数组拼接
     *
     * @param bytes1
     * @param bytes2
     * @return
     */
    public static byte[] appendBytes(byte[] bytes1, byte[]... bytes2) {
        int length = bytes1.length;
        for (byte[] bytes : bytes2) {
            length += bytes.length;
        }
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(bytes1);
        for (byte[] bytes : bytes2) {
            buffer.put(bytes);
        }
        return buffer.array();
    }
}
