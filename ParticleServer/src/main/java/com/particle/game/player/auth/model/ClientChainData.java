package com.particle.game.player.auth.model;


import com.particle.model.entity.model.skin.PlayerSkinData;

import java.util.UUID;

/**
 * Description: 玩家信息，从LoginPacket中解出
 */
public final class ClientChainData {
    private String username;
    private UUID clientUUID;
    private String xuid;
    private String identityPublicKey;

    private long clientId;
    private String serverAddress;
    private String deviceModel;
    private int deviceOS;
    private String gameVersion;
    private int guiScale;
    private String languageCode;
    private int currentInputMode;
    private int defaultInputMode;

    private int UIProfile;

    /**
     * 认证服认证
     */
    private long uid;
    private short version;
    private String env = "obt";

    private PlayerSkinData playerSkinData;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getClientUUID() {
        return clientUUID;
    }

    public void setClientUUID(UUID clientUUID) {
        this.clientUUID = clientUUID;
    }

    public String getXUID() {
        return xuid;
    }

    public void setXUID(String xuid) {
        this.xuid = xuid;
    }

    public String getIdentityPublicKey() {
        return identityPublicKey;
    }

    public void setIdentityPublicKey(String identityPublicKey) {
        this.identityPublicKey = identityPublicKey;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public int getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(int deviceOS) {
        this.deviceOS = deviceOS;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public int getGuiScale() {
        return guiScale;
    }

    public void setGuiScale(int guiScale) {
        this.guiScale = guiScale;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public int getCurrentInputMode() {
        return currentInputMode;
    }

    public void setCurrentInputMode(int currentInputMode) {
        this.currentInputMode = currentInputMode;
    }

    public int getDefaultInputMode() {
        return defaultInputMode;
    }

    public void setDefaultInputMode(int defaultInputMode) {
        this.defaultInputMode = defaultInputMode;
    }

    public int getUIProfile() {
        return UIProfile;
    }

    public void setUIProfile(int UIProfile) {
        this.UIProfile = UIProfile;
    }

    public PlayerSkinData getPlayerSkinData() {
        return playerSkinData;
    }

    public void setPlayerSkinData(PlayerSkinData playerSkinData) {
        this.playerSkinData = playerSkinData;
    }


    /**
     * 是否合法
     *
     * @return
     */
    public boolean isChainValid() {
        return this.env != null && this.env.equals("obt");
    }

    @Override
    public String toString() {
        return "ClientChainData{" +
                "username='" + username + '\'' +
                ", clientUUID=" + clientUUID +
                ", xuid='" + xuid + '\'' +
                ", uid='" + uid + '\'' +
                ", version='" + version + '\'' +
                ", env='" + env + '\'' +
                ", identityPublicKey='" + identityPublicKey + '\'' +
                ", clientId=" + clientId +
                ", serverAddress='" + serverAddress + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", deviceOS=" + deviceOS +
                ", gameVersion='" + gameVersion + '\'' +
                ", guiScale=" + guiScale +
                ", languageCode='" + languageCode + '\'' +
                ", currentInputMode=" + currentInputMode +
                ", defaultInputMode=" + defaultInputMode +
                ", UIProfile=" + UIProfile +
                ", playerSkinData=" + playerSkinData +
                '}';
    }
}
