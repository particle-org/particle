package com.particle.game.server;

import com.particle.util.configer.anno.ConfigBean;

@ConfigBean(name = "ServerConfig")
public class ServerConfig {
    private boolean enableNeteaseAuth = false;
    private String version = "1.0.0";
    private boolean isInventoryServerAuthoritative = true;
    private boolean isMovementServerAuthoritative = true;
    private boolean allow4DSkin = false;
    private boolean isPlayerSave = true;
    private boolean isPlayerLoad = true;
    private boolean isSaveOldData = true;
    private boolean isEnableSign = true;

    public boolean isEnableNeteaseAuth() {
        return enableNeteaseAuth;
    }

    public void setEnableNeteaseAuth(boolean enableNeteaseAuth) {
        this.enableNeteaseAuth = enableNeteaseAuth;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isAllow4DSkin() {
        return allow4DSkin;
    }

    public void setAllow4DSkin(boolean allow4DSkin) {
        this.allow4DSkin = allow4DSkin;
    }

    public boolean isPlayerSave() {
        return isPlayerSave;
    }

    public void setPlayerSave(boolean playerSave) {
        isPlayerSave = playerSave;
    }

    public boolean isPlayerLoad() {
        return isPlayerLoad;
    }

    public void setPlayerLoad(boolean playerLoad) {
        isPlayerLoad = playerLoad;
    }

    public boolean isSaveOldData() {
        return isSaveOldData;
    }

    public void setSaveOldData(boolean saveOldData) {
        isSaveOldData = saveOldData;
    }

    public boolean isEnableSign() {
        return isEnableSign;
    }

    public void setEnableSign(boolean enableSign) {
        isEnableSign = enableSign;
    }

    public boolean isInventoryServerAuthoritative() {
        return isInventoryServerAuthoritative;
    }

    public void setInventoryServerAuthoritative(boolean inventoryServerAuthoritative) {
        isInventoryServerAuthoritative = inventoryServerAuthoritative;
    }

    public boolean isMovementServerAuthoritative() {
        return isMovementServerAuthoritative;
    }

    public void setMovementServerAuthoritative(boolean movementServerAuthoritative) {
        isMovementServerAuthoritative = movementServerAuthoritative;
    }
}
