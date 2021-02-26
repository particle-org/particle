package com.particle.network.handler.v313;

import com.particle.model.entity.type.EntityTypeDictionary;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTToByteArray;
import com.particle.model.network.packets.data.AvailableEntityIdentifiersPacket;
import com.particle.network.handler.AbstractPacketHandler;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AvailableEntityIdentifiersPacketHandler extends AbstractPacketHandler<AvailableEntityIdentifiersPacket> {

    private static final Logger logger = LoggerFactory.getLogger(AvailableEntityIdentifiersPacketHandler.class);

    @Override
    protected void doDecode(AvailableEntityIdentifiersPacket dataPacket, int version) {
        int nbtLen = dataPacket.readLShort();
        byte[] nbtBuffer = new byte[0];
        if (nbtLen > 0) {
            nbtBuffer = new byte[nbtLen];
            dataPacket.readBytes(nbtBuffer);
        }
        NBTTagCompound nbt = null;
        try {
            if (nbtBuffer.length > 0) {
                nbt = (NBTTagCompound) NBTToByteArray.convertToTag(nbtBuffer, false);
            }
        } catch (IOException ioe) {
            logger.error("解析nbt失败", ioe);
            return;
        }
        dataPacket.setActorInfoList(nbt);
    }

    @Override
    protected void doEncode(AvailableEntityIdentifiersPacket dataPacket, int version) {
        ByteBuf encodeData = EntityTypeDictionary.getEncodeData();
        if (encodeData == null) {
            dataPacket.writeLShort((short) 0);
            return;
        }
        dataPacket.writeBytes(encodeData);
    }
}
