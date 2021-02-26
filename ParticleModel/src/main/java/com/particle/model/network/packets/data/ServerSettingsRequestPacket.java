package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class ServerSettingsRequestPacket extends DataPacket {

    @Override
    public int pid() {
        return ProtocolInfo.SERVER_SETTINGS_REQUEST_PACKET;
    }
}
