package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class RemoveObjectivePacket extends DataPacket {

    private String objectiveName;

    @Override
    public int pid() {
        return ProtocolInfo.REMOVE_OBJECTIVE_PACKET;
    }

    public String getObjectiveName() {
        return objectiveName;
    }

    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }
}
