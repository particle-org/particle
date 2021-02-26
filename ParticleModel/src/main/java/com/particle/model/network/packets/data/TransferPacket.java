package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class TransferPacket extends DataPacket {

    private String serverAddress;

    private short port;

    @Override
    public int pid() {
        return ProtocolInfo.TRANSFER_PACKET;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }
}
