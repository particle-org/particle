package com.particle.game.world.physical.collider;

import com.particle.model.math.Vector3f;

public abstract class BaseCollider {

    // 中心距离生物位置的向量
    private Vector3f center;

    private Vector3f lastPosition;
    private Vector3f lastMovement;

    private boolean colliderWithBlock;

    private Vector3f colliderMotion = new Vector3f(0, 0, 0);

    public abstract float getXPadding();

    public abstract float getYPadding();

    public abstract float getZPadding();

    public Vector3f getCenter() {
        return center;
    }

    public void setCenter(Vector3f center) {
        this.center = center;
    }

    public Vector3f getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Vector3f lastPosition) {
        this.lastPosition = lastPosition;
    }

    public Vector3f getLastMovement() {
        return lastMovement;
    }

    public void setLastMovement(Vector3f lastMovement) {
        this.lastMovement = lastMovement;
    }

    public boolean isColliderWithBlock() {
        return colliderWithBlock;
    }

    public void setColliderWithBlock(boolean colliderWithBlock) {
        this.colliderWithBlock = colliderWithBlock;
    }

    public Vector3f getColliderMotion() {
        return colliderMotion;
    }

    public void setColliderMotion(Vector3f colliderMotion) {
        this.colliderMotion = colliderMotion;
    }
}
