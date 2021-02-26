package com.particle.network.encoder;

import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTToByteArray;
import com.particle.model.network.packets.DataPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NBTTagCompoundEncoder extends ModelHandler<NBTTagCompound> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NBTTagCompoundEncoder.class);

    /**
     * 单例对象
     */
    private static final NBTTagCompoundEncoder INSTANCE = new NBTTagCompoundEncoder();

    /**
     * 获取单例
     */
    public static NBTTagCompoundEncoder getInstance() {
        return NBTTagCompoundEncoder.INSTANCE;
    }

    @Override
    public NBTTagCompound decode(DataPacket dataPacket, int version) {
        byte[] nbtBuffer = dataPacket.readLeft();

        try {
            if (nbtBuffer.length > 0) {
                return (NBTTagCompound) NBTToByteArray.convertToTag(nbtBuffer, true);
            }
        } catch (IOException ioe) {
            LOGGER.error("解析nbt失败", ioe);
        }

        return null;
    }

    @Override
    public void encode(DataPacket dataPacket, NBTTagCompound nbtTagCompound, int version) {
        if (nbtTagCompound == null) {
            dataPacket.writeLShort((short) 0);
        } else {
            try {
                byte[] nbt = NBTToByteArray.convertToByteArray(nbtTagCompound, true);
                if (nbt != null) {
                    dataPacket.writeBytes(nbt);
                }
            } catch (IOException ioe) {
                dataPacket.writeLShort((short) 0);
            }
        }
    }
}
