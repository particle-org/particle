package com.particle.game.world.level.convert;

import com.particle.model.level.chunk.ChunkSection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ChunkSection2SaveBuffer {

    /**
     * 将ChunkSection转换成适用于存储的二进制流
     *
     * @param chunkSection
     * @return
     */
    public static ByteBuf toSaveFormat(ChunkSection chunkSection) {
        ByteBuf byteBuf = Unpooled.buffer(12289);

        // y
        byteBuf.writeByte(chunkSection.getY());

        // 填充id信息
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    int blockId = chunkSection.getBlockId(x, y, z);
                    int meta = chunkSection.getBlockData(x, y, z);

                    // meta + id
                    byteBuf.writeByte(((meta << 2) | (blockId >>> 8)) & 0xff);
                    // id
                    byteBuf.writeByte((blockId & 0xff));
                }
            }
        }

        // 填充light信息
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    // light
                    byteBuf.writeByte((chunkSection.getBlockLight(x, y, z) << 4) | chunkSection.getBlockSkyLight(x, y, z));
                }
            }
        }

        return byteBuf;
    }

    /**
     * 将二进制数据流转成ChunkSection
     *
     * @param data
     * @return
     */
    public static ChunkSection fromSaveFormat(ByteBuf data) {
        ChunkSection chunkSection = new ChunkSection(data.readByte());

        // 填充id信息
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    int data1 = data.readByte();
                    int data2 = data.readByte();

                    chunkSection.setBlock(x, y, z, ((data1 & 0b11) << 8) | (data2 & 0xff), data1 >>> 2);
                }
            }
        }

        // 填充light信息
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    // light
                    byte light = data.readByte();

                    chunkSection.setBlockLight(x, y, z, (byte) (light >> 4));
                    chunkSection.setBlockSkyLight(x, y, z, (byte) (light & 0b1111));
                }
            }
        }

        return chunkSection;
    }
}
