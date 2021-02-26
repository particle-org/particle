package com.particle.model.level.settings;

import com.particle.model.math.Vector3;
import com.particle.model.player.GameMode;
import com.particle.model.player.settings.PlayerPermissionLevel;

import java.util.Map;
import java.util.stream.Collectors;

public class LevelSettings {

    // ----- 与客户端同步的数据 -----
    private int seed;

    short spawnBiomeType = 0x00;

    String userDefinedBiomeName = "";

    private Dimension dimension;

    private GeneratorType generatorType = GeneratorType.Overworld;

    private GameMode gameMode;

    private Difficulty gameDifficulty;

    private int spawnX;
    private int spawnY;
    private int spawnZ;

    private boolean hasAchievementsDisabled = true;
    private int dayCycleStopTime = -1;
    @Deprecated
    private boolean eduMode = false;
    private int educationEditionOffer = 0;
    private boolean eduFeature = false;
    private float rainLevel;
    private float lightningLevel;
    private boolean confirmedPlatformLockedContent = false;
    private boolean multiplayerGame = true;
    private boolean broadcastToLAN = true;
    private boolean broadcastToXboxLive = true;
    private boolean commandsEnabled;
    private boolean isTexturePacksRequired = false;

    private Map<String, GameRule> ruleDatas = GameRule.getDefaultGameRules().stream().collect(
            Collectors.toMap(GameRule::getRuleName, gameRule -> gameRule)
    );

    private boolean bonusChest = false;
    private boolean startMapEnabled = false;
    private boolean trustPlayers = false;

    private PlayerPermissionLevel playerPermissions = PlayerPermissionLevel.Member;
    private GamePublishSetting gamePublishSetting = GamePublishSetting.Public;

    private int serverChunkTickRange = 4;

    private boolean canPlatformBroadcast = false;

    private GamePublishSetting broadcastMode = GamePublishSetting.FriendsOfFriends;

    private boolean xblBroadcastIntent = true;

    private boolean hasLockBehaviroPack = false;
    private boolean hasLockResourcePack = false;
    private boolean isFromLockedTemplate = false;

    private boolean useMsaGametagsOnly = false;

    private boolean isWorldFromTemplate = false;

    private boolean isWorldTemplateOptionLocked = false;

    private boolean onlySpawnV1Villagers;

    // ----- 与服务的内部使用的数据 -----

    private boolean forceSpawn = false;
    private boolean tickMobEntity = true;
    private boolean tickItemEntity = true;
    private boolean tickTileEntity = true;
    private boolean tickProjectileEntity = true;
    private boolean tickPlayers = true;
    private boolean tickNPCs = true;
    private boolean tickMonsters = true;
    private int tickInterval = 50;

    private boolean isPlayerVisiable = true;

    /**
     * 世界边界
     */
    private int xMin = Integer.MIN_VALUE;
    private int xMax = Integer.MAX_VALUE;
    private int zMin = Integer.MIN_VALUE;
    private int zMax = Integer.MAX_VALUE;

    /**
     * 区块加载
     */
    private int chunkLoadRadius = 4;
    private int chunkUnloadRadius = 6;

    /**
     * 世界的人数容量
     */
    private int miniPlayerNum;

    private int maxPlayerNum;

    /**
     * 是否只读
     */
    private boolean readOnly = false;

    private String vanillaVersion = "*";

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public GeneratorType getGeneratorType() {
        return generatorType;
    }

    public void setGeneratorType(GeneratorType generatorType) {
        this.generatorType = generatorType;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Difficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public void setGameDifficulty(Difficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }

    public Vector3 getSpawn() {
        return new Vector3(this.spawnX, this.spawnY, this.spawnZ);
    }

    public int getSpawnX() {
        return spawnX;
    }

    public void setSpawnX(int spawnX) {
        this.spawnX = spawnX;
    }

    public int getSpawnY() {
        return spawnY;
    }

    public void setSpawnY(int spawnY) {
        this.spawnY = spawnY;
    }

    public int getSpawnZ() {
        return spawnZ;
    }

    public void setSpawnZ(int spawnZ) {
        this.spawnZ = spawnZ;
    }

    public boolean isHasAchievementsDisabled() {
        return hasAchievementsDisabled;
    }

    public void setHasAchievementsDisabled(boolean hasAchievementsDisabled) {
        this.hasAchievementsDisabled = hasAchievementsDisabled;
    }

    public int getDayCycleStopTime() {
        return dayCycleStopTime;
    }

    public void setDayCycleStopTime(int dayCycleStopTime) {
        this.dayCycleStopTime = dayCycleStopTime;
    }

    @Deprecated
    public boolean isEduMode() {
        return eduMode;
    }

    @Deprecated
    public void setEduMode(boolean eduMode) {
        this.eduMode = eduMode;
    }

    public int getEducationEditionOffer() {
        return educationEditionOffer;
    }

    public void setEducationEditionOffer(int educationEditionOffer) {
        this.educationEditionOffer = educationEditionOffer;
    }

    public boolean isEduFeature() {
        return eduFeature;
    }

    public void setEduFeature(boolean eduFeature) {
        this.eduFeature = eduFeature;
    }

    public float getRainLevel() {
        return rainLevel;
    }

    public void setRainLevel(float rainLevel) {
        this.rainLevel = rainLevel;
    }

    public float getLightningLevel() {
        return lightningLevel;
    }

    public void setLightningLevel(float lightningLevel) {
        this.lightningLevel = lightningLevel;
    }

    public boolean isConfirmedPlatformLockedContent() {
        return confirmedPlatformLockedContent;
    }

    public void setConfirmedPlatformLockedContent(boolean confirmedPlatformLockedContent) {
        this.confirmedPlatformLockedContent = confirmedPlatformLockedContent;
    }

    public boolean isMultiplayerGame() {
        return multiplayerGame;
    }

    public void setMultiplayerGame(boolean multiplayerGame) {
        this.multiplayerGame = multiplayerGame;
    }

    public boolean isBroadcastToLAN() {
        return broadcastToLAN;
    }

    public void setBroadcastToLAN(boolean broadcastToLAN) {
        this.broadcastToLAN = broadcastToLAN;
    }

    public boolean isBroadcastToXboxLive() {
        return broadcastToXboxLive;
    }

    public void setBroadcastToXboxLive(boolean broadcastToXboxLive) {
        this.broadcastToXboxLive = broadcastToXboxLive;
    }

    public boolean isCommandsEnabled() {
        return commandsEnabled;
    }

    public void setCommandsEnabled(boolean commandsEnabled) {
        this.commandsEnabled = commandsEnabled;
    }

    public boolean isTexturePacksRequired() {
        return isTexturePacksRequired;
    }

    public void setTexturePacksRequired(boolean texturePacksRequired) {
        isTexturePacksRequired = texturePacksRequired;
    }

    public Map<String, GameRule> getRuleDatas() {
        return ruleDatas;
    }

    public void setRuleDatas(Map<String, GameRule> ruleDatas) {
        this.ruleDatas = ruleDatas;
    }

    public boolean isBonusChest() {
        return bonusChest;
    }

    public void setBonusChest(boolean bonusChest) {
        this.bonusChest = bonusChest;
    }

    public boolean isStartMapEnabled() {
        return startMapEnabled;
    }

    public void setStartMapEnabled(boolean startMapEnabled) {
        this.startMapEnabled = startMapEnabled;
    }

    public boolean isTrustPlayers() {
        return trustPlayers;
    }

    public void setTrustPlayers(boolean trustPlayers) {
        this.trustPlayers = trustPlayers;
    }

    public PlayerPermissionLevel getPlayerPermissions() {
        return playerPermissions;
    }

    public void setPlayerPermissions(PlayerPermissionLevel playerPermissions) {
        this.playerPermissions = playerPermissions;
    }

    public GamePublishSetting getGamePublishSetting() {
        return gamePublishSetting;
    }

    public void setGamePublishSetting(GamePublishSetting gamePublishSetting) {
        this.gamePublishSetting = gamePublishSetting;
    }

    public int getServerChunkTickRange() {
        return serverChunkTickRange;
    }

    public void setServerChunkTickRange(int serverChunkTickRange) {
        this.serverChunkTickRange = serverChunkTickRange;
    }

    public boolean isCanPlatformBroadcast() {
        return canPlatformBroadcast;
    }

    public void setCanPlatformBroadcast(boolean canPlatformBroadcast) {
        this.canPlatformBroadcast = canPlatformBroadcast;
    }

    public GamePublishSetting getBroadcastMode() {
        return broadcastMode;
    }

    public void setBroadcastMode(GamePublishSetting broadcastMode) {
        this.broadcastMode = broadcastMode;
    }

    public boolean isXblBroadcastIntent() {
        return xblBroadcastIntent;
    }

    public void setXblBroadcastIntent(boolean xblBroadcastIntent) {
        this.xblBroadcastIntent = xblBroadcastIntent;
    }

    public boolean isHasLockBehaviroPack() {
        return hasLockBehaviroPack;
    }

    public void setHasLockBehaviroPack(boolean hasLockBehaviroPack) {
        this.hasLockBehaviroPack = hasLockBehaviroPack;
    }

    public boolean isHasLockResourcePack() {
        return hasLockResourcePack;
    }

    public void setHasLockResourcePack(boolean hasLockResourcePack) {
        this.hasLockResourcePack = hasLockResourcePack;
    }

    public boolean isFromLockedTemplate() {
        return isFromLockedTemplate;
    }

    public void setFromLockedTemplate(boolean fromLockedTemplate) {
        isFromLockedTemplate = fromLockedTemplate;
    }

    public boolean isUseMsaGametagsOnly() {
        return useMsaGametagsOnly;
    }

    public void setUseMsaGametagsOnly(boolean useMsaGametagsOnly) {
        this.useMsaGametagsOnly = useMsaGametagsOnly;
    }

    public boolean isWorldFromTemplate() {
        return isWorldFromTemplate;
    }

    public void setWorldFromTemplate(boolean worldFromTemplate) {
        isWorldFromTemplate = worldFromTemplate;
    }

    public boolean isWorldTemplateOptionLocked() {
        return isWorldTemplateOptionLocked;
    }

    public void setWorldTemplateOptionLocked(boolean worldTemplateOptionLocked) {
        isWorldTemplateOptionLocked = worldTemplateOptionLocked;
    }

    public boolean isForceSpawn() {
        return forceSpawn;
    }

    public void setForceSpawn(boolean forceSpawn) {
        this.forceSpawn = forceSpawn;
    }

    public boolean isTickMobEntity() {
        return tickMobEntity;
    }

    public void setTickMobEntity(boolean tickMobEntity) {
        this.tickMobEntity = tickMobEntity;
    }

    public boolean isTickItemEntity() {
        return tickItemEntity;
    }

    public void setTickItemEntity(boolean tickItemEntity) {
        this.tickItemEntity = tickItemEntity;
    }

    public boolean isTickTileEntity() {
        return tickTileEntity;
    }

    public void setTickTileEntity(boolean tickTileEntity) {
        this.tickTileEntity = tickTileEntity;
    }

    public boolean isTickProjectileEntity() {
        return tickProjectileEntity;
    }

    public void setTickProjectileEntity(boolean tickProjectileEntity) {
        this.tickProjectileEntity = tickProjectileEntity;
    }

    public boolean isTickPlayers() {
        return tickPlayers;
    }

    public void setTickPlayers(boolean tickPlayers) {
        this.tickPlayers = tickPlayers;
    }

    public boolean isTickNPCs() {
        return tickNPCs;
    }

    public void setTickNPCs(boolean tickNPCs) {
        this.tickNPCs = tickNPCs;
    }

    public boolean isTickMonsters() {
        return tickMonsters;
    }

    public void setTickMonsters(boolean tickMonsters) {
        this.tickMonsters = tickMonsters;
    }

    public int getTickInterval() {
        return tickInterval;
    }

    public void setTickInterval(int tickInterval) {
        this.tickInterval = tickInterval;
    }

    public int getMiniPlayerNum() {
        return miniPlayerNum;
    }

    public void setMiniPlayerNum(int miniPlayerNum) {
        this.miniPlayerNum = miniPlayerNum;
    }

    public int getMaxPlayerNum() {
        return maxPlayerNum;
    }

    public void setMaxPlayerNum(int maxPlayerNum) {
        this.maxPlayerNum = maxPlayerNum;
    }

    public boolean isOnlySpawnV1Villagers() {
        return onlySpawnV1Villagers;
    }

    public void setOnlySpawnV1Villagers(boolean onlySpawnV1Villagers) {
        this.onlySpawnV1Villagers = onlySpawnV1Villagers;
    }

    /**
     * 设置Level的世界边界
     */
    public int getxMin() {
        return xMin;
    }

    public void setxMin(int xMin) {
        this.xMin = xMin;
    }

    public int getxMax() {
        return xMax;
    }

    public void setxMax(int xMax) {
        this.xMax = xMax;
    }

    public int getzMin() {
        return zMin;
    }

    public void setzMin(int zMin) {
        this.zMin = zMin;
    }

    public int getzMax() {
        return zMax;
    }

    public void setzMax(int zMax) {
        this.zMax = zMax;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isPlayerVisiable() {
        return isPlayerVisiable;
    }

    public void setPlayerVisiable(boolean playerVisiable) {
        isPlayerVisiable = playerVisiable;
    }

    public void setChunkLoadRadius(int chunkLoadRadius) {
        this.chunkLoadRadius = chunkLoadRadius;
    }

    public void setChunkUnloadRadius(int chunkUnloadRadius) {
        this.chunkUnloadRadius = chunkUnloadRadius;
    }

    public int getChunkLoadRadius() {
        return chunkLoadRadius;
    }

    public int getChunkUnloadRadius() {
        return chunkUnloadRadius;
    }

    public String getVanillaVersion() {
        return vanillaVersion;
    }

    public void setVanillaVersion(String vanillaVersion) {
        this.vanillaVersion = vanillaVersion;
    }

    public short getSpawnBiomeType() {
        return spawnBiomeType;
    }

    public void setSpawnBiomeType(short spawnBiomeType) {
        this.spawnBiomeType = spawnBiomeType;
    }

    public String getUserDefinedBiomeName() {
        return userDefinedBiomeName;
    }

    public void setUserDefinedBiomeName(String userDefinedBiomeName) {
        this.userDefinedBiomeName = userDefinedBiomeName;
    }
}
