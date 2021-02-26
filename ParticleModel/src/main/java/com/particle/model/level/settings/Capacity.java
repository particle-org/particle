package com.particle.model.level.settings;

public class Capacity {

    private int miniNum = 1;

    private int maxNum = 10;

    private int currentNum;

    public int getMiniNum() {
        return miniNum;
    }

    public void setMiniNum(int miniNum) {
        this.miniNum = miniNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }
}
