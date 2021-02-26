package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class EmoteListPacket extends DataPacket {
    private long runtimeId;
    private List<UUID> pieceIdList = new LinkedList<>();

    @Override
    public int pid() {
        return ProtocolInfo.EMOTE_LIST_PACKET;
    }

    public long getRuntimeId() {
        return runtimeId;
    }

    public void setRuntimeId(long runtimeId) {
        this.runtimeId = runtimeId;
    }

    public List<UUID> getPieceIdList() {
        return pieceIdList;
    }
}
