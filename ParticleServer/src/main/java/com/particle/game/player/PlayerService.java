package com.particle.game.player;

import com.particle.api.entity.PlayerServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.executor.service.AsyncScheduleService;
import com.particle.executor.service.ExecutorFactory;
import com.particle.game.block.enchantment.EnchantmentService;
import com.particle.game.block.trigger.EntityEnterBlockTriggerService;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.attack.EntityAttackService;
import com.particle.game.entity.attack.EntityAttackedHandleService;
import com.particle.game.entity.attack.EntityRemoteAttackService;
import com.particle.game.entity.attribute.explevel.EntityExperienceModule;
import com.particle.game.entity.attribute.explevel.ExperienceService;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.attribute.identified.EntityNameModule;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.entity.attribute.identified.UUIDModule;
import com.particle.game.entity.attribute.metadata.EntityMetaDataModule;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.attribute.satisfaction.HungerService;
import com.particle.game.entity.link.EntityLinkServiceProxy;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.service.network.PlayerPacketBuilder;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.entity.spawn.SpawnModule;
import com.particle.game.entity.spawn.processor.PlayerSpawnProcessor;
import com.particle.game.entity.state.EntityStateService;
import com.particle.game.player.craft.CraftService;
import com.particle.game.player.craft.RecipeManager;
import com.particle.game.player.interactive.EntityInteractiveTTLModule;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.modules.MultiObservedContainerModule;
import com.particle.game.player.inventory.modules.MultiOwedContainerModule;
import com.particle.game.player.inventory.modules.PlayerUIInventoryModule;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.player.permission.PermissionService;
import com.particle.game.player.save.PlayerDataOperateState;
import com.particle.game.player.save.PlayerDataService;
import com.particle.game.player.service.EntityUsingItemModule;
import com.particle.game.player.state.EntityGameModeModule;
import com.particle.game.player.state.PlayerMovePacketModule;
import com.particle.game.player.uuid.PlayerUuidService;
import com.particle.game.scene.module.BroadcastModule;
import com.particle.game.server.Server;
import com.particle.game.utils.config.ServerConfigService;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.game.world.aoi.BroadcastService;
import com.particle.game.world.aoi.components.PlayerAutoChunkSubscriberModule;
import com.particle.game.world.level.ChunkService;
import com.particle.game.world.level.LevelService;
import com.particle.game.world.level.WorldService;
import com.particle.game.world.map.MapGenerateService;
import com.particle.game.world.map.MapService;
import com.particle.game.world.physical.EntityColliderService;
import com.particle.game.world.physical.PhysicalService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.EntityType;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.attribute.EntityAttributeType;
import com.particle.model.entity.component.player.OpenContainerStatusModule;
import com.particle.model.entity.component.player.PermissionModule;
import com.particle.model.entity.component.player.PlayerSkinModule;
import com.particle.model.entity.component.ui.FormLayoutModule;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.model.skin.PlayerSkinData;
import com.particle.model.events.level.player.*;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.LevelStatus;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.*;
import com.particle.model.player.*;
import com.particle.model.player.spawn.PlayerRespawnState;
import com.particle.network.NetworkManager;
import com.particle.network.handler.AbstractPacketHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Singleton
public class PlayerService implements PlayerServiceApi {

    private static Logger logger = LoggerFactory.getLogger(PlayerService.class);

    // ----- 核心组件 -----
    private static final ECSModuleHandler<UUIDModule> UUID_MODULE_HANDLER = ECSModuleHandler.buildHandler(UUIDModule.class);
    private static final ECSModuleHandler<EntityNameModule> ENTITY_NAME_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityNameModule.class);
    private static final ECSModuleHandler<EntityMetaDataModule> ENTITY_META_DATA_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityMetaDataModule.class);
    private static final ECSModuleHandler<EntityExperienceModule> ENTITY_EXPERIENCE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityExperienceModule.class);
    private static final ECSModuleHandler<EntityGameModeModule> ENTITY_GAME_MODE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityGameModeModule.class);
    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<PlayerSkinModule> PLAYER_SKIN_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlayerSkinModule.class);
    private static final ECSModuleHandler<PermissionModule> PERMISSION_MODULE_HANDLER = ECSModuleHandler.buildHandler(PermissionModule.class);
    private static final ECSModuleHandler<OpenContainerStatusModule> OPEN_CONTAINER_STATUS_MODULE_HANDLER = ECSModuleHandler.buildHandler(OpenContainerStatusModule.class);
    private static final ECSModuleHandler<PlayerAutoChunkSubscriberModule> PLAYER_AUTO_CHUNK_SUBSCRIBER_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlayerAutoChunkSubscriberModule.class);
    private static final ECSModuleHandler<BroadcastModule> BROADCAST_MODULE_HANDLER = ECSModuleHandler.buildHandler(BroadcastModule.class);
    private static final ECSModuleHandler<SpawnModule> SPAWN_MODULE_HANDLER = ECSModuleHandler.buildHandler(SpawnModule.class);
    private static final ECSModuleHandler<EntityUsingItemModule> ENTITY_USING_ITEM_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityUsingItemModule.class);
    private static final ECSModuleHandler<PlayerUIInventoryModule> PLAYER_UI_INVENTORY_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlayerUIInventoryModule.class);
    private static final ECSModuleHandler<EntityInteractiveTTLModule> ENTITY_INTERACTIVE_TTL_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityInteractiveTTLModule.class);


    // ----- 容器 -----
    private static final ECSModuleHandler<MultiObservedContainerModule> MULTI_OBSERVED_CONTAINER_MODULE_HANDLER = ECSModuleHandler.buildHandler(MultiObservedContainerModule.class);
    private static final ECSModuleHandler<MultiOwedContainerModule> MULTI_OWED_CONTAINER_MODULE_HANDLER = ECSModuleHandler.buildHandler(MultiOwedContainerModule.class);

    // ----- 功能组件 -----
    private static final ECSModuleHandler<FormLayoutModule> FORM_LAYOUT_MODULE_HANDLER = ECSModuleHandler.buildHandler(FormLayoutModule.class);

    // 移動包重複處理
    private static final ECSModuleHandler<PlayerMovePacketModule> PLAYER_MOVE_PACKET_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlayerMovePacketModule.class);


    // ----- 核心Service -----
    @Inject
    private Server server;
    @Inject
    private NetworkManager networkManager;
    @Inject
    private BroadcastService broadcastService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();
    @Inject
    private RecipeManager recipeManager;
    @Inject
    private PlayerDataService playerDataService;
    @Inject
    private ECSComponentManager ecsComponentManager;

    // ----- 世界相关service -----
    @Inject
    private WorldService worldService;
    @Inject
    private LevelService levelService;
    @Inject
    private ChunkService chunkService;

    // ----- 玩家业务相关service -----
    @Inject
    private EntityLinkServiceProxy entityLinkServiceProxy;
    @Inject
    private InventoryManager inventoryManager;
    @Inject
    private PlayerInventoryAPI playerInventoryAPI;
    @Inject
    private MetaDataService metaDataService;
    @Inject
    private PositionService positionService;
    @Inject
    private HealthServiceProxy healthServiceProxy;
    @Inject
    private MovementServiceProxy movementServiceProxy;
    @Inject
    private ExperienceService experienceService;
    @Inject
    private PermissionService permissionService;
    @Inject
    private EntitySpawnService entitySpawnService;
    @Inject
    private PlayerUuidService playerUuidService;
    @Inject
    private EntityNameService entityNameService;
    @Inject
    private EntityStateService entityStateService;
    @Inject
    private PhysicalService physicalService;
    @Inject
    private EntityColliderService entityColliderService;
    @Inject
    private CraftService craftService;
    @Inject
    private MapService mapService;
    @Inject
    private MapGenerateService mapGenerateService;
    @Inject
    private EnchantmentService enchantmentService;
    @Inject
    private PlayerSkinService playerSkinService;

    // ------- 玩家相关的处理器 ----------------------
    @Inject
    private PlayerSpawnProcessor playerSpawnProcessor;
    @Inject
    private PlayerPacketBuilder playerPacketBuilder;
    @Inject
    private EntityAttackService entityAttackService;
    @Inject
    private EntityRemoteAttackService entityRemoteAttackService;
    @Inject
    private EntityAttackedHandleService entityAttackedHandleService;
    @Inject
    private EntityEnterBlockTriggerService entityEnterBlockTriggerService;
    @Inject
    private PlayerSkinCheckServiceProvider playerSkinCheckServiceProvider;

    @Inject
    private HungerService hungerService;

    // 設定
    @Inject
    private ServerConfigService serverConfigService;

    /**
     * 创建玩家对象
     *
     * @param address
     * @return
     */
    public Player createPlayer(InetSocketAddress address) {
        Player createdPlayer = new Player(address);
        createdPlayer.init();
        return createdPlayer;
    }

    /**
     * 初始化玩家对象
     *
     * @param player
     * @return
     */
    public void initPlayer(Player player) {
        // 位置相关信息
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.bindModule(player);
        transformModule.setPosition(0, 80, 0);
        transformModule.setDirection(0, 0, 0);
        transformModule.setMoveEntityPacketBuilder(this.playerPacketBuilder.getMovePacketBuilder(player));

        // 设置Entity基础信息
        EntityMetaDataModule entityMetaDataModule = ENTITY_META_DATA_MODULE_HANDLER.bindModule(player);
        entityMetaDataModule.setLongData(EntityMetadataType.FLAGS, 0);
        entityMetaDataModule.setFloatData(EntityMetadataType.SCALE, 1f);
        entityMetaDataModule.setShortData(EntityMetadataType.AIR, (short) 400);
        entityMetaDataModule.setShortData(EntityMetadataType.MAX_AIR, (short) 400);
        entityMetaDataModule.setLongData(EntityMetadataType.LEAD_HOLDER_EID, -1);

        ENTITY_NAME_MODULE_HANDLER.bindModule(player);

        // 碰撞盒
        this.entityColliderService.bindAABBBindBox(player, new Vector3f(0, 0, 0), new Vector3f(0, 1, 0), new Vector3f(1, 1.9f, 1));
        this.entityColliderService.bindDefaultColliderDetector(player);
        this.physicalService.initPhysicalEffects(player, true, true, (float) (9.8 / 7));

        // 移动相关
        this.movementServiceProxy.enableMovement(player, 0.1f, 0.2f);

        // 状态相关
        this.entityStateService.initEntityStateComponent(player);
        this.entityEnterBlockTriggerService.initDetectComponent(player);

        // 地图相关
        this.mapService.initPlayerComponent(player);

        // 伤害相关业务
        this.entityAttackedHandleService.initEntityAttackedComponent(player);

        this.entityAttackService.initEntityAttackComponent(player, 1, 500);
        this.entityRemoteAttackService.initEntityRemoteAttackComponent(player, 10);

        // Spawn相关业务
        this.entitySpawnService.enableSpawn(
                player,
                this.playerSpawnProcessor.getEntitySpawnProcessor(player),
                this.playerPacketBuilder.getAddPacketBuilder(player),
                this.playerPacketBuilder.getRemovePacketBuilder(player)
        );

        // 合成相关
        this.craftService.init(player);
        this.enchantmentService.initComponents(player);

        // 基础属性相关
        this.healthServiceProxy.initHealthComponent(player, 20);
        this.hungerService.initHungerComponent(player);
        ENTITY_EXPERIENCE_MODULE_HANDLER.bindModule(player);
        ENTITY_GAME_MODE_MODULE_HANDLER.bindModule(player);

        PLAYER_SKIN_MODULE_HANDLER.bindModule(player);

        UUID_MODULE_HANDLER.bindModule(player);

        ENTITY_INTERACTIVE_TTL_MODULE_HANDLER.bindModule(player);

        // 设置权限信息
        PERMISSION_MODULE_HANDLER.bindModule(player);

        // 背包相关
        MULTI_OBSERVED_CONTAINER_MODULE_HANDLER.bindModule(player);
        MULTI_OWED_CONTAINER_MODULE_HANDLER.bindModule(player);

        PLAYER_UI_INVENTORY_MODULE_HANDLER.bindModule(player);

        OPEN_CONTAINER_STATUS_MODULE_HANDLER.bindModule(player);

        ENTITY_USING_ITEM_MODULE_HANDLER.bindModule(player);

        // 表单
        FORM_LAYOUT_MODULE_HANDLER.bindModule(player);

        // 移動包重複處理
        PLAYER_MOVE_PACKET_MODULE_ECS_MODULE_HANDLER.bindModule(player);
    }

    /**
     * 初始化玩家数据.
     *
     * @param player the entity
     */
    public void initPlayerData(Player player) {
        logger.info("Player {} login success.", player.getIdentifiedStr());

        Level defaultLevel = this.worldService.getDefaultLevel();
        PlayerJoinGameBeforeEvent playerJoinGameBeforeEvent = new PlayerJoinGameBeforeEvent(player, defaultLevel);
        this.eventDispatcher.dispatchEvent(playerJoinGameBeforeEvent);

        //加载玩家存档
        AsyncScheduleService.getInstance().getThread().scheduleSimpleTask("PlayerDataLoaded", () -> {
            player.setPlayerState(PlayerState.SPAWNING);
            // 异步读取存档
            this.playerDataService.loadPlayerData(player, state -> {
                if (state == PlayerDataOperateState.EXCEPTION) {
                    this.networkManager.removePlayer(player.getClientAddress(), "Fail to load save data");
                    return;
                }

                // 切回主线重同步数据
                defaultLevel.getLevelSchedule().scheduleSimpleTask("PlayerDataInit", () -> {
                    if (player.getPlayerState() == PlayerState.DISCONNECTED) {
                        logger.info("Player {} disconnect before data loaded!", player.getIdentifiedStr());
                        return;
                    }

                    logger.info("Player {} data loaded!", player.getIdentifiedStr());

                    // 如果血量为0，则重置（上一次死亡后直接退出）
                    if (this.healthServiceProxy.getHealth(player) <= 0) {
                        this.healthServiceProxy.resetHealth(player);
                        this.hungerService.resetFoodLevel(player);
                    }

                    // 加载默认背包
                    this.inventoryManager.onPlayerLoadDefaultInventory(player);

                    // 如果没有存档，则初始化背包
                    if (state == PlayerDataOperateState.NOT_EXIST) {
                        Inventory playerInventory = this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
                        if (playerInventory.getAllSlots().isEmpty()) {
                            this.playerInventoryAPI.setItem(playerInventory, 0, ItemStack.getItem(ItemPrototype.LEATHER_HELMET), false);
                            this.playerInventoryAPI.setItem(playerInventory, 1, ItemStack.getItem(ItemPrototype.WOODEN_AXE), false);
                            this.playerInventoryAPI.setItem(playerInventory, 2, ItemStack.getItem(ItemPrototype.APPLE, 32), false);
                            this.playerInventoryAPI.setItem(playerInventory, 6, this.mapGenerateService.generateSingleContentMap(true, "新生存家园地图"), false);
                        }
                    }

                    //发送玩家加入游戏的事件
                    PlayerJoinGameEvent playerJoinGameEvent = new PlayerJoinGameEvent(player, defaultLevel);
                    playerJoinGameEvent.overrideAfterEventExecuted(() -> {
                        if (playerJoinGameEvent.getChangedLevel() == null) {
                            this.startPlayerGame(player, playerJoinGameEvent.getLevel());
                        } else {
                            this.startPlayerGame(player, playerJoinGameEvent.getChangedLevel());
                        }

                    });

                    playerJoinGameEvent.overrideOnEventCancelled(() -> {
                        String reason = playerJoinGameEvent.getCancelReason() == null ? "Start Game Cancelled" : playerJoinGameEvent.getCancelReason();
                        this.server.close(player, reason);
                    });
                    this.eventDispatcher.dispatchEvent(playerJoinGameEvent);
                });

                // 同步UUID
                this.playerUuidService.playerNameCheck(this.entityNameService.getEntityName(player), this.getPlayerUUID(player).toString(), this.getPlayerRoleId(player));
            });
        });
    }

    /**
     * 启动游戏
     *
     * @param player
     * @param defaultLevel
     */
    private void startPlayerGame(Player player, Level defaultLevel) {
        //发送开始游戏包

        // TODO: 2020/1/9 优化发包顺序
        /*
            startgamepacket
            UpdateBlockPropertiesPacket
            SetSpawnPositionPacket
            SetTimePacket
            SetDifficultyPacket
            SetCommandsEnabledPacket
            AdventureSettingsPacket
            gameRulesPacket
            PlayerListPacket
            BiomeDefinitionListPacket
            AvailableActorIdentifiersPacket
         */

        // Step 1 : StartGamePacket
        StartGamePacket startGamePacket = new StartGamePacket();
        startGamePacket.setEntityUniqueId(player.getRuntimeId());
        startGamePacket.setEntityRuntimeId(player.getRuntimeId());
        startGamePacket.setPlayerGamemode(this.getGameMode(player).getMode());
        startGamePacket.setInventoryServerAuthoritative(serverConfigService.isInventoryServerAuthoritative());
        startGamePacket.setMovementServerAuthoritative(false);

        // 若是 1.16, 并且是中国版本
        if (player.getProtocolVersion() >= AbstractPacketHandler.VERSION_1_16 && AbstractPacketHandler.isChsVersion) {
            startGamePacket.setMovementServerAuthoritative(serverConfigService.isMovementServerAuthoritative());
        }

        if (defaultLevel.getLevelSettings().isForceSpawn()) {
            this.positionService.setPosition(player, defaultLevel.getLevelSettings().getSpawn());
        }


        Vector3f position = this.positionService.getPosition(player);
        Direction direction = this.positionService.getDirection(player);
        startGamePacket.setX(position.getX());
        startGamePacket.setY(position.getY() + 1.62f);
        startGamePacket.setZ(position.getZ());
        startGamePacket.setYaw(direction.getYaw());
        startGamePacket.setPitch(direction.getPitch());

        startGamePacket.setSettings(defaultLevel.getLevelSettings());

        startGamePacket.setLevelId("");
        startGamePacket.setWorldName("World");
        startGamePacket.setCurrentLevelTime(1);
        startGamePacket.setEnchantmentSeed(1);

        this.networkManager.sendMessage(player.getClientAddress(), startGamePacket);

        // Step 2 : SetSpawnPositionPacket

        // Step 3 : SetTimePacket
        SetTimePacket setTimePacket = new SetTimePacket();
        setTimePacket.setTime((int) defaultLevel.getTime());
        this.networkManager.sendMessage(player.getClientAddress(), setTimePacket);

        // Step 4 : SetDifficultyPacket

        // Step 5 : SetCommandEnabledPacket
        SetCommandEnabledPacket setCommandEnabledPacket = new SetCommandEnabledPacket();
        setCommandEnabledPacket.setEnabled(true);
        this.networkManager.sendMessage(player.getClientAddress(), setCommandEnabledPacket);

        // Step 6 : AdventureSettingsPacket

        // Step 7 : GameRulesChangedPacket


        // Step 8 : 发送生物群系
        this.networkManager.sendMessage(player.getClientAddress(), new BiomeDefinitionListCompiledPacket());

        // Step 9 : AvailableEntityIdentifiersPacket
        // 同步entity的信息
        if (player.getProtocolVersion() >= AbstractPacketHandler.VERSION_1_8) {
            this.networkManager.sendMessage(player.getClientAddress(), new AvailableEntityIdentifiersPacket());
        }

        // 发送背包
        this.inventoryManager.openEntityMultiInventory(player);
        this.recipeManager.sendRecipes(player);
        // 加载玩家权限
        this.permissionService.reload(player);

        // 刷新玩家属性
        EntityExperienceModule entityExperienceModule = ENTITY_EXPERIENCE_MODULE_HANDLER.getModule(player);
        entityExperienceModule.refreshEntityExperienceAttribute();
        entityExperienceModule.refreshEntityExperienceLevelAttribute();

        // 同步玩家属性
        this.updatePlayerAttribute(player);

        // 同步创造模式背包
        GameMode gameMode = this.getGameMode(player);
        this.inventoryManager.notifyPlayerWithCreativeContents(player);
        if (gameMode == GameMode.SURVIVE) {
            this.inventoryManager.notifyPlayerAllInventoryChanged(player);
        }

        // 同步玩家状态
        this.metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_GRAVITY, true, false);
        this.metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_BREATHING, true, false);
        this.metaDataService.setShortData(player, EntityMetadataType.AIR, (short) 400);
        this.metaDataService.setShortData(player, EntityMetadataType.MAX_AIR, (short) 400);
        this.metaDataService.setStringData(player, EntityMetadataType.INTERACTIVE_TAG, "");
        this.metaDataService.setStringData(player, EntityMetadataType.NAMETAG, this.entityNameService.getEntityName(player));
        this.metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_CAN_SHOW_NAMETAG, true, false);
        this.metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_ALWAYS_SHOW_NAMETAG, true, false);
        this.metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_CAN_CLIMB, true, false);
        this.metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_ONFIRE, false, false);
        // 若是創世觀察者則隱身
        this.metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_INVISIBLE, gameMode == GameMode.CREATIVE_VIEWER, false);
        this.metaDataService.refreshPlayerMetadata(player);

        //开启玩家tick
        this.ecsComponentManager.filterTickedSystem(player);

        //同步ChunkRadius
        ChunkRadiusUpdatePacket chunkRadiusUpdatePacket = new ChunkRadiusUpdatePacket();
        chunkRadiusUpdatePacket.setRadius(8);
        this.networkManager.sendMessage(player.getClientAddress(), chunkRadiusUpdatePacket);

        // 视距
        if (player.getProtocolVersion() >= AbstractPacketHandler.VERSION_1_8) {
            PLAYER_AUTO_CHUNK_SUBSCRIBER_MODULE_HANDLER.bindModule(player);
        }

        // 如果是玩家，将自己的皮肤同步一次
        this.playerSkinService.refreshSelfSkin(player);

        // 尝试校验皮肤（保证在PlayerList包广播后再发送Confirm）
        PlayerSkinData playerSkinData = PLAYER_SKIN_MODULE_HANDLER.getModule(player).getBaseSkinData();
        if (playerSkinData != null) {
            this.playerSkinCheckServiceProvider.get().checkSkin(playerSkinData, (result -> {
                player.getLevel().getLevelSchedule().scheduleSimpleTask("SkinCheckCallback", () -> {
                    if (result) {
                        ConfirmSkinPacket confirmSkinPacket = new ConfirmSkinPacket();
                        confirmSkinPacket.addPlayerListEntry(UUID_MODULE_HANDLER.getModule(player).getUuid());
                        BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.getModule(player);
                        // 有可能还没有spawn进世界，导致broadcast module还没有绑定
                        if (broadcastModule != null) {
                            BroadcastService.broadcast(player, broadcastModule, confirmSkinPacket, true);
                        } else {
                            networkManager.sendMessage(player.getClientAddress(), confirmSkinPacket);
                        }
                    }
                });
            }), ExecutorFactory.buildExecutor("SkinCheck", AsyncScheduleService.getInstance().getThread()));
        }

        this.confirmStartGame(player, defaultLevel, position);
    }

    /**
     * 确认是否可开始游戏
     *
     * @param player
     * @param level
     * @param position
     */
    private void confirmStartGame(Player player, Level level, Vector3f position) {
        level.getLevelSchedule().scheduleDelayTask("start_game_delay", () -> {
            if (player.getPlayerState() != PlayerState.DISCONNECTED) {
                //发送玩家开始游戏事件
                PlayerStartGameEvent playerStartGameEvent = new PlayerStartGameEvent(player, level, position);
                this.eventDispatcher.dispatchEvent(playerStartGameEvent);

                // 当事件被取消后，重发事件
                if (playerStartGameEvent.isCancelled()) {
                    this.confirmStartGame(player, level, position);
                } else {
                    this.spawnPlayer(level, player, playerStartGameEvent.getPosition());
                }
            }
        }, 50);
    }

    /**
     * 将玩家spawn到世界中
     * 处理内容包括：
     * 1) 注册区块监听
     * 2) 发送区块数据
     * 3) 区块内玩家之间相互spawn
     *
     * @param player 待spawn的Player
     */
    public void spawnPlayer(Level level, Player player, Vector3f position) {
        logger.info("Player {} spawn into {}, position:{}, {}, {}!", player.getIdentifiedStr(), level.getLevelName(),
                position.getX(), position.getY(), position.getZ());

        // 检查是否有重复玩家
        Player duplicatedPlayer = level.addPlayer(player);

        // 如果有重复玩家，则移除之前的玩家
        // TODO: 注意移除过程中是否会出现误判，清除当前玩家的信息
        if (duplicatedPlayer != null) {
            this.server.close(duplicatedPlayer, "duplicated spawn");
            // TODO: 2019/1/6 因为不确定close操作是否会误清数据，因此两个玩家都弹出游戏（保证僵尸玩家一定被立刻弹出游戏）
            this.server.close(player, "duplicated spawn");

            return;
        }

        // 针对刚刚登陆游戏的玩家，切换Spawn状态
        if (player.getPlayerState() == PlayerState.SPAWNING) {
            // 切换玩家Spawn状态
            PlayerStatusPacket playerStatusPacket = new PlayerStatusPacket();
            playerStatusPacket.setStatus(PlayerStatusPacket.PLAYER_SPAWN);
            this.networkManager.sendMessage(player.getClientAddress(), playerStatusPacket);
        }
        player.setPlayerState(PlayerState.SPAWNED);

        // 发送玩家传入世界的事件
        PlayerPostSpawnLevelEvent playerPostSpawnLevelEvent = new PlayerPostSpawnLevelEvent(player, level);
        this.eventDispatcher.dispatchEvent(playerPostSpawnLevelEvent);

        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);

        // 更新玩家spawn点
        this.updatePlayerSpawnPoint(player, position);

        // 更新服务端内的玩家坐标
        this.positionService.setPosition(player, position);

        // 执行剩下spawnn操作
        this.entitySpawnService.spawn(level, player);


    }

    /**
     * 将玩家从世界中移除
     * <p>
     * 为了保证respawn事件被正确处理，这里不清除玩家spawn的level
     *
     * @param level
     * @param player
     */
    public boolean deSpawnPlayer(Level level, Player player) {
        // 从Level中移除玩家
        Player removePlayer = level.removePlayer(player);

        // 暂时不移除玩家本身缓存的level，despawn状态下的event走原level发送
        // TODO: 2019/1/6 以后despawn状态下的event走默认level发送

        // 清除骑乘关系
        this.entityLinkServiceProxy.unRideEntity(player);

        // despawn数据处理
        player.setPlayerState(PlayerState.DESPAWNED);
        this.entitySpawnService.despawn(player);

        return true;
    }

    /**
     * 将玩家复活进入世界
     *
     * @param player
     */
    public void respawnPlayer(Player player) {
        PlayerRespawnEvent playerRespawnEvent = new PlayerRespawnEvent(player);

        playerRespawnEvent.overrideAfterEventExecuted(() -> {
            Vector3f spawnPosition = playerRespawnEvent.getSpawnPosition();
            if (spawnPosition == null) {
                spawnPosition = player.getSpawnPosition();
            }
            // spawn进世界
            this.positionService.setPosition(player, spawnPosition);
            // 更新玩家spawn点
            this.updatePlayerSpawnPoint(player, spawnPosition);

            this.healthServiceProxy.resetHealth(player);

            // 复活惩罚扣血量
            float maxHealth = this.healthServiceProxy.getMaxHealth(player);
            this.healthServiceProxy.setHealth(player, maxHealth - playerRespawnEvent.getRespawnLossHealth());

            // 复活扣经验
            int curExperience = experienceService.getEntityExperience(player);
            int newExperience = Math.max(1, curExperience - playerRespawnEvent.getRespawnLossExperience());
            experienceService.setEntityExperience(player, newExperience);

            this.hungerService.resetFoodLevel(player);

            // TODO: 2019/6/19 做一个重置DataFlag的功能
            this.metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_INVISIBLE, false, true);
            this.metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_ONFIRE, false, true);
            this.metaDataService.refreshPlayerMetadata(player);

            this.entityStateService.disableAllState(player);
            this.updatePlayerAttribute(player);

            // 设置玩家可见
            SpawnModule spawnModule = SPAWN_MODULE_HANDLER.getModule(player);
            BroadcastModule broadcastModule = BROADCAST_MODULE_HANDLER.bindModule(player);
            broadcastModule.initGameObject(player.getLevel().getScene(),
                    (address) -> this.networkManager.sendMessage(address, spawnModule.getAddEntityPacketBuilder().buildPacket()),
                    (address) -> this.networkManager.sendMessage(address, spawnModule.getRemoveEntityPacketBuilder().buildPacket()));
            broadcastModule.setAddress(player.getClientAddress());
        });
        this.eventDispatcher.dispatchEvent(playerRespawnEvent);


    }

    /**
     * 获取生物的属性
     *
     * @return
     */
    @Override
    public EntityAttribute[] getAttributes(Player player) {
        List<EntityAttribute> entityAttributes = new ArrayList<>();
        EntityAttribute entityAttribute = this.movementServiceProxy.getMovementAttribute(player);
        if (entityAttribute != null) {
            entityAttributes.add(entityAttribute);
        }
        entityAttribute = this.healthServiceProxy.getEntityHealthAttribute(player);
        if (entityAttribute != null) {
            entityAttributes.add(entityAttribute);
        }
        entityAttribute = this.healthServiceProxy.getEntityAbsorptionAttribute(player);
        if (entityAttribute != null) {
            entityAttributes.add(entityAttribute);
        }
        entityAttribute = this.experienceService.getEntityExpAttribute(player);
        if (entityAttribute != null) {
            entityAttributes.add(entityAttribute);
        }
        entityAttribute = this.experienceService.getEntityExpLevelAttribute(player);
        if (entityAttribute != null) {
            entityAttributes.add(entityAttribute);
        }
        EntityAttribute[] attrs = this.hungerService.getHungerAttributes(player);
        if (attrs != null) {
            for (EntityAttribute attr : attrs) {
                entityAttributes.add(attr);
            }
        }
        return entityAttributes.toArray(new EntityAttribute[0]);
    }

    /**
     * 切换玩家模式
     *
     * @param player
     * @param mode
     * @param clientSide
     */
    @Override
    public void changePlayerGameMode(Player player, GameMode mode, boolean clientSide) {
        EntityGameModeModule gameModeModule = ENTITY_GAME_MODE_MODULE_HANDLER.getModule(player);

        GameMode oldGameMode = gameModeModule.getGameMode();
        if (mode == oldGameMode) {
            return;
        }

        gameModeModule.setGameMode(mode);
        if (mode == GameMode.CREATIVE) {
            this.inventoryManager.notifyPlayerWithCreativeContents(player);
        } else if (mode == GameMode.SURVIVE) {
            inventoryManager.notifyPlayerAllInventoryChanged(player);
        }
        // TODO 设置AdventureSettings

        // 服务端主动发送游戏模式切换的时候，主动主动关闭所有的背包
        if (!clientSide) {
            ContainerClosePacket containerClosePacket = new ContainerClosePacket();
            containerClosePacket.setContainerId(InventoryConstants.CONTAINER_ID_NONE);
            networkManager.sendMessage(player.getClientAddress(), containerClosePacket);
        }

        // 发送SetPlayerGameType操作
        if (!clientSide) {
            SetPlayerGameTypePacket setPlayerGameTypePacket = new SetPlayerGameTypePacket();
            setPlayerGameTypePacket.setGameMode(mode.getMode());
            networkManager.sendMessage(player.getClientAddress(), setPlayerGameTypePacket);
        }
    }

    @Override
    public GameMode getGameMode(Player player) {
        return ENTITY_GAME_MODE_MODULE_HANDLER.getModule(player).getGameMode();
    }

    /**
     * tp操作
     *
     * @param player
     * @param position
     */
    @Override
    public void teleport(Player player, Vector3f position) {
        this.positionService.setPosition(player, position);

        List<Entity> riderEntities = this.entityLinkServiceProxy.getRiderEntities(player);
        if (riderEntities != null) {
            for (Entity riderEntity : riderEntities) {
                this.positionService.setPosition(riderEntity, position);
            }
        }

        this.entityLinkServiceProxy.unRideEntity(player);

        Direction direction = this.positionService.getDirection(player);
        if (direction == null) {
            direction = new Direction(0, 0, 0);
        }
        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
        movePlayerPacket.setEntityId(player.getRuntimeId());
        movePlayerPacket.setVector3f(position.add(0, 1.62f, 0));
        movePlayerPacket.setDirection(direction);
        movePlayerPacket.setMode(PositionMode.TELEPORT);
        movePlayerPacket.setTeleportationCause(TeleportationCause.COMMAND);
        movePlayerPacket.setSourceEntityType(EntityType.UNDEFINED);
        movePlayerPacket.setOnGround(false);
        movePlayerPacket.setRidingEntityId(0);
        this.networkManager.sendMessage(player.getClientAddress(), movePlayerPacket);
    }

    @Override
    public void teleport(Player player, Vector3f position, Direction direction) {
        this.positionService.setPosition(player, position);
        this.positionService.setDirection(player, direction);

        this.entityLinkServiceProxy.unRideEntity(player);

        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
        movePlayerPacket.setEntityId(player.getRuntimeId());
        movePlayerPacket.setVector3f(position.add(0, 1.62f, 0));
        movePlayerPacket.setDirection(direction);
        movePlayerPacket.setMode(PositionMode.TELEPORT);
        movePlayerPacket.setTeleportationCause(TeleportationCause.COMMAND);
        movePlayerPacket.setSourceEntityType(EntityType.UNDEFINED);
        movePlayerPacket.setOnGround(false);
        movePlayerPacket.setRidingEntityId(0);
        this.networkManager.sendMessage(player.getClientAddress(), movePlayerPacket);
    }

    /**
     * 切换世界
     *
     * @param player
     * @param newLevel
     */
    @Override
    public void switchLevel(Player player, Level newLevel) {
        Vector3f spawnPoint = new Vector3f(newLevel.getLevelSettings().getSpawn());
        this.switchLevel(player, newLevel, spawnPoint);
    }

    /**
     * 切换世界
     *
     * @param player
     * @param newLevel
     * @param spawnPosition
     */
    @Override
    public void switchLevel(Player player, Level newLevel, Vector3f spawnPosition) {
        if (newLevel == null || newLevel.getLevelStatus() == LevelStatus.DESTROYING
                || newLevel.getLevelStatus() == LevelStatus.CLOSED) {
            return;
        }

        //发送玩家离开世界的event
        PlayerLeaveLevelEvent playerLeaveLevelEvent = new PlayerLeaveLevelEvent(player, player.getLevel(), PlayerLeaveLevelEvent.LeaveCause.SWITCH_LEVEL);
        this.eventDispatcher.dispatchEvent(playerLeaveLevelEvent);

        // 从旧世界despawn掉，这时候玩家已经不再接收旧世界的任何订阅消息
        if (player.getLevel() != null) {
            this.deSpawnPlayer(player.getLevel(), player);
        }

        // 转到新世界
        this.spawnPlayer(newLevel, player, spawnPosition);
    }

    /**
     * 切换世界
     *
     * @param player
     * @param newLevelName
     */
    @Override
    public void switchLevel(Player player, String newLevelName) {
        if (StringUtils.isEmpty(newLevelName)) {
            return;
        }
        Level newLevel = this.worldService.getLevel(newLevelName);
        if (newLevel != null) {
            this.switchLevel(player, newLevel);
        } else {
            logger.warn(String.format("Level : %s not exist.", newLevelName));
        }
    }

    /**
     * 将玩家重定向到指定地址
     *
     * @param player
     * @param address
     * @param port
     */
    @Override
    public void transfer(Player player, String address, short port) {
        TransferPacket transferPacket = new TransferPacket();
        transferPacket.setServerAddress(address);
        transferPacket.setPort(port);

        this.networkManager.sendMessage(player.getClientAddress(), transferPacket);
    }

    /**
     * 获取玩家UUID
     *
     * @param player
     * @return
     */
    @Override
    public UUID getPlayerUUID(Player player) {
        return this.UUID_MODULE_HANDLER.getModule(player).getUuid();
    }

    @Override
    public void setPlayerUUID(Player player, UUID targetUuid) {
        // 剛連線階段才可更改
        logger.warn("player uuid will be change!");
        if (player.getPlayerState() == PlayerState.CONNECTING) {
            logger.warn(String.format("player uuid : %s, player name : %s, will change uuid to : %s.", getPlayerUUID(player), entityNameService.getEntityName(player), targetUuid));
            this.UUID_MODULE_HANDLER.getModule(player).setUuid(targetUuid);
        }
    }

    /**
     * 获取玩家 RoleId
     *
     * @param player
     * @return
     */
    @Override
    public long getPlayerRoleId(Player player) {
        return this.UUID_MODULE_HANDLER.getModule(player).getRoleId();
    }

    @Override
    public void setPlayerRoleId(Player player, long roleId) {
        // 剛連線階段才可更改
        logger.warn("player roleId will be change!");
        if (player.getPlayerState() == PlayerState.CONNECTING) {
            logger.warn(String.format("player roleId : %s, player name : %s, will change roleId to : %s.", getPlayerRoleId(player), entityNameService.getEntityName(player), roleId));
            this.UUID_MODULE_HANDLER.getModule(player).setRoleId(roleId);
        }
    }

    public void updatePlayerSpawnPoint(Player player, Vector3f position) {
        player.setSpawnPosition(position);
        RespawnPacket respawnPacket = new RespawnPacket();
        respawnPacket.setX(position.getX());
        respawnPacket.setY(position.getY() + 1.62f);
        respawnPacket.setZ(position.getZ());
        respawnPacket.setPlayerRuntimeId(player.getRuntimeId());
        respawnPacket.setState(PlayerRespawnState.READY_TO_SPAWN);

        this.networkManager.sendMessage(player.getClientAddress(), respawnPacket);
    }

    public void updatePlayerAttribute(Player player) {
        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.setEntityId(player.getRuntimeId());
        updateAttributesPacket.setAttributes(this.getAttributes(player));
        this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
    }

    public void updatePlayerAttribute(Player player, EntityAttributeType attributeType) {
        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();

        switch (attributeType) {
            case HUNGER:
            case SATURATION:
                updateAttributesPacket.setEntityId(player.getRuntimeId());
                updateAttributesPacket.setAttributes(this.hungerService.getHungerAttributes(player));
                this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
                return;
            case HEALTH:
                updateAttributesPacket.setEntityId(player.getRuntimeId());
                updateAttributesPacket.setAttributes(new EntityAttribute[]{
                        this.healthServiceProxy.getEntityHealthAttribute(player), this.healthServiceProxy.getEntityAbsorptionAttribute(player)});
                this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
                return;
        }

    }

    /**
     * 获取某个坐标最近的玩家
     *
     * @param level    检查Level
     * @param position 检查坐标
     * @param limit    最远距离
     * @return
     */
    @Override
    public Player getClosestPlayer(Level level, Vector3f position, float limit) {
        int xMin = (int) (Math.floor((position.getX() - limit) / 16));
        int xMax = (int) (Math.floor((position.getX() + limit) / 16));
        int zMin = (int) (Math.floor((position.getZ() - limit) / 16));
        int zMax = (int) (Math.floor((position.getZ() + limit) / 16));

        List<Chunk> chunks = new LinkedList<>();
        for (int i = xMin; i <= xMax; i++) {
            for (int j = zMin; j <= zMax; j++) {
                Chunk chunk = this.chunkService.getChunk(level, i, j, false);
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }

        limit = limit * limit;

        Player closestPlayer = null;
        float closestDistance = Float.MAX_VALUE;

        for (Chunk chunk : chunks) {
            for (Player player : chunk.getPlayersCollection().getEntitiesViewer()) {
                float distance = this.positionService.getPosition(player).subtract(position).lengthSquared();
                if (distance < limit && distance < closestDistance) {
                    closestDistance = distance;
                    closestPlayer = player;
                }
            }
        }

        return closestPlayer;
    }

    /**
     * 获取某个坐标最近的玩家
     *
     * @param level    检查Level
     * @param gameMode 玩家游戏模式
     * @param position 检查坐标
     * @param limit    最远距离
     * @return
     */
    public Player getClosestPlayer(Level level, GameMode gameMode, Vector3f position, float limit) {
        int xMin = (int) (Math.floor((position.getX() - limit) / 16));
        int xMax = (int) (Math.floor((position.getX() + limit) / 16));
        int zMin = (int) (Math.floor((position.getZ() - limit) / 16));
        int zMax = (int) (Math.floor((position.getZ() + limit) / 16));

        List<Chunk> chunks = new LinkedList<>();
        for (int i = xMin; i <= xMax; i++) {
            for (int j = zMin; j <= zMax; j++) {
                Chunk chunk = this.chunkService.getChunk(level, i, j, false);
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }

        limit = limit * limit;

        Player closestPlayer = null;
        float closestDistance = Float.MAX_VALUE;

        for (Chunk chunk : chunks) {
            for (Player player : chunk.getPlayersCollection().getEntitiesViewer()) {
                if (this.getGameMode(player) == gameMode) {
                    float distance = this.positionService.getPosition(player).subtract(position).lengthSquared();
                    if (distance < limit && distance < closestDistance) {
                        closestDistance = distance;
                        closestPlayer = player;
                    }
                }
            }
        }

        return closestPlayer;
    }

    /**
     * 获取某个坐标最近的玩家
     *
     * @param level    检查Level
     * @param position 检查坐标
     * @param limit    最远距离
     * @return
     */
    public List<Player> getNearPlayers(Level level, Vector3f position, float limit) {
        int xMin = (int) (Math.floor((position.getX() - limit) / 16));
        int xMax = (int) (Math.floor((position.getX() + limit) / 16));
        int zMin = (int) (Math.floor((position.getZ() - limit) / 16));
        int zMax = (int) (Math.floor((position.getZ() + limit) / 16));

        List<Chunk> chunks = new LinkedList<>();
        for (int i = xMin; i <= xMax; i++) {
            for (int j = zMin; j <= zMax; j++) {
                Chunk chunk = this.chunkService.getChunk(level, i, j, false);
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }

        limit = limit * limit;

        List<Player> playerList = new ArrayList<>();

        for (Chunk chunk : chunks) {
            for (Player player : chunk.getPlayersCollection().getEntitiesViewer()) {
                if (this.positionService.getPosition(player).subtract(position).lengthSquared() < limit) {
                    playerList.add(player);
                }
            }
        }

        return playerList;
    }

    /**
     * 获取某个坐标最近的玩家
     *
     * @param level    检查Level
     * @param gameMode 玩家游戏模式
     * @param position 检查坐标
     * @param limit    最远距离
     * @return
     */
    public List<Player> getNearPlayers(Level level, GameMode gameMode, Vector3f position, float limit) {
        int xMin = (int) (Math.floor((position.getX() - limit) / 16));
        int xMax = (int) (Math.floor((position.getX() + limit) / 16));
        int zMin = (int) (Math.floor((position.getZ() - limit) / 16));
        int zMax = (int) (Math.floor((position.getZ() + limit) / 16));

        List<Chunk> chunks = new LinkedList<>();
        for (int i = xMin; i <= xMax; i++) {
            for (int j = zMin; j <= zMax; j++) {
                Chunk chunk = this.chunkService.getChunk(level, i, j, false);
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }

        limit = limit * limit;

        List<Player> playerList = new ArrayList<>();

        for (Chunk chunk : chunks) {
            for (Player player : chunk.getPlayersCollection().getEntitiesViewer()) {
                if (this.getGameMode(player) == gameMode
                        && this.positionService.getPosition(player).subtract(position).lengthSquared() < limit) {
                    playerList.add(player);
                }
            }
        }

        return playerList;
    }
}
