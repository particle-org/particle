package com.particle.model.level;

import java.util.HashMap;
import java.util.Map;

public enum TimeCycle implements Comparable<TimeCycle> {
    DAY_START(0, "Minecraft一天的开始。"),
    OVER_SUNRISE(450, "日出结束，地平线颜色不再变化。"),
    TIME_SET_DAY(1000, "/time set day命令设置的时间。"),
    MAX_BRIGHTNESS(4283, "亮度达到最大值15。"),
    MID_DAY(6000, "正午, 太阳处于正中。"),
    START_DECREASE_BRIGHTNESS(7700, "亮度开始降低。"),
    START_SUNSET(11616, "日落开始，地平线开始昏暗。"),
    DAY_OVER(12000, "白天结束"),
    CAN_TO_BED(12516, "开始可以上床睡觉。"),
    MOON_APPEAR(12566, "月亮出现在地平线上，亡灵生物开始生成。"),
    ATTACK_START_IN_RAINY(12966, "雨天时户外自然生成攻击型生物的第一刻。"),
    TIME_SER_NIGHT(13000, "/time set night命令设置的时间。"),
    SUN_DISAPPEAR(13050, "太阳完全消失于地平线。"),
    ATTACK_START_IN_SUNNY(13183, "晴天时户外自然生成攻击型生物的第一刻。"),
    OVER_SUNSET(13800, "日落结束。地平线颜色不再变化。"),
    MID_NIGHT(18000, "午夜, 月亮处于正中。"),
    START_SUNRISE(22550, "日出开始, 地平线开始变亮。"),
    ATTACK_STOP_IN_SUNNY(22800, "晴天时户外自然生成攻击型生物的最后一刻。"),
    SUN_APPEAR(22916, "太阳出现在地平线上。"),
    TIME_SET_SUNRISE(23000, "/time set sunrise使用命令设置的时间。 "),
    ATTACK_STOP_IN_RAINY(23016, "雨天时户外自然生成攻击型生物的最后一刻。"),
    MOON_DISAPPEAR(23450, "月亮完全消失于地平线，符合条件的亡灵生物开始自燃。");

    /**
     * 一天tick的时候
     */
    private int tick;

    /**
     * 提示语
     */
    private String toast;

    private static Map<Integer, TimeCycle> timeCycleMap = new HashMap<>();

    static {
        for (TimeCycle cycle : TimeCycle.values()) {
            timeCycleMap.put(cycle.tick, cycle);
        }
    }

    /**
     * 根据tick查找上一个节点
     *
     * @param tick
     * @return
     */
    public static TimeCycle fromTick(long tick) {
        int tickIndex = (int) (tick % 24000);
        TimeCycle[] cycles = TimeCycle.values();
        for (int index = cycles.length - 1; index >= 0; index--) {
            if (tickIndex >= cycles[index].tick) {
                return cycles[index];
            }
        }
        return TimeCycle.DAY_START;
    }

    TimeCycle(int tick, String toast) {
        this.tick = tick;
        this.toast = toast;
    }

    public int getTick() {
        return tick;
    }

    public String getToast() {
        return toast;
    }

}
