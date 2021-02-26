package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.TextPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class TextPacketHandler extends AbstractPacketHandler<TextPacket> {

    @Override
    protected void doDecode(TextPacket dataPacket, int version) {
        int messageType = dataPacket.readByte();
        dataPacket.setMessageType(messageType);
        dataPacket.setLocalize(dataPacket.readBoolean());
        switch (messageType) {
            case TextPacket.RawType:
            case TextPacket.TipType:
            case TextPacket.SystemMessageType:
            case TextPacket.TextObjectWhisper:
                dataPacket.setMessage(dataPacket.readString());
                break;
            case TextPacket.ChatType:
            case TextPacket.WhisperType:
            case TextPacket.AnnouncementType:
                dataPacket.setPrimaryName(dataPacket.readString());
                dataPacket.setThirdPartyName(dataPacket.readString());
                dataPacket.setSenderPlatformId(dataPacket.readSignedVarInt());
                dataPacket.setMessage(dataPacket.readString());
                break;
            case TextPacket.TranslateType:
            case TextPacket.PopupType:
            case TextPacket.JukeboxPopupType:
                dataPacket.setMessage(dataPacket.readString());
                int counts = dataPacket.readUnsignedVarInt();
                String[] parameter = new String[counts];
                for (int i = 0; i < counts; i++) {
                    parameter[i] = dataPacket.readString();
                }
                dataPacket.setParameterList(parameter);
                break;
        }
        dataPacket.setReserved(dataPacket.readString());
        dataPacket.setPlatformId(dataPacket.readString());
    }

    @Override
    protected void doEncode(TextPacket dataPacket, int version) {
        dataPacket.writeByte((byte) dataPacket.getMessageType());
        dataPacket.writeBoolean(dataPacket.isLocalize());
        switch (dataPacket.getMessageType()) {
            case TextPacket.RawType:
            case TextPacket.TipType:
            case TextPacket.SystemMessageType:
                dataPacket.writeString(dataPacket.getMessage());
                break;
            case TextPacket.ChatType:
            case TextPacket.WhisperType:
            case TextPacket.AnnouncementType:
                dataPacket.writeString(dataPacket.getPrimaryName());
                dataPacket.writeString(dataPacket.getThirdPartyName());
                dataPacket.writeSignedVarInt(dataPacket.getSenderPlatformId());
                dataPacket.writeString(dataPacket.getMessage());
                break;
            case TextPacket.TranslateType:
            case TextPacket.PopupType:
            case TextPacket.JukeboxPopupType:
                dataPacket.writeString(dataPacket.getMessage());
                dataPacket.writeUnsignedVarInt(dataPacket.getParameterList().length);
                for (String parameter : dataPacket.getParameterList()) {
                    dataPacket.writeString(parameter);
                }
                break;
        }
        dataPacket.writeString(dataPacket.getReserved());
        dataPacket.writeString(dataPacket.getPlatformId());
    }
}
