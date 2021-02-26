package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import io.netty.buffer.ByteBuf;

public class BatchPacket extends DataPacket {

    private ByteBuf payload;

    public ByteBuf getPayload() {
        return payload;
    }

    public void setPayload(ByteBuf payload) {
        this.payload = payload;
    }

    @Override
    public int pid() {
        return ProtocolInfo.COMPRESS_PACKET_HEAD;
    }

}
