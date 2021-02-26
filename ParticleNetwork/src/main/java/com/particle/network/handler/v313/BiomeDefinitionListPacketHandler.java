package com.particle.network.handler.v313;

import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTToByteArray;
import com.particle.model.network.packets.PacketBuffer;
import com.particle.model.network.packets.data.BiomeDefinitionListPacket;
import com.particle.network.handler.AbstractPacketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class BiomeDefinitionListPacketHandler extends AbstractPacketHandler<BiomeDefinitionListPacket> {

    private static final Logger logger = LoggerFactory.getLogger(BiomeDefinitionListPacketHandler.class);

    /**
     * 初始化byteBuf
     */
    private static PacketBuffer packetBuffer;

    static {
        try {
            InputStream inputStream = BiomeDefinitionListPacketHandler.class.getClassLoader().getResourceAsStream("biome_definitions.dat");
            int size = inputStream.available();
            byte[] existedBuf = new byte[size];
            inputStream.read(existedBuf);
            packetBuffer = new PacketBuffer(size + 10);
            packetBuffer.writeBytes(existedBuf);
        } catch (IOException ioe) {
            logger.error("AvailableEntityIdentifiersPacketHandler failed!" + ioe);
        }
    }

    @Override
    protected void doDecode(BiomeDefinitionListPacket dataPacket, int version) {
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
        dataPacket.setBiomeDefinitionData(nbt);
    }

    @Override
    protected void doEncode(BiomeDefinitionListPacket dataPacket, int version) {
        if (packetBuffer == null) {
            dataPacket.writeLShort((short) 0);
            return;
        }
        dataPacket.writeBytes(packetBuffer.getBuffer());
    }
}
