package com.particle.network.encoder;

import com.particle.model.level.settings.GameRule;
import com.particle.model.level.settings.GameRuleType;
import com.particle.model.network.packets.DataPacket;


public class GameRulesHandler extends ModelHandler<GameRule> {

    /**
     * 单例对象
     */
    private static final GameRulesHandler INSTANCE = new GameRulesHandler();

    /**
     * 获取单例
     */
    public static GameRulesHandler getInstance() {
        return GameRulesHandler.INSTANCE;
    }

    @Override
    public GameRule decode(DataPacket dataPacket, int version) {
        GameRule gameRule = new GameRule();
        String name = dataPacket.readString();
        gameRule.setRuleName(name);
        int type = dataPacket.readUnsignedVarInt();
        GameRuleType ruleType = GameRuleType.valueOf(type);
        gameRule.setRuleType(ruleType);
        if (ruleType == GameRuleType.Bool) {
            gameRule.setBoolValue(dataPacket.readBoolean());
        } else if (ruleType == GameRuleType.Int) {
            gameRule.setIntValue(dataPacket.readUnsignedVarInt());
        } else if (ruleType == GameRuleType.Float) {
            gameRule.setFloatValue(dataPacket.readLFloat());
        }
        return gameRule;
    }

    @Override
    public void encode(DataPacket dataPacket, GameRule gameRule, int version) {
        dataPacket.writeString(gameRule.getRuleName());
        GameRuleType ruleType = gameRule.getRuleType();
        dataPacket.writeUnsignedVarInt(ruleType.getType());
        if (ruleType == GameRuleType.Bool) {
            dataPacket.writeBoolean(gameRule.isBoolValue());
        } else if (ruleType == GameRuleType.Int) {
            dataPacket.writeUnsignedVarInt(gameRule.getIntValue());
        } else if (ruleType == GameRuleType.Float) {
            dataPacket.writeLFloat(gameRule.getFloatValue());
        }
    }
}
