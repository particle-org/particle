package com.particle.network.handler.v274;

import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTToByteArray;
import com.particle.model.network.packets.data.FullChunkDataPacket;
import com.particle.network.handler.AbstractPacketHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FullChunkDataPacketHandler extends AbstractPacketHandler<FullChunkDataPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullChunkDataPacketHandler.class);

    @Override
    protected void doDecode(FullChunkDataPacket dataPacket, int version) {

    }

    @Override
    protected void doEncode(FullChunkDataPacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getChunkX());
        dataPacket.writeSignedVarInt(dataPacket.getChunkZ());

        ByteBuf chunkBuffer = Unpooled.buffer();
        chunkBuffer.writeByte(dataPacket.getChunkSections().size());
        for (byte[] section : dataPacket.getChunkSections()) {
            chunkBuffer.writeBytes(section);
        }

        chunkBuffer.writeBytes(dataPacket.getHeightMap());
        chunkBuffer.writeBytes(new byte[256]);
        chunkBuffer.writeBytes(dataPacket.getBiomes());
        chunkBuffer.writeByte(0);
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
}
