package com.particle.game.entity.state.model;

import com.google.common.collect.Maps;

import java.util.Map;

public enum EntityStateType {

    // ------------------- 生物的基础状态 -------------------------
    /**
     * 吃饱了的状态
     */
    FULL_STATUS("FullStatus", -1, 6000),

    // ------------------- 生物AI的状态 -------------------------
    /**
     * 发情状态
     */
    ESTRUS_STATUS("EstrusStatus", 500, 6000),

    // ------------------- 生物MetaData的状态 -------------------------
    /**
     * 毛被剪掉的状态
     */
    SHEARED("Sheared", -1, -1),
    /**
     * 起火的状态
     */
    ON_FIRE("OnFire", 1000, 5000),

    // ------------------- 生物药水的状态 -------------------------
    /**
     * 加速的状态
     */
    SPEED("speed", -1, -1),
    /**
     * 减速的状态
     */
    SLOWNESS("slowness", -1, -1),
    /**
     * 急迫的状态
     */
    HASTE("haste", -1, -1),
    /**
     * 疲劳的状态
     */
    FATIGUE("mining_fatigue", -1, -1),
    /**
     * 力量的状态
     */
    STRENGTH("strength", -1, -1),
    /**
     * 跳跃加成的状态
     */
    LEAPING("jump_boost", -1, -1),
    /**
     * 恶心的状态
     */
    NAUSEA("nausea", -1, -1),
    /**
     * 恢复的状态
     */
    REGENERATION("regeneration", -1, -1),
    /**
     * 伤害抗性的状态
     */
    DAMAGE_RESISTANCE("resistance", -1, -1),
    /**
     * 起火抗性的状态
     */
    FIRE_RESISTANCE("fire_resistance", -1, -1),
    /**
     * 水下呼吸的状态
     */
    WATER_BREATHING("water_breathing", -1, -1),
    /**
     * 隐身的状态
     */
    INVISIBILITY("invisibility", -1, -1),
    /**
     * 致盲的状态
     */
    BLINDNESS("blindness", -1, -1),
    /**
     * 夜视的状态
     */
    NIGHT_VISION("night_vision", -1, -1),
    /**
     * 饥饿的状态
     */
    HUNGER("hunger", -1, -1),
    /**
     * 虚弱的状态
     */
    WEAKNESS("weakness", -1, -1),
    /**
     * 中毒的状态
     */
    POISON("poison", -1, -1),
    /**
     * 凋零的状态
     */
    WITHER("wither", -1, -1),
    /**
     * 生命恢复加速的状态
     */
    HEALTH_BOOST("health_boost", -1, -1),
    /**
     * 伤害吸收
     */
    ABSORPTION("absorption", -1, -1),


    ;

    /**
     * 状态名称
     */
    private String name;

    /**
     * 默认更新间隔
     */
    private long defaultUpdateInterval = -1;

    /**
     * 默认有效期
     */
    private long defaultTimeToLive = -1;

    EntityStateType(String name, long defaultUpdateInterval, long defaultTimeToLive) {
        this.name = name;
        this.defaultUpdateInterval = defaultUpdateInterval;
        this.defaultTimeToLive = defaultTimeToLive;
    }

    public String getName() {
        return name;
    }

    public long getDefaultTimeToLive() {
        return defaultTimeToLive;
    }

    public long getDefaultUpdateInterval() {
        return defaultUpdateInterval;
    }

    public static EntityStateType getEntityStateTypeByName(String name) {
        return name2EntityStateType.get(name);
    }

    private static final Map<String, EntityStateType> name2EntityStateType;

    static {
        Map<String, EntityStateType> _name2EntityStateType = Maps.newHashMap();
        for (EntityStateType value : values()) {
            _name2EntityStateType.put(value.getName(), value);
        }
        name2EntityStateType = _name2EntityStateType;
    }
}
