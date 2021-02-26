package com.particle.model.network.packets.data;

import com.particle.model.level.settings.LevelSettings;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class StartGamePacket extends DataPacket {

    private long entityUniqueId;
    private long entityRuntimeId;
    private int playerGamemode;
    private boolean isInventoryServerAuthoritative = false;
    private int rewindHistorySize = 20;

    private float x;
    private float y;
    private float z;
    private float yaw;
    private float pitch;

    private LevelSettings settings;

    private String levelId = "";
    private String worldName;
    private String premiumWorldTemplateId = "";
    private boolean isTrial = false;
    private long currentLevelTime;
    private int enchantmentSeed;

    //version 1.6 block table encode in PacketHandler

    // version 1.7
    private String multiPlayerCorrelationId = "";

    private boolean isMovementServerAuthoritative;

    @Override
    public int pid() {
        return ProtocolInfo.START_GAME_PACKET;
    }

    public long getEntityUniqueId() {
        return entityUniqueId;
    }

    public void setEntityUniqueId(long entityUniqueId) {
        this.entityUniqueId = entityUniqueId;
    }

    public long getEntityRuntimeId() {
        return entityRuntimeId;
    }

    public void setEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

    public int getPlayerGamemode() {
        return playerGamemode;
    }

    public void setPlayerGamemode(int playerGamemode) {
        this.playerGamemode = playerGamemode;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public LevelSettings getSettings() {
        return settings;
    }

    public void setSettings(LevelSettings settings) {
        this.settings = settings;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getPremiumWorldTemplateId() {
        return premiumWorldTemplateId;
    }

    public void setPremiumWorldTemplateId(String premiumWorldTemplateId) {
        this.premiumWorldTemplateId = premiumWorldTemplateId;
    }

    public boolean isTrial() {
        return isTrial;
    }

    public void setTrial(boolean trial) {
        isTrial = trial;
    }

    public long getCurrentLevelTime() {
        return currentLevelTime;
    }

    public void setCurrentLevelTime(long currentLevelTime) {
        this.currentLevelTime = currentLevelTime;
    }

    public int getEnchantmentSeed() {
        return enchantmentSeed;
    }

    public void setEnchantmentSeed(int enchantmentSeed) {
        this.enchantmentSeed = enchantmentSeed;
    }

    public String getMultiPlayerCorrelationId() {
        return multiPlayerCorrelationId;
    }

    public void setMultiPlayerCorrelationId(String multiPlayerCorrelationId) {
        this.multiPlayerCorrelationId = multiPlayerCorrelationId;
    }

    public boolean isMovementServerAuthoritative() {
        return isMovementServerAuthoritative;
    }

    public void setMovementServerAuthoritative(boolean movementServerAuthoritative) {
        isMovementServerAuthoritative = movementServerAuthoritative;
    }

    public boolean isInventoryServerAuthoritative() {
        return isInventoryServerAuthoritative;
    }

    public void setInventoryServerAuthoritative(boolean inventoryServerAuthoritative) {
        isInventoryServerAuthoritative = inventoryServerAuthoritative;
    }

    public int getRewindHistorySize() {
        return rewindHistorySize;
    }

    public void setRewindHistorySize(int rewindHistorySize) {
        this.rewindHistorySize = rewindHistorySize;
    }
}
