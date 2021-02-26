package com.particle.game.entity.movement.component;

import com.particle.core.ecs.component.ECSComponent;

public class EntitySpeedComponent implements ECSComponent {

    private float speed = 0.1f;

    private float maxSpeed = 0.2f;

    private boolean isRunning = false;

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

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
