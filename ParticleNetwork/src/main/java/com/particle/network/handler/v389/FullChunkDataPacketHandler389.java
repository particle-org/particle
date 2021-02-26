package com.particle.network.handler.v389;

import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Chunk;
import com.particle.model.level.chunk.ChunkSection;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTToByteArray;
import com.particle.model.network.packets.PacketBuffer;
import com.particle.model.network.packets.data.FullChunkDataPacket;
import com.particle.network.handler.AbstractPacketHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullChunkDataPacketHandler389 extends AbstractPacketHandler<FullChunkDataPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullChunkDataPacketHandler389.class);

    @Override
    protected void doDecode(FullChunkDataPacket dataPacket, int version) {

    }

    @Override
    protected void doEncode(FullChunkDataPacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getChunkX());
        dataPacket.writeSignedVarInt(dataPacket.getChunkZ());

        dataPacket.writeUnsignedVarInt(dataPacket.getSubChunkCount());
        // Only support false
        dataPacket.writeBoolean(false);

        ByteBuf chunkBuffer = Unpooled.buffer();
        List<byte[]> chunkSections = getChunkSectionPacketData(dataPacket.getChunk(), version);
        for (byte[] section : chunkSections) {
            chunkBuffer.writeBytes(section);
        }

        chunkBuffer.writeBytes(dataPacket.getBiomes());
        // extra data
        chunkBuffer.writeByte(0);
        for (NBTTagCompound tileEntity : dataPacket.getTileEntities()) {
            try {
                chunkBuffer.writeBytes(NBTToByteArray.convertToByteArray(tileEntity, true));
            } catch (IOException e) {
                LOGGER.error("Fail to convent entity data", e);
            }
        }

        dataPacket.writeUnsignedVarInt(chunkBuffer.writerIndex() - chunkBuffer.readerIndex());
        dataPacket.writeBytes(chunkBuffer);
    }


    public List<byte[]> getChunkSectionPacketData(Chunk chunk, int version) {
        List<byte[]> sectionCache = new ArrayList<>(16);

        if (chunk.getChunkSections() != null) {
            for (ChunkSection chunkSection : chunk.getChunkSections()) {
                byte[] encodeCache = null;
                if (chunkSection != null) {
                    encodeCache = chunkSection.getEncodeCache();
                }

                if (encodeCache == null || chunkSection.getLatestEncodeVersion() != version) {
                    ByteBuf byteBuf = Unpooled.buffer();
                    byteBuf.writeByte(1);
                    byteBuf.writeBytes(getPacketFormatBytesV2(chunkSection, version));

                    encodeCache = new byte[byteBuf.writerIndex() - byteBuf.readerIndex()];
                    byteBuf.readBytes(encodeCache);

                    if (chunkSection != null) {
                        chunkSection.setEncodeCache(encodeCache);
                        chunkSection.setLatestEncodeVersion(version);
                    }
                }

                sectionCache.add(encodeCache);
            }
        }

        return sectionCache;
    }

    private byte[] getPacketFormatBytesV2(ChunkSection chunkSection, int version) {
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
                        short indexId = (short) blockIdMapBuilder.getIndexId(getBlockRuntimeId(chunkSection.getBlockId(x, y, z), chunkSection.getBlockData(x, y, z), version));
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

    private int getBlockRuntimeId(int id, int meta, int version) {
        int ver = version < AbstractPacketHandler.VERSION_1_16 ? AbstractPacketHandler.VERSION_1_13 : AbstractPacketHandler.VERSION_1_16;

        BlockPrototype blockPrototype = Block.getBlockType(id);

        if (meta > blockPrototype.getMaxMetadata()) {
            meta = 0;
        }

        int missCount = 0;
        List<Short> missList = blockPrototype.getMissMetaListMap().get(ver);
        if (missList != null) {
            for (short missMeta : missList) {
                if (meta > missMeta) {
                    missCount++;
                }
            }
        }

        return blockPrototype.getStartRuntimeIdMap().get(ver) + meta - missCount;
    }

    private class BlockIdMapBuilder {

        private Map<Integer, Integer> runtimeIdMap = new HashMap<>();
        private List<Integer> runtimeIdList = new ArrayList<>();
        private int nextIndexId = 0;

        public BlockIdMapBuilder() {
            getIndexId(0);
        }

        public int getIndexId(int runtimeId) {
            int indexId = this.runtimeIdMap.getOrDefault(runtimeId, -1);
            if (indexId == -1) {
                indexId = nextIndexId++;
                this.runtimeIdMap.put(runtimeId, indexId);
                this.runtimeIdList.add(runtimeId);
            }
            return indexId;
        }

        public int getSize() {
            return runtimeIdMap.size();
        }

        public List<Integer> getRuntimeIds() {
            return runtimeIdList;
        }

    }
}
