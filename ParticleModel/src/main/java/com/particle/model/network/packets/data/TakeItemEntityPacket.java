package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class TakeItemEntityPacket extends DataPacket {
    private long entityRuntimeId;
    private long playerRuntimeId;

    @Override
    public int pid() {
        return ProtocolInfo.TAKE_ITEM_ENTITY_PACKET;
    }

    public long getEntityRuntimeId() {
        return entityRuntimeId;
    }

    public void setEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

    public long getPlayerRuntimeId() {
        return playerRuntimeId;
    }

    public void setPlayerRuntimeId(long playerRuntimeId) {
        this.playerRuntimeId = playerRuntimeId;
    }
}
