package com.particle.model.level.settings;

import java.util.ArrayList;
import java.util.List;

public class GameRule {

    private static List<GameRule> ruleDatas = new ArrayList<>();

    static {
        ruleDatas.add(new GameRule(GameRuleName.COMMAND_BLOCK_OUTPUT, true));
        ruleDatas.add(new GameRule(GameRuleName.DO_DAYLIGHT_CYCLE, true));
        ruleDatas.add(new GameRule(GameRuleName.DO_ENTITY_DROPS, true));
        ruleDatas.add(new GameRule(GameRuleName.DO_FIRE_TICK, true));
        ruleDatas.add(new GameRule(GameRuleName.DO_MOB_LOOT, true));
        ruleDatas.add(new GameRule(GameRuleName.DO_MOB_SPAWNING, true));
        ruleDatas.add(new GameRule(GameRuleName.DO_TILE_DROPS, true));
        ruleDatas.add(new GameRule(GameRuleName.DO_WEATHER_CYCLE, true));
        ruleDatas.add(new GameRule(GameRuleName.DROWNING_DAMAGE, true));
        ruleDatas.add(new GameRule(GameRuleName.FALL_DAMAGE, true));
        ruleDatas.add(new GameRule(GameRuleName.FIRE_DAMAGE, true));
        ruleDatas.add(new GameRule(GameRuleName.KEEP_INVENTORY, false));
        ruleDatas.add(new GameRule(GameRuleName.MOB_GRIEFING, true));
        ruleDatas.add(new GameRule(GameRuleName.NATURAL_REGENERATION, false));
        ruleDatas.add(new GameRule(GameRuleName.PVP, true));
        ruleDatas.add(new GameRule(GameRuleName.SEND_COMMAND_FEEDBACK, true));
        ruleDatas.add(new GameRule(GameRuleName.SHOW_COORDINATES, true));
        ruleDatas.add(new GameRule(GameRuleName.TNT_EXPLODES, true));
        ruleDatas.add(new GameRule(GameRuleName.SHOW_DEATH_MESSAGE, true));
    }

    /**
     * 默认的GameRule
     *
     * @return
     */
    public static List<GameRule> getDefaultGameRules() {
        return ruleDatas;
    }

    private GameRuleType ruleType;

    private String ruleName;

    /**
     * dependend on ruleType = GameRuleType.Bool
     */
    private boolean boolValue;

    /**
     * dependend on ruleType = GameRuleType.Int
     */
    private int intValue;

    /**
     * dependend on ruleType = GameRuleType.Float
     */
    private float floatValue;

    public GameRule() {
    }

    public GameRule(GameRuleName ruleName, boolean boolValue) {
        this.ruleName = ruleName.getName();
        this.boolValue = boolValue;
        this.ruleType = GameRuleType.Bool;
    }

    public GameRule(GameRuleName ruleName, int intValue) {
        this.ruleName = ruleName.getName();
        this.intValue = intValue;
        this.ruleType = GameRuleType.Int;
    }

    public GameRule(GameRuleName ruleName, float floatValue) {
        this.ruleName = ruleName.getName();
        this.floatValue = floatValue;
        this.ruleType = GameRuleType.Float;
    }

    public GameRuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(GameRuleType ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public boolean isBoolValue() {
        return boolValue;
    }

    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }
}
