package com.particle.util.deflater;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * The type Deflater tool.
 */
public class DeflaterTool {

    private static DeflaterBufferPool deflaterBufferPool = new DeflaterBufferPool();

    /**
     * 压缩数组中的数据.
     *
     * @param data 待压缩的数据
     * @return 压缩结果
     * @throws IOException the io exception
     */
    public static ByteBuf compress(byte[] data, boolean isNeedHead) throws IOException {
        ByteBuf packetBuffer = Unpooled.buffer(data.length > 128 ? 128 : data.length);

        Deflater compressor = new Deflater(7, !isNeedHead);
        byte[] buf = deflaterBufferPool.requestBuffer();
        try {
            compressor.setInput(data);
            compressor.finish();

            while (!compressor.finished()) {
                int count = compressor.deflate(buf);
                packetBuffer.writeBytes(buf, 0, count);
            }
        } finally {
            compressor.end();
            deflaterBufferPool.returnBuffer(buf);
        }

        return packetBuffer;
    }

    /**
     * 解压缩数组中的数据.
     *
     * @param data 待解压的数据
     * @return 解压缩结果
     * @throws IOException the io exception
     */
    public static ByteBuf uncompress(byte[] data, boolean isNeedHead) throws IOException {
        ByteBuf packetBuffer = Unpooled.buffer();

        Inflater decompressor = new Inflater(!isNeedHead);
        byte[] buf = deflaterBufferPool.requestBuffer();
        try {
            decompressor.setInput(data);
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);

                packetBuffer.writeBytes(buf, 0, count);
            }
        } catch (DataFormatException e) {
            throw new IOException(e.getMessage());
        } finally {
            decompressor.end();
            deflaterBufferPool.returnBuffer(buf);
        }

        return packetBuffer;
    }
}
