package com.particle.game.item;

import com.particle.util.configer.anno.ConfigBean;

@ConfigBean(name = "RemoteWeapon")
public class RemoteWeaponConfig {

    private String id = "weaponId";
    private String entityTemplate = "minecraft:wither_skull_dangerous";
    private float damage = 5f;
    private float additionDamage = 10f;
    private float additionDamageSpeed = 10f;
    private float gravity = 0f;
    private float boundBoxX = 0.2f;
    private float boundBoxY = 0.2f;
    private float boundBoxZ = 0.2f;
    private boolean autoDirection = false;
    private boolean explosion = true;
    private float explosionPower = 10f;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityTemplate() {
        return entityTemplate;
    }

    public void setEntityTemplate(String entityTemplate) {
        this.entityTemplate = entityTemplate;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getAdditionDamage() {
        return additionDamage;
    }

    public void setAdditionDamage(float additionDamage) {
        this.additionDamage = additionDamage;
    }

    public float getAdditionDamageSpeed() {
        return additionDamageSpeed;
    }

    public void setAdditionDamageSpeed(float additionDamageSpeed) {
        this.additionDamageSpeed = additionDamageSpeed;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public float getBoundBoxX() {
        return boundBoxX;
    }

    public void setBoundBoxX(float boundBoxX) {
        this.boundBoxX = boundBoxX;
    }

    public float getBoundBoxY() {
        return boundBoxY;
    }

    public void setBoundBoxY(float boundBoxY) {
        this.boundBoxY = boundBoxY;
    }

    public float getBoundBoxZ() {
        return boundBoxZ;
    }

    public void setBoundBoxZ(float boundBoxZ) {
        this.boundBoxZ = boundBoxZ;
    }

    public boolean isAutoDirection() {
        return autoDirection;
    }

    public void setAutoDirection(boolean autoDirection) {
        this.autoDirection = autoDirection;
    }

    public boolean isExplosion() {
        return explosion;
    }

    public void setExplosion(boolean explosion) {
        this.explosion = explosion;
    }

    public float getExplosionPower() {
        return explosionPower;
    }

    public void setExplosionPower(float explosionPower) {
        this.explosionPower = explosionPower;
    }
}
