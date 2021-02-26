package com.particle.game.entity.ai.components;

import com.google.common.collect.Maps;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import java.util.List;
import java.util.Map;

public enum Knowledge {

    /**
     * Entity目标，用于攻击、追踪
     */
    ENTITY_TARGET("EntityTarget", Entity.class),
    /**
     * 被攻击时的攻击者
     */
    ENTITY_CRIMINAL("EntityCriminal", Entity.class),
    /**
     * 生物间消息传递的来源
     */
    ENTITY_SPEAKER("EntitySpeaker", Entity.class),
    /**
     * 被交互时的交互者
     */
    ENTITY_INTERACTOR("EntityInteractor", Entity.class),
    /**
     * 位置目标，用于移动
     */
    POSITION_TARGET("PositionTarget", Vector3f.class),
    /**
     * 下一个移动位置，用于移动
     */
    POSITION_MOVE("PositionMove", Vector3f.class),
    /**
     * 寻路节点列表
     */
    ROAD_POINTS("RoadPositions", List.class),
    /**
     * 状态时间戳
     */
    STATE_TIMESTAMP("StateTimestamp", Long.class),
    /**
     * 停留状态时间戳
     */
    STAY_POSITION_TIMESTAMP("StayPositionTimestamp", Long.class),
    /**
     * 上次移动的时间戳，用于控制间歇移动
     */
    LAST_MOVE_TIMESTAMP("StateTimestamp", Long.class),
    /**
     * 检查生物是否卡住
     */
    STOCKED("Stocked", Boolean.class),
    /**
     * 检查生物右边是否可以通行
     */
    RIGHT_STOCKED("RightStocked", Boolean.class),

    /**
     * 检查手持物是否改變
     */
    ITEM_IN_HAND_CACHE("ItemInHandCache", Integer.class),

    /**
     * 慌亂狀態
     */
    FLURRIED_STATUS("FlurriedStatus", Boolean.class),

    /**
     * 發情狀態
     */
    ESTRUS_STATUS("EstrusStatus", Boolean.class),

    /**
     * 相親狀態
     */
    BLIND_DATE("BlindDate", Entity.class),

    /**
     * 發情冷卻
     */
    ESTRUS_COOL_DOWN("EstrusCoolDown", Long.class),

    /**
     * 游泳狀態
     */
    SWIMMING_STATUS("SwimmingStatus", Boolean.class),

    /**
     * 远程攻击的蓄力
     */
    REMOTE_ATTACK_POWER("RemoteAttackPower", Float.class),

    /**
     * 下沉状态
     */
    SINK_STATUS("SinkStatus", Boolean.class),

    /**
     * 无敌时间
     */
    INVINCIBLE_TIME("InvincibleTime", Long.class),

    /**
     * 无敌状态
     */
    INVINCIBLE_STATUS("InvincibleStatus", Boolean.class),

    /**
     * 冷却时间
     */
    COOLING_TIME("CoolingTime", Long.class),

    /**
     * 播放粒子时间
     */
    PLAY_PARTICLE_TIME("PlayParticleTime", Long.class),

    /**
     * 是否变身
     */
    TRANSFORMATION_STATUS("TransformationStatus", Boolean.class),

    /**
     * 自定义状态
     */
    CUSTOM_STATUS("CustomStatus", Integer.class),

    /**
     * 技能冷却时间
     */
    SKILL_CD_TIME("SkillCdTime", Long.class),

    /**
     * 增强结束时间
     */
    UPGRADE_END_TIME("UpgradeEndTime", Long.class),

    /**
     * 家园位置信息
     */
    HOME_POSITION_INFO("HomePositionInfo", String.class),

    ;

    private String key;
    private Class type;

    Knowledge(String key, Class type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public Class getType() {
        return type;
    }

    private static final Map<String, Knowledge> key2Knowledge = Maps.newHashMap();

    static {
        for (Knowledge value : values()) {
            key2Knowledge.put(value.getKey(), value);
        }
    }

    public static Knowledge parse(String key) {
        return key2Knowledge.get(key);
    }
}
