package com.particle.network.handler.v361;

import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTToByteArray;
import com.particle.model.network.packets.data.FullChunkDataPacket;
import com.particle.network.handler.AbstractPacketHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FullChunkDataPacketHandler361 extends AbstractPacketHandler<FullChunkDataPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullChunkDataPacketHandler361.class);

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
        for (byte[] section : dataPacket.getChunkSections()) {
            chunkBuffer.writeBytes(section);
        }

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
