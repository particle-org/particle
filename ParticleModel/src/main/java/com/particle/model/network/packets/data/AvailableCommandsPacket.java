package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class AvailableCommandsPacket extends DataPacket {

    @Override
    public int pid() {
        return ProtocolInfo.AVAILABLE_COMMANDS_PACKET;
    }
}
