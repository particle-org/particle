package com.particle.model.player.saver;

public class PlayerBaseData {
    private float health = 20.0f;
    private float maxHealth = 20.0f;

    private int exp;
    private int expLevel;

    private float posX;
    private float posY;
    private float posZ;

    private float yaw;
    private float pitch;

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int experience) {
        this.exp = experience;
    }

    public int getExpLevel() {
        return expLevel;
    }

    public void setExpLevel(int level) {
        this.expLevel = level;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getPosZ() {
        return posZ;
    }

    public void setPosZ(float posZ) {
        this.posZ = posZ;
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
}
