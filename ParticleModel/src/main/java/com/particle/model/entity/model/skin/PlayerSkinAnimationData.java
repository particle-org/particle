package com.particle.model.entity.model.skin;

public class PlayerSkinAnimationData {

    private int width;
    private int height;
    // 这里和皮肤统一用byte[]，而不是String
    private byte[] images;
    private int animationType;
    private float frameCount;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public byte[] getImages() {
        return images;
    }

    public void setImages(byte[] images) {
        this.images = images;
    }

    public int getAnimationType() {
        return animationType;
    }

    public void setAnimationType(int animationType) {
        this.animationType = animationType;
    }

    public float getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(float frameCount) {
        this.frameCount = frameCount;
    }
}
