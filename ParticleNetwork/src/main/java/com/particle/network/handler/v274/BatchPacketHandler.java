package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.BatchPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class BatchPacketHandler extends AbstractPacketHandler<BatchPacket> {

    @Override
    protected void doDecode(BatchPacket dataPacket, int version) {
        dataPacket.setPayload(dataPacket.slice());
    }

    @Override
    protected void doEncode(BatchPacket dataPacket, int version) {
        dataPacket.append(dataPacket.getPayload());
    }

    @Override
    protected void doDecodeHead(BatchPacket dataPacket, int version) {
    }

    @Override
    protected void doEncodeHead(BatchPacket dataPacket, int version) {
        // batchPacket强制写一个byte
        dataPacket.writeByte((byte) dataPacket.pid());
    }
}
