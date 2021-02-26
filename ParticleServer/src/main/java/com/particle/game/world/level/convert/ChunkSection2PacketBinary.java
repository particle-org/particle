package com.particle.game.world.level.convert;

import com.particle.model.block.types.BlockPrototype;
import com.particle.model.block.types.BlockPrototypeDictionary;
import com.particle.model.level.chunk.ChunkSection;
import com.particle.model.network.packets.PacketBuffer;

import java.nio.ByteBuffer;

public class ChunkSection2PacketBinary {

    private static BlockPrototypeDictionary blockPrototypeDictionary = new BlockPrototypeDictionary();

    /**
     * 将ChunkSection转换成适用于发包的二进制流（原版）
     *
     * @param section
     * @return
     */
    public static byte[] getBytes(ChunkSection section) {
        ByteBuffer buffer = ByteBuffer.allocate(6144);
        byte[] blocks = new byte[4096];
        byte[] data = new byte[2048];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int i = (x << 7) | (z << 3);
                for (int y = 0; y < 16; y += 2) {
                    blocks[(i << 1) | y] = (byte) section.getBlockId(x, y, z);
                    blocks[(i << 1) | (y + 1)] = (byte) section.getBlockId(x, y + 1, z);
                    int b1 = section.getBlockData(x, y, z);
                    int b2 = section.getBlockData(x, y + 1, z);
                    data[i | (y >> 1)] = (byte) ((b2 << 4) | b1);
                }
            }
        }
        return buffer
                .put(blocks)
                .put(data)
                .array();
    }

    /**
     * 将ChunkSection转换成适用于发包的二进制流
     *
     * @param section
     * @return
     */
    public static byte[] getPacketFormatBytes(ChunkSection section) {
        byte[] buff = new byte[6144];

        if (section == null) {
            return buff;
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int i = (x << 8) | (z << 4);

                for (int y = 0; y < 16; y += 2) {
                    buff[i | y] = (byte) section.getBlockId(x, y, z);
                    buff[i | (y + 1)] = (byte) section.getBlockId(x, y + 1, z);
                    buff[4096 + ((i | y) >>> 1)] = (byte) ((section.getBlockData(x, y + 1, z) << 4) | section.getBlockData(x, y, z));
                }
            }
        }

        return buff;
    }

    /**
     * 将ChunkSection转换成适用于发包的二进制流（快速编码版本）
     *
     * @param section
     * @return
     */
    public static byte[] getPacketFormatBytesQuicker(ChunkSection section) {
        byte[] buff = new byte[6144];

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int i = (x << 8) | (z << 4);

                int y = 0;
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y++, z);
                buff[i | y] = (byte) section.getBlockId(x, y, z);

                y = 0;
                buff[4096 + ((i | y) >> 1)] = (byte) ((section.getBlockData(x, y++, z) << 4) | section.getBlockData(x, y++, z));
                buff[4096 + ((i | y) >> 1)] = (byte) ((section.getBlockData(x, y++, z) << 4) | section.getBlockData(x, y++, z));
                buff[4096 + ((i | y) >> 1)] = (byte) ((section.getBlockData(x, y++, z) << 4) | section.getBlockData(x, y++, z));
                buff[4096 + ((i | y) >> 1)] = (byte) ((section.getBlockData(x, y++, z) << 4) | section.getBlockData(x, y++, z));
                buff[4096 + ((i | y) >> 1)] = (byte) ((section.getBlockData(x, y++, z) << 4) | section.getBlockData(x, y++, z));
                buff[4096 + ((i | y) >> 1)] = (byte) ((section.getBlockData(x, y++, z) << 4) | section.getBlockData(x, y++, z));
                buff[4096 + ((i | y) >> 1)] = (byte) ((section.getBlockData(x, y++, z) << 4) | section.getBlockData(x, y++, z));
                buff[4096 + ((i | y) >> 1)] = (byte) ((section.getBlockData(x, y++, z) << 4) | section.getBlockData(x, y, z));
            }
        }

        return buff;
    }

    /**
     * 将ChunkSection转换成适用于发包的二进制流（RuntimeID版本）
     *
     * @param chunkSection
     * @return
     */
    public static byte[] getPacketFormatBytesV2(ChunkSection chunkSection) {
        PacketBuffer buff = new PacketBuffer();

        if (chunkSection == null) {
            buff.writeByte((byte) 3);

            for (int i = 0; i < 512; i++) {
                buff.writeByte((byte) 0);
            }

            buff.writeSignedVarInt(1);
            buff.writeSignedVarInt(0);

            return buff.readAll();
        } else {
            BlockIdMapBuilder blockIdMapBuilder = new BlockIdMapBuilder();

            buff.writeByte((byte) 33);
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 16; y++) {
                        short indexId = (short) blockIdMapBuilder.getIndexId(getBlockRuntimeId(chunkSection.getBlockId(x, y, z), chunkSection.getBlockData(x, y, z)));
                        buff.writeLShort(indexId);
                    }
                }
            }
            buff.writeSignedVarInt(blockIdMapBuilder.getSize());
            for (Integer blockId : blockIdMapBuilder.getRuntimeIds()) {
                buff.writeSignedVarInt(blockId);
            }

            return buff.readAll();
        }
    }

    /**
     * 将ChunkSection转换成适用于发包的二进制流（RuntimeID版本，并使用快速编码优化）
     *
     * @param chunkSection
     * @return
     */
    public static byte[] getPacketFormatBytesV2Quicker(ChunkSection chunkSection) {
        PacketBuffer buff = new PacketBuffer();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // 循环展开
                buff.writeSignedVarInt(getBlockRuntimeId(chunkSection.getBlockId(x, 0, z), chunkSection.getBlockData(x, 0, z)));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 1, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 2, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 3, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 4, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 5, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 6, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 7, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 8, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 9, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 10, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 11, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 12, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 13, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 14, z));
                buff.writeSignedVarInt(chunkSection.getBlockId(x, 15, z));
            }
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // 循环展开
                buff.writeByte((byte) ((chunkSection.getBlockData(x, 0, z) << 4) | chunkSection.getBlockData(x, 1, z)));
                buff.writeByte((byte) ((chunkSection.getBlockData(x, 2, z) << 4) | chunkSection.getBlockData(x, 3, z)));
                buff.writeByte((byte) ((chunkSection.getBlockData(x, 4, z) << 4) | chunkSection.getBlockData(x, 5, z)));
                buff.writeByte((byte) ((chunkSection.getBlockData(x, 6, z) << 4) | chunkSection.getBlockData(x, 7, z)));
                buff.writeByte((byte) ((chunkSection.getBlockData(x, 8, z) << 4) | chunkSection.getBlockData(x, 9, z)));
                buff.writeByte((byte) ((chunkSection.getBlockData(x, 10, z) << 4) | chunkSection.getBlockData(x, 11, z)));
                buff.writeByte((byte) ((chunkSection.getBlockData(x, 12, z) << 4) | chunkSection.getBlockData(x, 13, z)));
                buff.writeByte((byte) ((chunkSection.getBlockData(x, 14, z) << 4) | chunkSection.getBlockData(x, 15, z)));
            }
        }

        return buff.readAll();
    }

    private static int getBlockRuntimeId(int id, int meta) {
        BlockPrototype blockPrototype = blockPrototypeDictionary.map(id);

        if (meta > blockPrototype.getMaxMetadata()) {
            meta = 0;
        }

        return blockPrototype.getStartRuntimeId() + meta;
    }
}
