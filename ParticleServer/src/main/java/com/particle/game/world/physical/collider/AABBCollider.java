package com.particle.game.world.physical.collider;

import com.particle.model.math.Vector3f;

public class AABBCollider extends BaseCollider {

    private Vector3f size;

    public Vector3f getSize() {
        return size;
    }

    public void setSize(Vector3f size) {
        this.size = size;
    }

    @Override
    public float getXPadding() {
        return (size.getX() / 2);
    }

    @Override
    public float getYPadding() {
        return (size.getY() / 2);
    }

    @Override
    public float getZPadding() {
        return (size.getZ() / 2);
    }

}
