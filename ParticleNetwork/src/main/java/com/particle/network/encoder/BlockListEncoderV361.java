package com.particle.network.encoder;

import com.particle.model.block.types.BlockPrototype;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.PacketBuffer;
import com.particle.network.handler.common.BlockInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockListEncoderV361 {

    /**
     * 存储runTimeIdTable
     */
    public PacketBuffer runtimeIdTableBuf;

    public BlockListEncoderV361(List<BlockInfo> standBlockInfo) {
        // 读取官方id列表
        Set<String> bedrockNames = new HashSet<>();
        for (BlockInfo blockInfo : standBlockInfo) {
            bedrockNames.add(blockInfo.getName() + "-" + blockInfo.getData());
        }

        // 读取本地id列表
        BlockPrototype[] blockPrototypes = BlockPrototype.values();
        Set<String> localNames = new HashSet<>();
        for (BlockPrototype blockPrototype : blockPrototypes) {
            for (int i = 0; i < blockPrototype.getMaxMetadata(); i++) {
                localNames.add(blockPrototype.getName() + "-" + i);
            }
        }

        // 计算增补方块
        HashSet<String> missing = new HashSet<>(bedrockNames);
        missing.removeAll(localNames);

        // 计算缺失方块
        HashSet<String> exclude = new HashSet<>(localNames);
        exclude.removeAll(bedrockNames);

        // 构造runtime表
        int tableLen = missing.size();
        for (BlockPrototype blockPrototype : blockPrototypes) {
            tableLen += blockPrototype.getMaxMetadata();
        }

        this.runtimeIdTableBuf = new PacketBuffer(tableLen);
        this.runtimeIdTableBuf.writeUnsignedVarInt(tableLen);
        for (BlockPrototype blockPrototype : blockPrototypes) {
            int maxMeta = blockPrototype.getMaxMetadata();
            for (short i = 0; i < maxMeta; i++) {
                if (exclude.contains(blockPrototype.getName() + "-" + i)) {
                    // 对于服务端内有，但是对应版本没有的block，用minecraft:element_1占位，避免id错位
                    this.runtimeIdTableBuf.writeString("minecraft:element_101");
                    this.runtimeIdTableBuf.writeLShort((short) 0);
                    this.runtimeIdTableBuf.writeLShort((short) blockPrototype.getId());
                } else {
                    // 对于服务端和对应版本都有的，直接添加
                    this.runtimeIdTableBuf.writeString(blockPrototype.getName());
                    this.runtimeIdTableBuf.writeLShort(i);
                    this.runtimeIdTableBuf.writeLShort((short) blockPrototype.getId());
                }
            }
        }

        // 对于对应版本有，但是服务端内没有的item，添加到runtime表最后
        short offsetId = 2000;
        for (String name : missing) {
            String[] split = name.split("-");
            this.runtimeIdTableBuf.writeString(split[0]);
            this.runtimeIdTableBuf.writeLShort(Short.parseShort(split[1]));
            this.runtimeIdTableBuf.writeLShort(offsetId++);
        }
    }

    public void encode(DataPacket dataPacket) {
        dataPacket.writeBytes(this.runtimeIdTableBuf.getBuffer());
    }
}
