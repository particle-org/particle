package com.particle.game.world.physical.module;

import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import java.util.UUID;
import java.util.function.Consumer;

public class Arbiter {

    private UUID uuid;

    private Entity a;
    private Entity b;

    private Consumer<Entity> colliderCallbackA;
    private Consumer<Entity> colliderCallbackB;

    private Vector3f positionA;
    private Vector3f positionB;

    /**
     * 构造UUID，用于hash缓存的key
     *
     * @param a
     * @param b
     * @return
     */
    public static UUID caculateUUID(Entity a, Entity b) {
        if (a.ID < b.ID) {
            return new UUID(a.ID, b.ID);
        } else {
            return new UUID(b.ID, a.ID);
        }
    }

    public Arbiter(Entity a, Entity b, Consumer<Entity> colliderCallbackA, Consumer<Entity> colliderCallbackB, Vector3f positionA, Vector3f positionB) {
        if (a.ID < b.ID) {
            this.a = a;
            this.b = b;
            this.colliderCallbackA = colliderCallbackA;
            this.colliderCallbackB = colliderCallbackB;
            this.positionA = positionA;
            this.positionB = positionB;
        } else {
            this.a = b;
            this.b = a;
            this.colliderCallbackA = colliderCallbackB;
            this.colliderCallbackB = colliderCallbackA;
            this.positionA = positionB;
            this.positionB = positionA;
        }

        this.uuid = new UUID(this.a.ID, this.b.ID);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Entity getA() {
        return a;
    }

    public Entity getB() {
        return b;
    }

    public Consumer<Entity> getColliderCallbackA() {
        return colliderCallbackA;
    }

    public Consumer<Entity> getColliderCallbackB() {
        return colliderCallbackB;
    }

    public Vector3f getPositionA() {
        return positionA;
    }

    public Vector3f getPositionB() {
        return positionB;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Arbiter) {
            Arbiter arbiter = (Arbiter) o;

            return arbiter.a.ID == this.a.ID && arbiter.b.ID == this.b.ID;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.a.hashCode() + this.b.hashCode();
    }
}
