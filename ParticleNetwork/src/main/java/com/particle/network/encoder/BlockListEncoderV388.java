package com.particle.network.encoder;

import com.particle.model.block.types.BlockPrototype;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTTagList;
import com.particle.model.nbt.NBTToByteArray;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.PacketBuffer;
import com.particle.network.handler.AbstractPacketHandler;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockListEncoderV388 {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockListEncoderV388.class);

    /**
     * 存储runTimeIdTable
     */
    public PacketBuffer runtimeIdTableBuf;

    public BlockListEncoderV388(NBTTagList standBlockInfo) {
        // 读取官方id列表
        Map<String, NBTTagCompound> bedrockNames = new HashMap();
        for (int i = 0; i < standBlockInfo.tagCount(); i++) {
            NBTTagCompound blockTag = standBlockInfo.getCompoundTagAt(i);

            bedrockNames.put(blockTag.getCompoundTag("block").getString("name") + "-" + blockTag.getShort("meta"), blockTag);
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
        HashSet<String> missing = new HashSet<>(bedrockNames.keySet());
        missing.removeAll(localNames);

        // 计算缺失方块
        HashSet<String> exclude = new HashSet<>(localNames);
        exclude.removeAll(bedrockNames.keySet());

        // 构造runtime表
        int tableLen = missing.size();
        for (BlockPrototype blockPrototype : blockPrototypes) {
            tableLen += blockPrototype.getMaxMetadata();
        }

        NBTTagList nbtTagList = new NBTTagList();
        AtomicInteger atomicInteger = new AtomicInteger();
        int runtimeId = 0;
        for (BlockPrototype blockPrototype : blockPrototypes) {
            blockPrototype.getStartRuntimeIdMap().put(AbstractPacketHandler.VERSION_1_13, runtimeId);
            int maxMeta = blockPrototype.getMaxMetadata();
            for (short i = 0; i < maxMeta; i++) {
                // 大於等於1000的表示自定義方塊
                if (blockPrototype.getId() >= 1000) {
                    NBTTagCompound statesTag = new NBTTagCompound();
                    statesTag.setInteger("direction", i);

                    NBTTagCompound nbtBlock = new NBTTagCompound();
                    nbtBlock.setShort("id", (short) blockPrototype.getId());
                    NBTTagCompound nbtTagBlockInfo = new NBTTagCompound();
                    nbtTagBlockInfo.setString("name", blockPrototype.getName());
                    nbtTagBlockInfo.setInteger("version", 17694723);
                    nbtTagBlockInfo.setTag("states", statesTag);
                    nbtBlock.setTag("block", nbtTagBlockInfo);
                    nbtTagList.appendTag(nbtBlock);
                    runtimeId = atomicInteger.getAndIncrement() + 1;
                } else if (exclude.contains(blockPrototype.getName() + "-" + i)) {
                    // 紀錄間斷的meta
                    List<Short> missList = blockPrototype.getMissMetaListMap().get(AbstractPacketHandler.VERSION_1_13);
                    if (missList == null) {
                        missList = new LinkedList<>();
                    }

                    missList.add(i);
                    blockPrototype.getMissMetaListMap().put(AbstractPacketHandler.VERSION_1_13, missList);
                } else {
                    NBTTagCompound blockTag = bedrockNames.get(blockPrototype.getName() + "-" + i);

                    NBTTagCompound nbtBlock = new NBTTagCompound();
                    nbtBlock.setShort("id", (short) blockPrototype.getId());
                    NBTTagCompound nbtTagBlockInfo = new NBTTagCompound();
                    nbtTagBlockInfo.setString("name", blockTag.getCompoundTag("block").getString("name"));
                    nbtTagBlockInfo.setInteger("version", blockTag.getCompoundTag("block").getInteger("version"));
                    nbtTagBlockInfo.setTag("states", blockTag.getCompoundTag("block").getCompoundTag("states"));
                    nbtBlock.setTag("block", nbtTagBlockInfo);

                    nbtTagList.appendTag(nbtBlock);
                    runtimeId = atomicInteger.getAndIncrement() + 1;
                }
            }
        }

        // 对于对应版本有，但是服务端内没有的item，添加到runtime表最后
        short offsetId = 2000;
        for (String name : missing) {
            NBTTagCompound blockTag = bedrockNames.get(name);

            NBTTagCompound nbtBlock = new NBTTagCompound();
            nbtBlock.setShort("id", offsetId++);
            NBTTagCompound nbtTagBlockInfo = new NBTTagCompound();
            nbtTagBlockInfo.setString("name", blockTag.getCompoundTag("block").getString("name"));
            nbtTagBlockInfo.setInteger("version", blockTag.getCompoundTag("block").getInteger("version"));
            nbtTagBlockInfo.setTag("states", blockTag.getCompoundTag("block").getCompoundTag("states"));
            nbtBlock.setTag("block", nbtTagBlockInfo);

            nbtTagList.appendTag(nbtBlock);
        }

        byte[] bytes = null;
        try {
            bytes = NBTToByteArray.convertToByteArray(nbtTagList, true);
        } catch (IOException e) {
            LOGGER.error("Fail to build block list of v1.13", e);
            return;
        }

        this.runtimeIdTableBuf = new PacketBuffer(Unpooled.wrappedBuffer(bytes));
    }

    public void encode(DataPacket dataPacket) {
        dataPacket.writeBytes(this.runtimeIdTableBuf.getBuffer());
    }
}
