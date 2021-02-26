package com.particle.network.handler.v410;

import com.particle.model.network.packets.data.PacketViolationWarningPacket;
import com.particle.network.handler.AbstractPacketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketViolationWarningPacketHandler410 extends AbstractPacketHandler<PacketViolationWarningPacket> {
    private static Logger logger = LoggerFactory.getLogger(PacketViolationWarningPacketHandler410.class);

    @Override
    protected void doDecode(PacketViolationWarningPacket dataPacket, int version) {
        dataPacket.setPacketViolationType(PacketViolationWarningPacket.PacketViolationType.values()[dataPacket.readSignedVarInt() + 1]);
        dataPacket.setPacketViolationSeverity(PacketViolationWarningPacket.PacketViolationSeverity.values()[dataPacket.readSignedVarInt()]);
        dataPacket.setPacketId(dataPacket.readSignedVarInt());
        dataPacket.setContext(dataPacket.readString());

        logger.warn("packet id : " + dataPacket.getPacketId());
        logger.warn("packet client context : " + dataPacket.getContext());
        logger.warn("packet violation type : " + dataPacket.getPacketViolationType());
        logger.warn("packet violation severity : " + dataPacket.getPacketViolationSeverity());
    }

    @Override
    protected void doEncode(PacketViolationWarningPacket dataPacket, int version) {
        dataPacket.writeSignedVarInt(dataPacket.getPacketViolationType().ordinal() - 1);
        dataPacket.writeSignedVarInt(dataPacket.getPacketViolationSeverity().ordinal());
        dataPacket.writeSignedVarInt(dataPacket.getPacketId());
        dataPacket.writeString(dataPacket.getContext());
    }
}
