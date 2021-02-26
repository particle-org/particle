package com.particle.model.level;

public enum LevelStatus {
    // Level刚刚初始化的状态
    INITIALIZATION(0),
    RUNNING(1),
    DESTROYING(3),
    CLOSED(4);

    LevelStatus(int status) {
        this.status = status;
    }

    private int status;

    public int getStatus() {
        return status;
    }
}
