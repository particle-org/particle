package com.particle.network.handler.v313;

import com.particle.model.network.packets.data.BiomeDefinitionListCompiledPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class BiomeDefinitionListCompiledPacketHandler extends AbstractPacketHandler<BiomeDefinitionListCompiledPacket> {
    @Override
    protected void doDecode(BiomeDefinitionListCompiledPacket dataPacket, int version) {

    }

    @Override
    protected void doEncode(BiomeDefinitionListCompiledPacket dataPacket, int version) {
        if (BiomeDefinitionListCompiledPacket.existedBuf == null) {
            dataPacket.writeLShort((short) 0);
            return;
        }
        dataPacket.writeBytes(BiomeDefinitionListCompiledPacket.existedBuf);
    }
}
