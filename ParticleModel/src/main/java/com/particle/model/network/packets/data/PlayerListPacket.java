package com.particle.model.network.packets.data;

import com.particle.model.entity.model.skin.PlayerSkinAnimationData;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PlayerListPacket extends DataPacket {

    private PlayerListAction action;
    private List<PlayerListEntry> entries = new LinkedList<>();

    public PlayerListAction getAction() {
        return action;
    }

    public List<PlayerListEntry> getEntries() {
        return entries;
    }

    public void setAction(PlayerListAction action) {
        this.action = action;
    }

    public void addPlayerListEntry(PlayerListEntry playerListEntry) {
        this.entries.add(playerListEntry);
    }

    public void clearPlayerList() {
        this.entries.clear();
    }

    @Override
    public int pid() {
        return ProtocolInfo.PLAYER_LIST_PACKET;
    }

    public static class PlayerListEntry {
        private UUID entityUUID;
        private long entityId;
        private String entityName;
        private String reserved;
        private String chatId;
        private int buildPlatform = -1;
        private boolean isTeacher;
        private boolean isHost;

        private String skinId;
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
        private boolean premiumSkin;
        private boolean personSkin;
        private boolean personCapeOnClassicSkin;
        private PlayerSkinAnimationData[] playerSkinAnimationData;
        private String serializedAnimationData;

        private List<PersonaPiece> personaPieces = new ArrayList<>();
        private List<PersonaPieceTint> tintColors = new ArrayList<>();
        private String skinColor = "#0";
        private String armSize = "wide";
        private boolean trusted = false;

        private boolean isNewVersion;

        public UUID getEntityUUID() {
            return entityUUID;
        }

        public void setEntityUUID(UUID entityUUID) {
            this.entityUUID = entityUUID;
        }

        public long getEntityId() {
            return entityId;
        }

        public void setEntityId(long entityId) {
            this.entityId = entityId;
        }

        public String getEntityName() {
            return entityName;
        }

        public void setEntityName(String entityName) {
            this.entityName = entityName;
        }

        public String getSkinId() {
            return skinId;
        }

        public void setSkinId(String skinId) {
            this.skinId = skinId;
        }

        public byte[] getSkinData() {
            return skinData;
        }

        public void setSkinData(byte[] skinData) {
            this.skinData = skinData;
        }

        public String getReserved() {
            return reserved;
        }

        public void setReserved(String reserved) {
            this.reserved = reserved;
        }

        public String getChatId() {
            return chatId;
        }

        public void setChatId(String chatId) {
            this.chatId = chatId;
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

        public int getBuildPlatform() {
            return buildPlatform;
        }

        public void setBuildPlatform(int buildPlatform) {
            this.buildPlatform = buildPlatform;
        }

        public boolean isTeacher() {
            return isTeacher;
        }

        public void setTeacher(boolean teacher) {
            isTeacher = teacher;
        }

        public boolean isHost() {
            return isHost;
        }

        public void setHost(boolean host) {
            isHost = host;
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

        public boolean isNewVersion() {
            return isNewVersion;
        }

        public void setNewVersion(boolean newVersion) {
            isNewVersion = newVersion;
        }

        public List<PersonaPiece> getPersonaPieces() {
            return personaPieces;
        }

        public List<PersonaPieceTint> getTintColors() {
            return tintColors;
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
    }

    public enum PlayerListAction {
        ADD((byte) 0),
        REMOVE((byte) 1);

        private byte value;

        PlayerListAction(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }
}
