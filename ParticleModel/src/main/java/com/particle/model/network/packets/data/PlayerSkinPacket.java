package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.util.UUID;

public class PlayerSkinPacket extends DataPacket {

    private UUID playerUUID;
    private String skinId;
    private String skinName;
    private String oldSkinName;
    private String skinData;
    private String capeData;
    private String geometryName;
    private String geometryData;
    private boolean isPremium;

    @Override
    public int pid() {
        return ProtocolInfo.PLAYER_SKIN_PACKET;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getSkinId() {
        return skinId;
    }

    public void setSkinId(String skinId) {
        this.skinId = skinId;
    }

    public String getSkinName() {
        return skinName;
    }

    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }

    public String getOldSkinName() {
        return oldSkinName;
    }

    public void setOldSkinName(String oldSkinName) {
        this.oldSkinName = oldSkinName;
    }

    public String getSkinData() {
        return skinData;
    }

    public void setSkinData(String skinData) {
        this.skinData = skinData;
    }

    public String getCapeData() {
        return capeData;
    }

    public void setCapeData(String capeData) {
        this.capeData = capeData;
    }

    public String getGeometryName() {
        return geometryName;
    }

    public void setGeometryName(String geometryName) {
        this.geometryName = geometryName;
    }

    public String getGeometryData() {
        return geometryData;
    }

    public void setGeometryData(String geometryData) {
        this.geometryData = geometryData;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
