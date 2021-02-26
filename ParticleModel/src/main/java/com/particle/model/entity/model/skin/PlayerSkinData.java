package com.particle.model.entity.model.skin;

import com.particle.model.network.packets.data.PersonaPiece;
import com.particle.model.network.packets.data.PersonaPieceTint;

import java.util.ArrayList;
import java.util.List;

public class PlayerSkinData {
    private String skinId;
    private String skinName;
    private byte[] skinData;
    private int skinWidth;
    private int skinHeight;
    private String capeId;
    private byte[] capeData;
    private int capeWidth;
    private int capeHeight;
    private String skinGeometryName;
    private String skinGeometry;
    private String skinResourcePatch;
    private boolean isNewVersion;
    private boolean premiumSkin;
    private boolean personSkin;
    private boolean personCapeOnClassicSkin;
    private PlayerSkinAnimationData[] playerSkinAnimationData;
    private String serializedAnimationData;

    // 1.16
    private List<PersonaPiece> personaPieceList = new ArrayList<>();
    private List<PersonaPieceTint> personaPieceTintList = new ArrayList<>();
    private String skinColor = "#0";
    private String armSize = "wide";
    private boolean trusted = false;

    // 为了方便插件兼容，默认直接设置成true
    private boolean isChecked = true;

    public byte[] getSkinData() {
        return skinData;
    }

    public void setSkinData(byte[] skinData) {
        this.skinData = skinData;
    }

    public String getSkinName() {
        return skinName;
    }

    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }

    public String getSkinId() {
        return skinId;
    }

    public void setSkinId(String skinId) {
        this.skinId = skinId;
    }

    public int getSkinWidth() {
        return skinWidth;
    }

    public void setSkinWidth(int skinWidth) {
        this.skinWidth = skinWidth;
    }

    public int getSkinHeight() {
        return skinHeight;
    }

    public void setSkinHeight(int skinHeight) {
        this.skinHeight = skinHeight;
    }

    public String getCapeId() {
        return capeId;
    }

    public void setCapeId(String capeId) {
        this.capeId = capeId;
    }

    public byte[] getCapeData() {
        return capeData;
    }

    public void setCapeData(byte[] capeData) {
        this.capeData = capeData;
    }

    public int getCapeWidth() {
        return capeWidth;
    }

    public void setCapeWidth(int capeWidth) {
        this.capeWidth = capeWidth;
    }

    public int getCapeHeight() {
        return capeHeight;
    }

    public void setCapeHeight(int capeHeight) {
        this.capeHeight = capeHeight;
    }

    public String getSkinGeometryName() {
        return skinGeometryName;
    }

    public void setSkinGeometryName(String skinGeometryName) {
        this.skinGeometryName = skinGeometryName;
    }

    public String getSkinGeometry() {
        return skinGeometry;
    }

    public void setSkinGeometry(String skinGeometry) {
        this.skinGeometry = skinGeometry;
    }

    public String getSkinResourcePatch() {
        return skinResourcePatch;
    }

    public void setSkinResourcePatch(String skinResourcePatch) {
        this.skinResourcePatch = skinResourcePatch;
    }

    public boolean isNewVersion() {
        return isNewVersion;
    }

    public void setNewVersion(boolean newVersion) {
        isNewVersion = newVersion;
    }

    public boolean isPremiumSkin() {
        return premiumSkin;
    }

    public void setPremiumSkin(boolean premiumSkin) {
        this.premiumSkin = premiumSkin;
    }

    public boolean isPersonSkin() {
        return personSkin;
    }

    public void setPersonSkin(boolean personSkin) {
        this.personSkin = personSkin;
    }

    public boolean isPersonCapeOnClassicSkin() {
        return personCapeOnClassicSkin;
    }

    public void setPersonCapeOnClassicSkin(boolean personCapeOnClassicSkin) {
        this.personCapeOnClassicSkin = personCapeOnClassicSkin;
    }

    public PlayerSkinAnimationData[] getPlayerSkinAnimationData() {
        return playerSkinAnimationData;
    }

    public void setPlayerSkinAnimationData(PlayerSkinAnimationData[] playerSkinAnimationData) {
        this.playerSkinAnimationData = playerSkinAnimationData;
    }

    public String getSerializedAnimationData() {
        return serializedAnimationData;
    }

    public void setSerializedAnimationData(String serializedAnimationData) {
        this.serializedAnimationData = serializedAnimationData;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public List<PersonaPiece> getPersonaPieceList() {
        return personaPieceList;
    }

    public List<PersonaPieceTint> getPersonaPieceTintList() {
        return personaPieceTintList;
    }

    public String getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(String skinColor) {
        this.skinColor = skinColor;
    }

    public String getArmSize() {
        return armSize;
    }

    public void setArmSize(String armSize) {
        this.armSize = armSize;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    @Override
    public String toString() {
        return "PlayerSkinData{" +
                "skinId='" + skinId + '\'' +
                ", skinName='" + skinName + '\'' +
                ", skinWidth=" + skinWidth +
                ", skinHeight=" + skinHeight +
                ", capeId='" + capeId + '\'' +
                ", capeWidth=" + capeWidth +
                ", capeHeight=" + capeHeight +
                ", skinGeometryName='" + skinGeometryName + '\'' +
                ", skinGeometry='" + skinGeometry + '\'' +
                ", skinResourcePatch='" + skinResourcePatch + '\'' +
                ", isNewVersion=" + isNewVersion +
                ", premiumSkin=" + premiumSkin +
                ", personSkin=" + personSkin +
                ", personCapeOnClassicSkin=" + personCapeOnClassicSkin +
                ", serializedAnimationData='" + serializedAnimationData + '\'' +
                '}';
    }
}
