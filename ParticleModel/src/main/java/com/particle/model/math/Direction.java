package com.particle.model.math;

public class Direction {
    private float yaw;
    private float yawHead;
    private float pitch;

    //注意顺序！！！
    public Direction(float pitch, float yaw, float yawHead) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.yawHead = yawHead;
    }

    public Direction(Vector3f direction) {
        float yaw = 360 - (float) (Math.toDegrees(Math.atan2(direction.getX(), direction.getZ())));
        float pitch = direction.getY() == 0 ? 0 : -(float) Math.toDegrees(Math.atan2(direction.getY(), Math.sqrt(direction.getX() * direction.getX() + direction.getZ() * direction.getZ())));

        this.pitch = pitch;
        this.yaw = yaw;
        this.yawHead = yaw;
    }

    public Direction(Vector3 direction) {
        float yaw = 360 - (float) Math.toDegrees(Math.atan2(direction.getX(), direction.getZ()));
        float pitch = direction.getY() == 0 ? 0 : -(float) Math.toDegrees(Math.atan2(direction.getY(), Math.sqrt(direction.getX() * direction.getX() + direction.getZ() * direction.getZ())));

        this.pitch = pitch;
        this.yaw = yaw;
        this.yawHead = yaw;
    }

    public void update(float yaw, float pitch) {
        this.yaw = yaw;
        this.yawHead = yaw;
        this.pitch = pitch;
    }

    public void update(Direction direction) {
        this.yaw = direction.getYaw();
        this.yawHead = direction.getYawHead();
        this.pitch = direction.getPitch();
    }

    public Vector3f getDirectionVector() {
        double pitch = ((this.pitch + 90) * Math.PI) / 180;
        double yaw = ((this.yaw + 90) * Math.PI) / 180;

        float x = (float) (Math.sin(pitch) * Math.cos(yaw));
        float z = (float) (Math.sin(pitch) * Math.sin(yaw));
        float y = (float) Math.cos(pitch);

        return new Vector3f(x, y, z).normalize();
    }

    public Vector3f getAheadDirectionVector() {
        double yaw = ((this.yaw + 90) * Math.PI) / 180;

        float x = (float) Math.cos(yaw);
        float z = (float) Math.sin(yaw);

        return new Vector3f(x, 0, z).normalize();
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getYawHead() {
        return yawHead;
    }

    public void setYawHead(float yawHead) {
        this.yawHead = yawHead;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public Direction backend() {
        float yaw = this.yaw + 180;
        if (yaw > 180) yaw -= 360;

        this.pitch = -this.pitch;

        return new Direction(pitch, yaw, yaw);
    }

    public Direction left(float rotate) {
        float yaw = this.yaw + rotate;
        if (yaw > 180) yaw -= 360;

        this.pitch = 0;

        return new Direction(pitch, yaw, yaw);
    }

    public Direction right(float rotate) {
        float yaw = this.yaw - rotate;
        if (yaw < -180) yaw += 360;

        this.pitch = 0;

        return new Direction(pitch, yaw, yaw);
    }

    @Override
    public String toString() {
        return String.format("Direction(pitch=%f,yaw=%f,yawHead=%f)", this.pitch, this.yaw, this.yawHead);
    }
}
