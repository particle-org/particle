package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class SetCommandEnabledPacket extends DataPacket {

    private boolean enabled;

    @Override
    public int pid() {
        return ProtocolInfo.SET_COMMANDS_ENABLED_PACKET;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
