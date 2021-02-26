package com.particle.game.common.modules;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.game.common.components.TransformComponent;
import com.particle.model.entity.component.position.IMoveEntityPacketBuilder;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

public class TransformModule extends ECSModule {

    private static final ECSComponentHandler<TransformComponent> TRANSFORM_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(TransformComponent.class);

    private static final Class[] REQUIRE_CLASSES = new Class[]{TransformComponent.class};

    private TransformComponent component;

    private boolean needUpdate = false;

    // 回调
    private IMoveEntityPacketBuilder moveEntityPacketBuilder;

    public float getX() {
        return this.component.getX();
    }

    public int getFloorX() {
        return (int) this.component.getX();
    }

    public void setX(float x) {
        if (x != this.component.getX()) {
            this.component.setX(x);

            this.needUpdate = true;
        }
    }

    public float getY() {
        return this.component.getY();
    }

    public int getFloorY() {
        return (int) this.component.getY();
    }

    public void setY(float y) {
        if (y != this.component.getY()) {
            this.component.setY(y);

            this.needUpdate = true;
        }
    }

    public float getZ() {
        return this.component.getZ();
    }

    public int getFloorZ() {
        return (int) this.component.getZ();
    }

    public void setZ(float z) {
        if (z != this.component.getZ()) {
            this.component.setZ(z);

            this.needUpdate = true;
        }
    }

    public void setPosition(float x, float y, float z) {
        this.component.setX(x);
        this.component.setY(y);
        this.component.setZ(z);

        this.needUpdate = true;
    }

    public void setPosition(Vector3f position) {
        this.component.setX(position.getX());
        this.component.setY(position.getY());
        this.component.setZ(position.getZ());

        this.needUpdate = true;
    }

    public Vector3f getPosition() {
        return new Vector3f(this.component.getX(), this.component.getY(), this.component.getZ());
    }

    public Vector3 getFloorPosition() {
        return new Vector3((int) this.component.getX(), (int) this.component.getY(), (int) this.component.getZ());
    }

    public float getPitch() {
        return this.component.getPitch();
    }

    public void setPitch(float pitch) {
        if (pitch != this.component.getPitch()) {
            this.component.setPitch(pitch);

            this.needUpdate = true;
        }
    }

    public float getYaw() {
        return this.component.getYaw();
    }

    public void setYaw(float yaw) {
        if (yaw != this.component.getYaw()) {
            this.component.setYaw(yaw);

            this.needUpdate = true;
        }
    }

    public float getYawHead() {
        return this.component.getYawHead();
    }

    public void setYawHead(float yawHead) {
        if (yawHead != this.component.getYawHead()) {
            this.component.setYawHead(yawHead);

            this.needUpdate = true;
        }
    }

    public void setDirection(float pitch, float yaw, float yawHead) {
        this.component.setPitch(pitch);
        this.component.setYaw(yaw);
        this.component.setYawHead(yawHead);

        this.needUpdate = true;
    }

    public void setDirection(Direction direction) {
        this.component.setPitch(direction.getPitch());
        this.component.setYaw(direction.getYaw());
        this.component.setYawHead(direction.getYawHead());

        this.needUpdate = true;
    }

    public Direction getDirection() {
        return new Direction(this.component.getPitch(), this.component.getYaw(), this.component.getYawHead());
    }

    public boolean isOnGround() {
        return this.component.isOnGround();
    }

    public void setOnGround(boolean onGround) {
        if (onGround != this.component.isOnGround()) {
            this.component.setOnGround(onGround);

            this.needUpdate = true;
        }
    }

    public boolean needUpdate() {
        return needUpdate;
    }

    public void markUpdated() {
        this.needUpdate = false;
    }

    public IMoveEntityPacketBuilder getMoveEntityPacketBuilder() {
        return moveEntityPacketBuilder;
    }

    public void setMoveEntityPacketBuilder(IMoveEntityPacketBuilder moveEntityPacketBuilder) {
        this.moveEntityPacketBuilder = moveEntityPacketBuilder;
    }

    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return REQUIRE_CLASSES;
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.component = TRANSFORM_COMPONENT_HANDLER.getComponent(gameObject);
    }
}
