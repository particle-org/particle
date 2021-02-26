package com.particle.game.entity.movement.component;

import com.particle.core.ecs.component.ECSComponent;

public class EntityMotionComponent implements ECSComponent {

    //当前的速度
    private float motionX;
    private float motionY;
    private float motionZ;

    public float getMotionX() {
        return motionX;
    }

    public void setMotionX(float motionX) {
        this.motionX = motionX;
    }

    public float getMotionY() {
        return motionY;
    }

    public void setMotionY(float motionY) {
        this.motionY = motionY;
    }

    public float getMotionZ() {
        return motionZ;
    }

    public void setMotionZ(float motionZ) {
        this.motionZ = motionZ;
    }

}
