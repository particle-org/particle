package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class PacketViolationWarningPacket extends DataPacket {

    private PacketViolationType packetViolationType;
    private PacketViolationSeverity packetViolationSeverity;
    private int packetId;
    private String context;

    @Override
    public int pid() {
        return ProtocolInfo.PACKET_VIOLATION_WARNING_PACKET;
    }

    public PacketViolationType getPacketViolationType() {
        return packetViolationType;
    }

    public void setPacketViolationType(PacketViolationType packetViolationType) {
        this.packetViolationType = packetViolationType;
    }

    public PacketViolationSeverity getPacketViolationSeverity() {
        return packetViolationSeverity;
    }

    public void setPacketViolationSeverity(PacketViolationSeverity packetViolationSeverity) {
        this.packetViolationSeverity = packetViolationSeverity;
    }

    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public enum PacketViolationType {
        UNKNOWN,
        MALFORMED_PACKET
    }

    public enum PacketViolationSeverity {
        UNKNOWN,
        WARNING,
        FINAL_WARNING,
        TERMINATING_CONNECTION
    }
}
