package com.particle.game.player.chat;

import com.particle.util.configer.anno.ConfigBean;

@ConfigBean(name = "ChatConfig")
public class ChatConfig {

    private boolean enableOnlineCheck = false;

    private String onlineCheckApi = "";

    private String illegalMessage = "发言包含不合法内容";

    private String tooLongMessage = "发言内容过长！";

    public boolean isEnableOnlineCheck() {
        return enableOnlineCheck;
    }

    public void setEnableOnlineCheck(boolean enableOnlineCheck) {
        this.enableOnlineCheck = enableOnlineCheck;
    }

    public String getOnlineCheckApi() {
        return onlineCheckApi;
    }

    public void setOnlineCheckApi(String onlineCheckApi) {
        this.onlineCheckApi = onlineCheckApi;
    }

    public String getIllegalMessage() {
        return illegalMessage;
    }

    public void setIllegalMessage(String illegalMessage) {
        this.illegalMessage = illegalMessage;
    }

    public String getTooLongMessage() {
        return tooLongMessage;
    }

    public void setTooLongMessage(String tooLongMessage) {
        this.tooLongMessage = tooLongMessage;
    }
}

