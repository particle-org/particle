package com.particle.game.world.level.convert;

import com.particle.model.level.chunk.ChunkSection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ChunkSection2SaveBinary {

    /**
     * 将ChunkSection转换成适用于存储的二进制流
     *
     * @param chunkSection
     * @return
     */
    public static byte[] toSaveFormat(ChunkSection chunkSection) {
        byte[] buff = new byte[12289];
        int index = 0;

        // y
        buff[index++] = chunkSection.getY();

        // 填充id信息
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    int blockId = chunkSection.getBlockId(x, y, z);
                    int meta = chunkSection.getBlockData(x, y, z);

                    // meta + id
                    buff[index++] = (byte) ((meta << 2) | (blockId >>> 8) & 0xff);
                    // id
                    buff[index++] = (byte) (blockId & 0xff);
                }
            }
        }

        // 填充light信息
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    // light
                    buff[index++] = (byte) ((chunkSection.getBlockLight(x, y, z) << 4) | chunkSection.getBlockSkyLight(x, y, z));
                }
            }
        }

        return buff;
    }

    /**
     * 将二进制数据流转成ChunkSection
     *
     * @param data
     * @return
     */
    public static ChunkSection fromSaveFormat(byte[] data) {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(data);

        ChunkSection chunkSection = new ChunkSection(byteBuf.readByte());

        // 填充id信息
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    int data1 = byteBuf.readByte();
                    int data2 = byteBuf.readByte();

                    chunkSection.setBlock(x, y, z, ((data1 & 0b11) << 8) | (data2 & 0xff), data1 >>> 2);
                }
            }
        }

        // 填充light信息
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    // light
                    byte light = byteBuf.readByte();

                    chunkSection.setBlockLight(x, y, z, (byte) (light >> 4));
                    chunkSection.setBlockSkyLight(x, y, z, (byte) (light & 0b1111));
                }
            }
        }

        return chunkSection;
    }
}
