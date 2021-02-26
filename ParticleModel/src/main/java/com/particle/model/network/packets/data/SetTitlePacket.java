package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class SetTitlePacket extends DataPacket {

    public static final int CLEAR = 0;

    public static final int RESET = 1;

    public static final int TITLE = 2;

    public static final int SUBTITLE = 3;

    public static final int ACTIONBAR = 4;

    public static final int TIMES = 5;

    // 以下三个行为和TITLE这些一样，只是这些是raw text object，应该是发送给客户端后客户端不翻译文本
    public static final int TITLE_TEXT_OBJECT = 6;

    public static final int SUBTITLE_TEXT_OBJECT = 7;

    public static final int ACTIONBAR_TEXT_OBJECT = 8;

    @Override
    public int pid() {
        return ProtocolInfo.SET_TITLE_PACKET;
    }

    private int titleType;

    private String titleText = "";

    private int fadeInTime = 10;

    private int stayTime = 75;

    private int fadeOutTime = 20;

    public int getTitleType() {
        return titleType;
    }

    public void setTitleType(int titleType) {
        this.titleType = titleType;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public int getFadeInTime() {
        return fadeInTime;
    }

    public void setFadeInTime(int fadeInTime) {
        this.fadeInTime = fadeInTime;
    }

    public int getStayTime() {
        return stayTime;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    public int getFadeOutTime() {
        return fadeOutTime;
    }

    public void setFadeOutTime(int fadeOutTime) {
        this.fadeOutTime = fadeOutTime;
    }
}
