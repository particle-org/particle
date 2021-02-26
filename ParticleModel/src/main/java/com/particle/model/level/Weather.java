package com.particle.model.level;

public enum Weather {
    RAIN(1, "下雨", "rain"),
    THUNDER(2, "雷暴", "thunder"),
    SUNNY(3, "晴天", "clear");

    /**
     * 类型
     */
    private int type;

    /**
     * 中文提示
     */
    private String toast;

    /**
     * 指令名
     */
    private String cmdName;

    Weather(int type, String toast, String cmdName) {
        this.type = type;
        this.toast = toast;
        this.cmdName = cmdName;
    }

    public int getType() {
        return type;
    }

    public String getToast() {
        return toast;
    }

    public String getCmdName() {
        return cmdName;
    }
}
