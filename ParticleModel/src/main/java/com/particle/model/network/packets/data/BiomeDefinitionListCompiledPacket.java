package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class BiomeDefinitionListCompiledPacket extends DataPacket {

    private static final Logger LOGGER = LoggerFactory.getLogger(BiomeDefinitionListCompiledPacket.class);

    /**
     * 初始化byteBuf
     * <p>
     * 该功能因为工期安排，仅完成发包功能，不做包内容的解析
     */
    public static byte[] existedBuf;

    static {
        try {
            InputStream inputStream = BiomeDefinitionListCompiledPacket.class.getClassLoader().getResourceAsStream("biome_definitions_v116.dat");
            int size = inputStream.available();
            existedBuf = new byte[size];
            inputStream.read(existedBuf);
        } catch (IOException ioe) {
            LOGGER.error("BiomeDefinitionListCompiledPacket failed!", ioe);
        }

    }

    @Override
    public int pid() {
        return ProtocolInfo.BIOME_DEFINITION_LIST_PACKET;
    }
}
