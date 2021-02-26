package com.particle.network.handler.v354;

import com.particle.model.level.settings.*;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.StartGamePacket;
import com.particle.model.player.GameMode;
import com.particle.model.player.settings.PlayerPermissionLevel;
import com.particle.network.encoder.BlockListEncoder;
import com.particle.network.encoder.GameRulesHandler;
import com.particle.network.handler.AbstractPacketHandler;
import com.particle.network.utils.BlockInfoReader;

import java.util.HashMap;
import java.util.Map;

public class StartGamePacketHandler354 extends AbstractPacketHandler<StartGamePacket> {

    private GameRulesHandler gameRulesHandler = GameRulesHandler.getInstance();

    private BlockListEncoder blockListEncoder = new BlockListEncoder(BlockInfoReader.read("block_state_list_V1_11.json"));

    @Override
    protected void doDecode(StartGamePacket dataPacket, int version) {
        dataPacket.setEntityUniqueId(dataPacket.readSignedVarLong().longValue());
        dataPacket.setEntityRuntimeId(dataPacket.readUnsignedVarLong());
        dataPacket.setPlayerGamemode(dataPacket.readSignedVarInt());

        dataPacket.setX(dataPacket.readLFloat());
        dataPacket.setY(dataPacket.readLFloat());
        dataPacket.setZ(dataPacket.readLFloat());
        dataPacket.setYaw(dataPacket.readLFloat());
        dataPacket.setPitch(dataPacket.readLFloat());

        LevelSettings settings = this.decodeLevelSettings(dataPacket, version);
        dataPacket.setSettings(settings);

        dataPacket.setLevelId(dataPacket.readString());
        dataPacket.setWorldName(dataPacket.readString());
        dataPacket.setPremiumWorldTemplateId(dataPacket.readString());
        dataPacket.setTrial(dataPacket.readBoolean());
        dataPacket.setCurrentLevelTime(dataPacket.readLLong());
        dataPacket.setEnchantmentSeed(dataPacket.readSignedVarInt());

        // blockTable
        int count = dataPacket.readUnsignedVarInt();
        for (int i = 0; i < count; i++) {
            dataPacket.readString();
            dataPacket.readLShort();
        }
        // 1.7
        dataPacket.readString();
    }

    /**
     * decode version
     *
     * @param dataPacket
     * @param version
     * @return
     */
    private LevelSettings decodeLevelSettings(DataPacket dataPacket, int version) {
        //LevelSetting
        LevelSettings levelSettings = new LevelSettings();
        levelSettings.setSeed(dataPacket.readSignedVarInt());

        levelSettings.setDimension(Dimension.valueOf(dataPacket.readSignedVarInt()));
        levelSettings.setGeneratorType(GeneratorType.valueOf(dataPacket.readSignedVarInt()));
        levelSettings.setGameMode(GameMode.valueOf(dataPacket.readSignedVarInt()));
        levelSettings.setGameDifficulty(Difficulty.valueOf(dataPacket.readSignedVarInt()));

        levelSettings.setSpawnX(dataPacket.readSignedVarInt());
        levelSettings.setSpawnY(dataPacket.readUnsignedVarInt());
        levelSettings.setSpawnZ(dataPacket.readSignedVarInt());
        levelSettings.setHasAchievementsDisabled(dataPacket.readBoolean());
        levelSettings.setDayCycleStopTime(dataPacket.readSignedVarInt());
        levelSettings.setEduMode(dataPacket.readBoolean());
        levelSettings.setEduFeature(dataPacket.readBoolean());
        levelSettings.setRainLevel(dataPacket.readLFloat());
        levelSettings.setLightningLevel(dataPacket.readLFloat());

        levelSettings.setConfirmedPlatformLockedContent(dataPacket.readBoolean());
        levelSettings.setMultiplayerGame(dataPacket.readBoolean());
        levelSettings.setBroadcastToLAN(dataPacket.readBoolean());

        dataPacket.readSignedVarInt();
        levelSettings.setGamePublishSetting(GamePublishSetting.valueOf(dataPacket.readSignedVarInt()));
        levelSettings.setCommandsEnabled(dataPacket.readBoolean());
        levelSettings.setTexturePacksRequired(dataPacket.readBoolean());

        //Skip GameRule
        Map<String, GameRule> rules = new HashMap<>();
        long size = dataPacket.readUnsignedVarInt();
        for (int i = 0; i < size; i++) {
            GameRule gameRule = gameRulesHandler.decode(dataPacket, version);
            rules.put(gameRule.getRuleName(), gameRule);
        }
        levelSettings.setRuleDatas(rules);

        levelSettings.setBonusChest(dataPacket.readBoolean());
        levelSettings.setStartMapEnabled(dataPacket.readBoolean());
        levelSettings.setPlayerPermissions(PlayerPermissionLevel.valueOf(dataPacket.readSignedVarInt()));
        levelSettings.setServerChunkTickRange(dataPacket.readInt());
        levelSettings.setHasLockBehaviroPack(dataPacket.readBoolean());
        levelSettings.setHasLockResourcePack(dataPacket.readBoolean());
        levelSettings.setFromLockedTemplate(dataPacket.readBoolean());

        // 1.7
        levelSettings.setUseMsaGametagsOnly(dataPacket.readBoolean());

        // 1.8
        levelSettings.setWorldFromTemplate(dataPacket.readBoolean());
        levelSettings.setWorldTemplateOptionLocked(dataPacket.readBoolean());
        return levelSettings;
    }

    @Override
    protected void doEncode(StartGamePacket dataPacket, int version) {
        dataPacket.writeSignedVarLong(dataPacket.getEntityUniqueId());
        dataPacket.writeUnsignedVarLong(dataPacket.getEntityRuntimeId());
        dataPacket.writeSignedVarInt(dataPacket.getPlayerGamemode());

        dataPacket.writeLFloat(dataPacket.getX());
        dataPacket.writeLFloat(dataPacket.getY());
        dataPacket.writeLFloat(dataPacket.getZ());
        dataPacket.writeLFloat(dataPacket.getYaw());
        dataPacket.writeLFloat(dataPacket.getPitch());

        this.doEncodeLevelSettings(dataPacket, version, dataPacket.getSettings());

        dataPacket.writeString(dataPacket.getLevelId());
        dataPacket.writeString(dataPacket.getWorldName());
        dataPacket.writeString(dataPacket.getPremiumWorldTemplateId());
        dataPacket.writeBoolean(dataPacket.isTrial());
        dataPacket.writeLLong(dataPacket.getCurrentLevelTime());
        dataPacket.writeSignedVarInt(dataPacket.getEnchantmentSeed());

        // blockTable
        this.blockListEncoder.encode(dataPacket);

        // 1.7
        dataPacket.writeString(dataPacket.getMultiPlayerCorrelationId());
    }

    /**
     * @param dataPacket
     * @param version
     * @param levelSettings
     */
    private void doEncodeLevelSettings(DataPacket dataPacket, int version, LevelSettings levelSettings) {
        //LevelSetting
        dataPacket.writeSignedVarInt(levelSettings.getSeed());
        dataPacket.writeSignedVarInt(levelSettings.getDimension().getMode());
        dataPacket.writeSignedVarInt(levelSettings.getGeneratorType().getType());
        dataPacket.writeSignedVarInt(levelSettings.getGameMode().getMode());
        dataPacket.writeSignedVarInt(levelSettings.getGameDifficulty().getMode());

        dataPacket.writeSignedVarInt(levelSettings.getSpawnX());
        dataPacket.writeUnsignedVarInt(levelSettings.getSpawnY());
        dataPacket.writeSignedVarInt(levelSettings.getSpawnZ());
        dataPacket.writeBoolean(levelSettings.isHasAchievementsDisabled());
        dataPacket.writeSignedVarInt(levelSettings.getDayCycleStopTime());
        dataPacket.writeBoolean(levelSettings.isEduMode());
        dataPacket.writeBoolean(levelSettings.isEduFeature());
        dataPacket.writeLFloat(levelSettings.getRainLevel());
        dataPacket.writeLFloat(levelSettings.getLightningLevel());

        dataPacket.writeBoolean(levelSettings.isConfirmedPlatformLockedContent());
        dataPacket.writeBoolean(levelSettings.isMultiplayerGame());
        dataPacket.writeBoolean(levelSettings.isBroadcastToLAN());

        dataPacket.writeSignedVarInt(0);
        dataPacket.writeSignedVarInt(levelSettings.getGamePublishSetting().getMode());

        dataPacket.writeBoolean(levelSettings.isCommandsEnabled());
        dataPacket.writeBoolean(levelSettings.isTexturePacksRequired());

        //skip game rule
        int size = levelSettings.getRuleDatas().size();
        dataPacket.writeUnsignedVarInt(size);
        for (GameRule gameRule : levelSettings.getRuleDatas().values()) {
            this.gameRulesHandler.encode(dataPacket, gameRule, version);
        }

        dataPacket.writeBoolean(levelSettings.isBonusChest());
        dataPacket.writeBoolean(levelSettings.isStartMapEnabled());
        dataPacket.writeSignedVarInt(levelSettings.getPlayerPermissions().getLevel());
        dataPacket.writeLInt(levelSettings.getServerChunkTickRange());

        dataPacket.writeBoolean(levelSettings.isHasLockBehaviroPack());
        dataPacket.writeBoolean(levelSettings.isHasLockResourcePack());
        dataPacket.writeBoolean(levelSettings.isFromLockedTemplate());

        // 1.7
        dataPacket.writeBoolean(levelSettings.isUseMsaGametagsOnly());

        // 1.8
        dataPacket.writeBoolean(levelSettings.isWorldFromTemplate());
        dataPacket.writeBoolean(levelSettings.isWorldTemplateOptionLocked());
    }
}
