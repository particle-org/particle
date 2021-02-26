package com.particle.game.block.common.components;

import com.particle.core.ecs.component.ECSComponent;

public class CookComponent implements ECSComponent {

    public final static int STATUS_START = 0;

    public final static int STATUS_RUNNING = 1;

    public final static int STATUS_END = 2;

    public final static int STATUS_INVALID = 3;

    public static final int MAX_BREWING_TIME = 400;

    /**
     * 时间
     */
    private int cookTime = MAX_BREWING_TIME;

    /**
     * 0表示未开始，1表示酿造中，2表示已结束, 3不合法
     */
    private int status = STATUS_END;

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
