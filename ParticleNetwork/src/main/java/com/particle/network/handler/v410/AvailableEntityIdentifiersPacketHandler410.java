package com.particle.network.handler.v410;

import com.particle.model.entity.type.EntityTypeDictionary;
import com.particle.model.network.packets.data.AvailableEntityIdentifiersPacket;
import com.particle.network.handler.AbstractPacketHandler;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvailableEntityIdentifiersPacketHandler410 extends AbstractPacketHandler<AvailableEntityIdentifiersPacket> {

    private static final Logger logger = LoggerFactory.getLogger(AvailableEntityIdentifiersPacketHandler410.class);

    @Override
    protected void doDecode(AvailableEntityIdentifiersPacket dataPacket, int version) {
        // 不實現
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
