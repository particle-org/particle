package com.particle.network.handler.v389;

import com.particle.model.network.packets.data.TextPacket;
import com.particle.network.handler.v291.TextPacketHandler291;

public class TextPacketHandler389 extends TextPacketHandler291 {

    @Override
    protected void doDecode(TextPacket dataPacket, int version) {
        super.doDecode(dataPacket, version);
    }

    @Override
    protected void doEncode(TextPacket dataPacket, int version) {
        super.doEncode(dataPacket, version);
        if (isChsVersion) {
            switch (dataPacket.getMessageType()) {
                case TextPacket.ChatType:
                case TextPacket.PopupType:
                    dataPacket.writeByte((byte) 0);
            }
        }
    }
}
