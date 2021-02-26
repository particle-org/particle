package com.particle.network.encoder;

import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.nbt.*;
import com.particle.model.network.packets.DataPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class ItemStackEncoder410 extends ModelHandler<ItemStack> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemStackEncoder410.class);

    /**
     * 单例对象
     */
    private static final ItemStackEncoder410 INSTANCE = new ItemStackEncoder410();

    /**
     * 获取单例
     */
    public static ItemStackEncoder410 getInstance() {
        return ItemStackEncoder410.INSTANCE;
    }

    @Override
    public ItemStack decode(DataPacket dataPacket, int version) {
        // Item Id
        int itemId = dataPacket.readSignedVarInt();
        if (itemId <= 0) {
            return ItemStack.getItem(ItemPrototype.AIR, 0);
        }

        // Aux Value
        int extraValue = dataPacket.readSignedVarInt();
        int meta = extraValue >> 8;
        meta = meta == Short.MAX_VALUE ? -1 : meta;
        int count = extraValue & 0xff;

        // User Data Serialization Marker
        short marker = dataPacket.readLShort();
        byte[] nbtBuffer = new byte[0];
        NBTTagCompound nbt = null;

        // mark == -1
        if (marker == -1) {
            // User Data Serialization Version
            int serializationVersion = dataPacket.readByte();
            try {
                nbt = CompressedStreamTools.read(
                        new LittleEndianDataInputStream2(asInputStream(dataPacket), true),
                        NBTSizeTracker.INFINITE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            nbtBuffer = new byte[marker];
            dataPacket.readBytes(nbtBuffer);
        }

        // can place on blocks
        int canPlaceOnBlocks = dataPacket.readSignedVarInt();
        if (canPlaceOnBlocks > 0) {
            for (int i = 0; i < canPlaceOnBlocks; i++) {
                dataPacket.readString();
            }
        }

        // can destroy blocks
        int canDestroyBlocks = dataPacket.readSignedVarInt();
        if (canDestroyBlocks > 0) {
            for (int i = 0; i < canDestroyBlocks; i++) {
                dataPacket.readString();
            }
        }

        ItemStack itemStack = ItemStack.getItem(itemId);
        itemStack.setMeta(meta);
        itemStack.setCount(count);
        try {
            if (nbtBuffer.length > 0) {
                nbt = (NBTTagCompound) NBTToByteArray.convertToTag(nbtBuffer, false);
            }
        } catch (IOException ioe) {
            LOGGER.error("解析nbt失败，itemId {}, extraValue {}, marker {}", itemId, extraValue, marker, ioe);
            return ItemStack.getItem(ItemPrototype.AIR, 0);
        }
        if (nbt != null) {
            itemStack.updateNBT(nbt);
        }

        return itemStack;
    }

    @Override
    public void encode(DataPacket dataPacket, ItemStack itemStack, int version) {
        if (itemStack == null || itemStack.getItemType().getId() == ItemPrototype.AIR.getId()) {
            dataPacket.writeSignedVarInt(0);
            return;
        }
        dataPacket.writeSignedVarInt(itemStack.getItemType().getId());
        // 写meta、count
        int extraValue = (((itemStack.hasMeta() ? itemStack.getMeta() : -1) & 0x7fff) << 8) | itemStack.getCount();
        dataPacket.writeSignedVarInt(extraValue);
        // 写nbt信息
        NBTTagCompound nbtTagCompound = itemStack.getNbt();
        if (nbtTagCompound == null) {
            dataPacket.writeLShort((short) 0);
        } else {
            try {
                byte[] nbt = NBTToByteArray.convertToByteArray(nbtTagCompound, false);
                if (nbt != null) {
                    dataPacket.writeLShort((short) 0xffff);
                    dataPacket.writeByte((byte) 1);
                    dataPacket.writeBytes(NBTToByteArray.convertToByteArray(nbtTagCompound, true));
                }
            } catch (IOException ioe) {
                dataPacket.writeLShort((short) 0);
            }
        }
        // 写 can place on blocks
        dataPacket.writeSignedVarInt(0);
        // 写 can destroy blocks
        dataPacket.writeSignedVarInt(0);
    }

    private InputStream asInputStream(DataPacket dataPacket) {
        return new InputStream() {
            @Override
            public int read() {
                return dataPacket.readByte();
            }
        };
    }
}
