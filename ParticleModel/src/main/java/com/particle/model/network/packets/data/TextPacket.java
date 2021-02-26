package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.util.Arrays;

public class TextPacket extends DataPacket {

    // text 类型
    public static final int RawType = 0;

    public static final int ChatType = 1;

    public static final int TranslateType = 2;

    public static final int PopupType = 3;

    public static final int JukeboxPopupType = 4;

    public static final int TipType = 5;

    public static final int SystemMessageType = 6;

    public static final int WhisperType = 7;

    public static final int AnnouncementType = 8;

    public static final byte TextObjectWhisper = 9; // This type is used to transmit raw text for the new /tellraw command.


    /**
     * 最大的类型
     */
    public static final int MAX_TYPE = 9;

    @Override
    public int pid() {
        return ProtocolInfo.TEXT_PACKET;
    }

    private int messageType;

    private boolean localize = false;

    private String message = "";

    private String primaryName = "";

    private String thirdPartyName = "";

    private int senderPlatformId;

    private String[] parameterList = new String[0];

    private String reserved = "";

    private String platformId = "";

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public boolean isLocalize() {
        return localize;
    }

    public void setLocalize(boolean localize) {
        this.localize = localize;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String[] getParameterList() {
        return parameterList;
    }

    public void setParameterList(String[] parameterList) {
        this.parameterList = parameterList;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public int getSenderPlatformId() {
        return senderPlatformId;
    }

    public void setSenderPlatformId(int senderPlatformId) {
        this.senderPlatformId = senderPlatformId;
    }

    @Override
    public String toString() {
        return "TextPacket{" +
                "messageType=" + messageType +
                ", localize=" + localize +
                ", message='" + message + '\'' +
                ", primaryName='" + primaryName + '\'' +
                ", thirdPartyName='" + thirdPartyName + '\'' +
                ", senderPlatformId=" + senderPlatformId +
                ", parameterList=" + Arrays.toString(parameterList) +
                ", reserved='" + reserved + '\'' +
                ", platformId='" + platformId + '\'' +
                '}';
    }
}
