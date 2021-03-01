package com.particle.game.entity.service.config;

import java.util.List;
import java.util.Map;

public class MobEntityConfig {
    private String entity;
    private float health = 20;
    private float armor = 0;
    private float damage = 0;
    private float bindBoxX;
    private float bindBoxY;
    private float bindBoxZ;
    private float bindBoxLengthX;
    private float bindBoxLengthY;
    private float bindBoxLengthZ;
    private float eyeHeight;
    private float speed;
    private float maxSpeed;
    private int experience;
    private float gravity;
    private Map<String, EquipmentConfig> equipment;
    private Map<String, List<DropConfig>> drops;

    public String getEntity() {
        return entity;
    }


    public void setEntity(String entity) {
        this.entity = entity;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getArmor() {
        return armor;
    }

    public void setArmor(float armor) {
        this.armor = armor;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getBindBoxX() {
        return bindBoxX;
    }

    public void setBindBoxX(float bindBoxX) {
        this.bindBoxX = bindBoxX;
    }

    public float getBindBoxY() {
        return bindBoxY;
    }

    public void setBindBoxY(float bindBoxY) {
        this.bindBoxY = bindBoxY;
    }

    public float getBindBoxZ() {
        return bindBoxZ;
    }

    public void setBindBoxZ(float bindBoxZ) {
        this.bindBoxZ = bindBoxZ;
    }

    public float getBindBoxLengthX() {
        return bindBoxLengthX;
    }

    public void setBindBoxLengthX(float bindBoxLengthX) {
        this.bindBoxLengthX = bindBoxLengthX;
    }

    public float getBindBoxLengthY() {
        return bindBoxLengthY;
    }

    public void setBindBoxLengthY(float bindBoxLengthY) {
        this.bindBoxLengthY = bindBoxLengthY;
    }

    public float getBindBoxLengthZ() {
        return bindBoxLengthZ;
    }

    public void setBindBoxLengthZ(float bindBoxLengthZ) {
        this.bindBoxLengthZ = bindBoxLengthZ;
    }

    public float getEyeHeight() {
        return eyeHeight;
    }

    public void setEyeHeight(float eyeHeight) {
        this.eyeHeight = eyeHeight;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public Map<String, EquipmentConfig> getEquipment() {
        return equipment;
    }

    public void setEquipment(Map<String, EquipmentConfig> equipment) {
        this.equipment = equipment;
    }

    public Map<String, List<DropConfig>> getDrops() {
        return drops;
    }

    public void setDrops(Map<String, List<DropConfig>> drops) {
        this.drops = drops;
    }
}
