package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ConfirmSkinPacket extends DataPacket {

    private List<UUID> uuids = new LinkedList<>();

    public List<UUID> getUuids() {
        return uuids;
    }

    public void addPlayerListEntry(UUID uuid) {
        this.uuids.add(uuid);
    }


    @Override
    public int pid() {
        return ProtocolInfo.CONFIRM_SKIN_PACKET;
    }
}
