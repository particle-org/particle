package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.TransferPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class TransferPacketHandler extends AbstractPacketHandler<TransferPacket> {

    @Override
    protected void doDecode(TransferPacket dataPacket, int version) {
        dataPacket.setServerAddress(dataPacket.readString());
        dataPacket.setPort((short) dataPacket.readLShort());
    }

    @Override
    protected void doEncode(TransferPacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getServerAddress());
        dataPacket.writeLShort(dataPacket.getPort());
    }
}
