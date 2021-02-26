package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class BossEventPacket extends DataPacket {

    private long entityId;
    private EventType eventType;
    private long playerEntityId;
    private float healthPercent;
    private String bossName;
    private short darkenScreen;
    private int color;
    private int overlay;

    @Override
    public int pid() {
        return ProtocolInfo.BOSS_EVENT_PACKET;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public long getPlayerEntityId() {
        return playerEntityId;
    }

    public void setPlayerEntityId(long playerEntityId) {
        this.playerEntityId = playerEntityId;
    }

    public float getHealthPercent() {
        return healthPercent;
    }

    public void setHealthPercent(float healthPercent) {
        this.healthPercent = healthPercent;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public short getDarkenScreen() {
        return darkenScreen;
    }

    public void setDarkenScreen(short darkenScreen) {
        this.darkenScreen = darkenScreen;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getOverlay() {
        return overlay;
    }

    public void setOverlay(int overlay) {
        this.overlay = overlay;
    }

    public enum EventType {
        ADD(0),
        PLAYER_ADDED(1),
        REMOVE(2),
        PLAYER_REMOVED(3),
        UPDATE_PERCENT(4),
        UPDATE_NAME(5),
        UPDATE_PROPERTIES(6),
        UPDATE_STYLE(7);

        private int value;

        EventType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
