package com.particle.network;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.*;
import com.particle.network.handler.AbstractPacketHandler;
import com.particle.network.handler.v274.*;
import com.particle.network.handler.v282.AvailableCommandsPacketHandler282;
import com.particle.network.handler.v282.ResourcePacksInfoPacketHandler282;
import com.particle.network.handler.v282.SetScoreboardIdentityPacketHandler;
import com.particle.network.handler.v282.StartGamePacketHandler282;
import com.particle.network.handler.v291.AddPlayerPacketHandler291;
import com.particle.network.handler.v291.PlayerListPacketHandler291;
import com.particle.network.handler.v291.StartGamePacketHandler291;
import com.particle.network.handler.v291.TextPacketHandler291;
import com.particle.network.handler.v313.*;
import com.particle.network.handler.v332.*;
import com.particle.network.handler.v354.*;
import com.particle.network.handler.v361.*;
import com.particle.network.handler.v388.*;
import com.particle.network.handler.v389.AddPlayerPacketHandler389;
import com.particle.network.handler.v389.AvailableCommandsPacketHandler389;
import com.particle.network.handler.v389.FullChunkDataPacketHandler389;
import com.particle.network.handler.v389.TextPacketHandler389;
import com.particle.network.handler.v410.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PacketManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PacketManager.class);

    /**
     * 需要处理的package内容
     */
    private Class<? extends DataPacket>[] packetPool = new Class[512];

    /**
     * 处理消息包的handlers
     * <p>
     * packetHandlersMap路由指定玩家版本下处理器在ArrayList中的index
     * packetHandlers记录真正的处理器单例
     * <p>
     * 这样设计的好处是能快速匹配默认handler，不需要if判断，提高cpu的cache命中率
     * <p>
     * 版本号列表：
     * V1.5.1 - 261
     */
    private static final int MAX_VERSION = 512;
    private static final int MAX_ID = 300;
    private int[][] packetHandlersMap;
    private ArrayList<AbstractPacketHandler> packetHandlers;

    private PacketManager() {
        this.packetHandlersMap = new int[MAX_ID][MAX_VERSION];
        this.packetHandlers = new ArrayList<>();

        //初始化字典，默认值为0
        for (int i = 0; i < packetHandlersMap.length; i++) {
            for (int j = 0; j < packetHandlersMap[i].length; j++) {
                this.packetHandlersMap[i][j] = 0;
            }
        }

        this.packetHandlers.add(0, null);

        this.init();
    }

    public void init() {
        //开始注册数据
        // 登录包
        this.registerPacket(ProtocolInfo.LOGIN_PACKET, LoginPacket.class);
        this.registerHandler(ProtocolInfo.LOGIN_PACKET, new LoginPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        // 资源包相关
        this.registerPacket(ProtocolInfo.RESOURCE_PACK_CHUNK_REQUEST_PACKET, ResourcePackChunkRequestPacket.class);
        this.registerPacket(ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET, ResourcePackClientResponsePacket.class);

        this.registerHandler(ProtocolInfo.RESOURCE_PACKS_INFO_PACKET, new ResourcePacksInfoPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.RESOURCE_PACKS_INFO_PACKET, new ResourcePacksInfoPacketHandler282(), AbstractPacketHandler.VERSION_1_6);

        this.registerHandler(ProtocolInfo.RESOURCE_PACK_STACK_PACKET, new ResourcePackStackPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET, new ResourcePackClientResponsePacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.RESOURCE_PACK_CHUNK_DATA_PACKET, new ResourcePackChunkDataPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.RESOURCE_PACK_CHUNK_DATA_PACKET, new ResourcePackChunkDataPacketHandler388(), AbstractPacketHandler.VERSION_1_13);
        this.registerHandler(ProtocolInfo.RESOURCE_PACK_CHUNK_REQUEST_PACKET, new ResourcePackChunkRequestPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.RESOURCE_PACK_DATA_INFO_PACKET, new ResourcePackDataInfoPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.RESOURCE_PACK_DATA_INFO_PACKET, new ResourcePackDataInfoPacketHandlerV361(), AbstractPacketHandler.VERSION_1_12);
        this.registerHandler(ProtocolInfo.RESOURCE_PACK_DATA_INFO_PACKET, new ResourcePackDataInfoPacketHandlerV388(), AbstractPacketHandler.VERSION_1_13);

        //玩家相关
        this.registerPacket(ProtocolInfo.SET_ENTITY_DATA_PACKET, SetEntityDataPacket.class);
        this.registerPacket(ProtocolInfo.UPDATE_ATTRIBUTES_PACKET, UpdateAttributesPacket.class);
        this.registerPacket(ProtocolInfo.MOVE_PLAYER_PACKET, MovePlayerPacket.class);
        this.registerPacket(ProtocolInfo.PLAYER_ACTION_PACKET, PlayerActionPacket.class);
        this.registerPacket(ProtocolInfo.PLAYER_AUTH_INPUT_PACKET, PlayerAuthInputPacket.class);
        this.registerPacket(ProtocolInfo.ADD_PLAYER_PACKET, AddPlayerPacket.class);
        this.registerPacket(ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET, SetPlayerGameTypePacket.class);
        this.registerPacket(ProtocolInfo.RESPAWN_PACKET, RespawnPacket.class);
        this.registerPacket(ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET, ChunkRadiusUpdatePacket.class);
        this.registerPacket(ProtocolInfo.PLAYER_SKIN_PACKET, PlayerSkinPacket.class);
        this.registerPacket(ProtocolInfo.PLAYER_LIST_PACKET, PlayerListPacket.class);
        this.registerPacket(ProtocolInfo.CONFIRM_SKIN_PACKET, ConfirmSkinPacket.class);
        this.registerPacket(ProtocolInfo.BOSS_EVENT_PACKET, BossEventPacket.class);
        this.registerPacket(ProtocolInfo.PLAYER_INPUT_PACKET, PlayerInputPacket.class);
        this.registerPacket(ProtocolInfo.COMPLETED_USING_ITEM_PACKET, CompletedUsingItemPacket.class);

        this.registerHandler(ProtocolInfo.SET_ENTITY_DATA_PACKET, new SetEntityDataPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.SET_ENTITY_DATA_PACKET, new SetEntityDataPacketHandler354(), AbstractPacketHandler.VERSION_1_11);
        this.registerHandler(ProtocolInfo.SET_ENTITY_DATA_PACKET, new SetEntityDataPacketHandler361(), AbstractPacketHandler.VERSION_1_12);
        this.registerHandler(ProtocolInfo.SET_ENTITY_DATA_PACKET, new SetEntityDataPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.UPDATE_ATTRIBUTES_PACKET, new UpdateAttributesPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.UPDATE_ATTRIBUTES_PACKET, new UpdateAttributesPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.MOVE_PLAYER_PACKET, new MovePlayerPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.MOVE_PLAYER_PACKET, new MovePlayerPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.PLAYER_ACTION_PACKET, new PlayerActionPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.PLAYER_AUTH_INPUT_PACKET, new PlayerAuthInputPacketHandler410(), AbstractPacketHandler.VERSION_1_16);


        this.registerHandler(ProtocolInfo.ADD_PLAYER_PACKET, new AddPlayerPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.ADD_PLAYER_PACKET, new AddPlayerPacketHandler291(), AbstractPacketHandler.VERSION_1_7);
        this.registerHandler(ProtocolInfo.ADD_PLAYER_PACKET, new AddPlayerPacketHandler354(), AbstractPacketHandler.VERSION_1_11);
        this.registerHandler(ProtocolInfo.ADD_PLAYER_PACKET, new AddPlayerPacketHandler361(), AbstractPacketHandler.VERSION_1_12);
        this.registerHandler(ProtocolInfo.ADD_PLAYER_PACKET, new AddPlayerPacketHandler389(), AbstractPacketHandler.VERSION_1_14);
        this.registerHandler(ProtocolInfo.ADD_PLAYER_PACKET, new AddPlayerPacketHandler410(), AbstractPacketHandler.VERSION_1_16);

        this.registerHandler(ProtocolInfo.RESPAWN_PACKET, new RespawnPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.RESPAWN_PACKET, new RespawnPacketHandler388(), AbstractPacketHandler.VERSION_1_13);
        this.registerHandler(ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET, new ChunkRadiusUpdatePacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.PLAYER_SKIN_PACKET, new PlayerSkinPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        this.registerHandler(ProtocolInfo.PLAYER_LIST_PACKET, new PlayerListPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.PLAYER_LIST_PACKET, new PlayerListPacketHandler291(), AbstractPacketHandler.VERSION_1_7);
        this.registerHandler(ProtocolInfo.PLAYER_LIST_PACKET, new PlayerListPacketHandler388(), AbstractPacketHandler.VERSION_1_13);
        this.registerHandler(ProtocolInfo.PLAYER_LIST_PACKET, new PlayerListPacketHandler410(), AbstractPacketHandler.VERSION_1_16);

        this.registerHandler(ProtocolInfo.CONFIRM_SKIN_PACKET, new ConfirmSkinPacketHandler388(), AbstractPacketHandler.VERSION_1_13);

        this.registerHandler(ProtocolInfo.BOSS_EVENT_PACKET, new BossEventPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        this.registerHandler(ProtocolInfo.PLAYER_INPUT_PACKET, new PlayerInputPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        this.registerHandler(ProtocolInfo.PLAY_STATUS_PACKET, new PlayerStatusPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        this.registerHandler(ProtocolInfo.START_GAME_PACKET, new StartGamePacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.START_GAME_PACKET, new StartGamePacketHandler282(), AbstractPacketHandler.VERSION_1_6);
        this.registerHandler(ProtocolInfo.START_GAME_PACKET, new StartGamePacketHandler291(), AbstractPacketHandler.VERSION_1_7);
        this.registerHandler(ProtocolInfo.START_GAME_PACKET, new StartGamePacketHandler303(), AbstractPacketHandler.VERSION_1_8);
        this.registerHandler(ProtocolInfo.START_GAME_PACKET, new StartGamePacketHandler332(), AbstractPacketHandler.VERSION_1_9);
        this.registerHandler(ProtocolInfo.START_GAME_PACKET, new StartGamePacketHandler354(), AbstractPacketHandler.VERSION_1_11);
        this.registerHandler(ProtocolInfo.START_GAME_PACKET, new StartGamePacketHandler361(), AbstractPacketHandler.VERSION_1_12);
        this.registerHandler(ProtocolInfo.START_GAME_PACKET, new StartGamePacketHandler388(), AbstractPacketHandler.VERSION_1_13);
        this.registerHandler(ProtocolInfo.START_GAME_PACKET, new StartGamePacketHandler410(), AbstractPacketHandler.VERSION_1_16);

        this.registerHandler(ProtocolInfo.DISCONNECT_PACKET, new DisconnectPlayerPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET, new SetPlayerGameTypePacketHandler(), AbstractPacketHandler.VERSION_1_5);

        // 背包相关
        this.registerPacket(ProtocolInfo.CONTAINER_CLOSE_PACKET, ContainerClosePacket.class);
        this.registerPacket(ProtocolInfo.INVENTORY_TRANSACTION_PACKET, InventoryTransactionPacket.class);
        this.registerPacket(ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET, MobArmorEquipmentPacket.class);
        this.registerPacket(ProtocolInfo.MOB_EQUIPMENT_PACKET, MobEquipmentPacket.class);
        this.registerPacket(ProtocolInfo.PLAYER_HOTBAR_PACKET, PlayerHotbarPacket.class);

        this.registerHandler(ProtocolInfo.CONTAINER_CLOSE_PACKET, new ContainerClosePacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.CONTAINER_OPEN_PACKET, new ContainerOpenPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.INVENTORY_CONTENT_PACKET, new InventoryContentPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.INVENTORY_CONTENT_PACKET, new InventoryContentPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.INVENTORY_SLOT_PACKET, new InventorySlotPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.INVENTORY_SLOT_PACKET, new InventorySlotPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.INVENTORY_TRANSACTION_PACKET, new InventoryTransactionPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.INVENTORY_TRANSACTION_PACKET, new InventoryTransactionPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET, new MobArmorEquipmentPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET, new MobArmorEquipmentPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.MOB_EQUIPMENT_PACKET, new MobEquipmentPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.MOB_EQUIPMENT_PACKET, new MobEquipmentPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.PLAYER_HOTBAR_PACKET, new PlayerHotbarPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.CONTAINER_SET_DATA_PACKET, new ContainerSetDataPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        this.registerHandler(ProtocolInfo.COMPLETED_USING_ITEM_PACKET, new CompletedUsingItemPacketHandler388(), AbstractPacketHandler.VERSION_1_13);

        //世界相关
        this.registerPacket(ProtocolInfo.LEVEL_EVENT_PACKET, LevelEventPacket.class);
        this.registerPacket(ProtocolInfo.ADD_ENTITY_PACKET, AddEntityPacket.class);
        this.registerPacket(ProtocolInfo.REMOVE_ENTITY_PACKET, RemoveEntityPacket.class);
        this.registerPacket(ProtocolInfo.ADD_ITEM_ENTITY_PACKET, AddItemEntityPacket.class);
        this.registerPacket(ProtocolInfo.UPDATE_BLOCK_PACKET, UpdateBlockPacket.class);

        this.registerHandler(ProtocolInfo.LEVEL_EVENT_PACKET, new LevelEventPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.ADD_ENTITY_PACKET, new AddEntityPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.ADD_ENTITY_PACKET, new AddEntityPacketHandler354(), AbstractPacketHandler.VERSION_1_11);
        this.registerHandler(ProtocolInfo.ADD_ENTITY_PACKET, new AddEntityPacketHandler361(), AbstractPacketHandler.VERSION_1_12);
        this.registerHandler(ProtocolInfo.REMOVE_ENTITY_PACKET, new RemoveEntityPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.ADD_ITEM_ENTITY_PACKET, new AddItemEntityPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.ADD_ITEM_ENTITY_PACKET, new AddItemEntityPacketHandler354(), AbstractPacketHandler.VERSION_1_11);
        this.registerHandler(ProtocolInfo.ADD_ITEM_ENTITY_PACKET, new AddItemEntityPacketHandler361(), AbstractPacketHandler.VERSION_1_12);
        this.registerHandler(ProtocolInfo.ADD_ITEM_ENTITY_PACKET, new AddItemEntityPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.UPDATE_BLOCK_PACKET, new UpdateBlockPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        this.registerHandler(ProtocolInfo.SET_SPAWN_POSITION_PACKET, new SetSpawnPositionPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.CODE_BUILDER_PACKET, new CodeBuilderPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.CREATIVE_CONTENT_PACKET, new CreativeContentPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.DEBUG_INFO_PACKET, new DebugInfoPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.EMOTE_LIST_PACKET, new EmoteListPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.HURT_ARMOR_PACKET, new HurtArmorPacketHandler410(), AbstractPacketHandler.VERSION_1_16);

        this.registerHandler(ProtocolInfo.FULL_CHUNK_DATA_PACKET, new FullChunkDataPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.FULL_CHUNK_DATA_PACKET, new FullChunkDataPacketHandler332(), AbstractPacketHandler.VERSION_1_9);
        this.registerHandler(ProtocolInfo.FULL_CHUNK_DATA_PACKET, new FullChunkDataPacketHandler361(), AbstractPacketHandler.VERSION_1_12);
        this.registerHandler(ProtocolInfo.FULL_CHUNK_DATA_PACKET, new FullChunkDataPacketHandler389(), AbstractPacketHandler.VERSION_1_14);

        // 声音
        this.registerHandler(ProtocolInfo.LEVEL_SOUND_EVENT_V1_PACKET, new LevelSoundEventV1PacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.PLAY_SOUND_PACKET, new PlaySoundPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.STOP_SOUND_PACKET, new StopSoundPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        // 命令相关
        this.registerPacket(ProtocolInfo.COMMAND_REQUEST_PACKET, CommandRequestPacket.class);

        this.registerHandler(ProtocolInfo.AVAILABLE_COMMANDS_PACKET, new AvailableCommandsPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.AVAILABLE_COMMANDS_PACKET, new AvailableCommandsPacketHandler282(), AbstractPacketHandler.VERSION_1_6);
        this.registerHandler(ProtocolInfo.AVAILABLE_COMMANDS_PACKET, new AvailableCommandsPacketHandler389(), AbstractPacketHandler.VERSION_1_14);
        this.registerHandler(ProtocolInfo.COMMAND_REQUEST_PACKET, new CommandRequestPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.SET_COMMANDS_ENABLED_PACKET, new SetCommandEnabledPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        // 表单相关
        this.registerPacket(ProtocolInfo.MODAL_FORM_RESPONSE_PACKET, ModalFormResponsePacket.class);
        this.registerPacket(ProtocolInfo.SERVER_SETTINGS_REQUEST_PACKET, ServerSettingsRequestPacket.class);

        this.registerHandler(ProtocolInfo.MODAL_FORM_REQUEST_PACKET, new ModalFormRequestPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.MODAL_FORM_RESPONSE_PACKET, new ModalFormResponsePacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.SERVER_SETTINGS_REQUEST_PACKET, new ServerSettingsRequestPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.SERVER_SETTINGS_RESPONSE_PACKET, new ServerSettingsResponsePacketHandler(), AbstractPacketHandler.VERSION_1_5);

        // 配方
        this.registerPacket(ProtocolInfo.CRAFTING_EVENT_PACKET, CraftingEventPacket.class);

        this.registerHandler(ProtocolInfo.CRAFTING_DATA_PACKET, new CraftingDataPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.CRAFTING_DATA_PACKET, new CraftingDataPacketHandler354(), AbstractPacketHandler.VERSION_1_11);
        this.registerHandler(ProtocolInfo.CRAFTING_DATA_PACKET, new CraftingDataPacketHandler361(), AbstractPacketHandler.VERSION_1_12);
        this.registerHandler(ProtocolInfo.CRAFTING_DATA_PACKET, new CraftingDataPacketHandler388(), AbstractPacketHandler.VERSION_1_13);
        this.registerHandler(ProtocolInfo.CRAFTING_DATA_PACKET, new CraftingDataPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.CRAFTING_EVENT_PACKET, new CraftingEventPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.CRAFTING_EVENT_PACKET, new CraftingEventPacketHandler410(), AbstractPacketHandler.VERSION_1_16);

        // 文本
        this.registerPacket(ProtocolInfo.TEXT_PACKET, TextPacket.class);

        this.registerHandler(ProtocolInfo.TEXT_PACKET, new TextPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.TEXT_PACKET, new TextPacketHandler291(), AbstractPacketHandler.VERSION_1_7);
        this.registerHandler(ProtocolInfo.TEXT_PACKET, new TextPacketHandler389(), AbstractPacketHandler.VERSION_1_14);

        // 标题
        this.registerPacket(ProtocolInfo.SET_TITLE_PACKET, SetTitlePacket.class);
        this.registerHandler(ProtocolInfo.SET_TITLE_PACKET, new SetTitlePacketHandler(), AbstractPacketHandler.VERSION_1_5);

        this.registerPacket(ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET, ClientboundMapItemDataPacket.class);
        this.registerHandler(ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET, new ClientBoundMapItemDataPacketHandler332(), AbstractPacketHandler.VERSION_1_9);
        this.registerHandler(ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET, new ClientBoundMapItemDataPacketHandler354(), AbstractPacketHandler.VERSION_1_11);

        this.registerPacket(ProtocolInfo.MAP_INFO_REQUEST_PACKET, MapInfoRequestPacket.class);
        this.registerHandler(ProtocolInfo.MAP_INFO_REQUEST_PACKET, new MapInfoRequestPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        // entity
        this.registerPacket(ProtocolInfo.ENTITY_EVENT_PACKET, EntityEventPacket.class);
        this.registerPacket(ProtocolInfo.TAKE_ITEM_ENTITY_PACKET, TakeItemEntityPacket.class);
        this.registerPacket(ProtocolInfo.MOVE_ENTITY_PACKET, MoveEntityPacket.class);
        this.registerPacket(ProtocolInfo.SET_ENTITY_MOTION_PACKET, SetEntityMotionPacket.class);
        this.registerPacket(ProtocolInfo.BLOCK_ENTITY_DATA_PACKET, BlockEntityDataPacket.class);
        this.registerPacket(ProtocolInfo.MOB_EFFECT_PACKET, MobEffectPacket.class);
        this.registerPacket(ProtocolInfo.ANIMATE_PACKET, AnimationPacket.class);
        this.registerPacket(ProtocolInfo.SET_ENTITY_LINK_PACKET, SetEntityLinkPacket.class);

        this.registerHandler(ProtocolInfo.ENTITY_EVENT_PACKET, new EntityEventPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.TAKE_ITEM_ENTITY_PACKET, new TakeItemEntityPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.MOVE_ENTITY_PACKET, new MoveEntityPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.SET_ENTITY_MOTION_PACKET, new SetEntityMotionPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.BLOCK_ENTITY_DATA_PACKET, new BlockEntityDataPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.SET_ENTITY_LINK_PACKET, new SetEntityLinkPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        this.registerHandler(ProtocolInfo.MOB_EFFECT_PACKET, new MobEffectPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        this.registerHandler(ProtocolInfo.ANIMATE_PACKET, new AnimationPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        // 时间
        this.registerHandler(ProtocolInfo.SET_TIME_PACKET, new SetTimePacketHandler(), AbstractPacketHandler.VERSION_1_5);

        // block
        this.registerHandler(ProtocolInfo.BLOCK_EVENT_PACKET, new BlockEventPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        // 跳转
        this.registerHandler(ProtocolInfo.TRANSFER_PACKET, new TransferPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        // v1.8
        // 粒子效果
        this.registerHandler(ProtocolInfo.SPAWN_PARTICLE_EFFECT_PACKET, new SpawnParticleEffectPacketHandler(), AbstractPacketHandler.VERSION_1_8);
        this.registerHandler(ProtocolInfo.AVAILABLE_ACTOR_IDENTIFIERS_PACKET, new AvailableEntityIdentifiersPacketHandler(), AbstractPacketHandler.VERSION_1_8);
        this.registerHandler(ProtocolInfo.AVAILABLE_ACTOR_IDENTIFIERS_PACKET, new AvailableEntityIdentifiersPacketHandler410(), AbstractPacketHandler.VERSION_1_16);
        this.registerHandler(ProtocolInfo.LEVEL_SOUND_EVENT_V2_PACKET, new LevelSoundEventV2PacketHandler(), AbstractPacketHandler.VERSION_1_8);
        this.registerHandler(ProtocolInfo.NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET, new NetworkChunkPublisherUpdatePacketHandler(), AbstractPacketHandler.VERSION_1_8);
        this.registerHandler(ProtocolInfo.BIOME_DEFINITION_LIST_PACKET, new BiomeDefinitionListCompiledPacketHandler(), AbstractPacketHandler.VERSION_1_8);
        this.registerHandler(ProtocolInfo.ADD_ENTITY_PACKET, new AddEntityPacketHandler303(), AbstractPacketHandler.VERSION_1_8);
        this.registerHandler(ProtocolInfo.RESOURCE_PACK_STACK_PACKET, new ResourcePackStackPacketHandler303(), AbstractPacketHandler.VERSION_1_8);
        this.registerHandler(ProtocolInfo.RESOURCE_PACK_STACK_PACKET, new ResourcePackStackPacketHandler388(), AbstractPacketHandler.VERSION_1_13);


        // score
        this.registerHandler(ProtocolInfo.REMOVE_OBJECTIVE_PACKET, new RemoveObjectivePacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.SET_DISPLAY_OBJECTIVE_PACKET, new SetDisplayObjecivePacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.SET_SCORE_PACKET, new SetScorePacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerHandler(ProtocolInfo.SET_SCOREBOARD_IDENTITY_PACKET, new SetScoreboardIdentityPacketHandler(), AbstractPacketHandler.VERSION_1_6);

        // 1.9
        this.registerHandler(ProtocolInfo.SPAWN_PARTICLE_EFFECT_PACKET, new SpawnParticleEffectPacketHandler332(), AbstractPacketHandler.VERSION_1_9);
        this.registerHandler(ProtocolInfo.LEVEL_SOUND_EVENT_PACKET, new LevelSoundEventPacketHandler(), AbstractPacketHandler.VERSION_1_9);
        this.registerHandler(ProtocolInfo.RESOURCE_PACKS_INFO_PACKET, new ResourcePacksInfoPacketHandler332(), AbstractPacketHandler.VERSION_1_9);
        this.registerHandler(ProtocolInfo.UPDATE_BLOCK_PACKET, new UpdateBlockPacketHandler332(), AbstractPacketHandler.VERSION_1_9);

        // 交互
        this.registerPacket(ProtocolInfo.INTERACT_PACKET, InteractPacket.class);
        this.registerHandler(ProtocolInfo.INTERACT_PACKET, new InteractPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        // 付费
        this.registerPacket(ProtocolInfo.NETEASE_JSON_PACKET, NeteaseJsonPacket.class);
        this.registerHandler(ProtocolInfo.NETEASE_JSON_PACKET, new NeteaseJsonPacketHandler(), AbstractPacketHandler.VERSION_1_5);
        this.registerPacket(ProtocolInfo.NETEASE_BUY_SUCCESS_PACKET, NeteaseBuySuccessPacket.class);
        this.registerHandler(ProtocolInfo.NETEASE_BUY_SUCCESS_PACKET, new NeteaseBuySuccessPacketHandler(), AbstractPacketHandler.VERSION_1_5);

        // 1.16 前端回報
        this.registerPacket(ProtocolInfo.PACKET_VIOLATION_WARNING_PACKET, PacketViolationWarningPacket.class);
        this.registerHandler(ProtocolInfo.PACKET_VIOLATION_WARNING_PACKET, new PacketViolationWarningPacketHandler410(), AbstractPacketHandler.VERSION_1_16);

        // 该段代码必须放最后
        this.initFillFreeHandler();
    }

    /**
     * 注册具体某个id的packet
     *
     * @param id
     * @param clazz
     */
    private void registerPacket(int id, Class<? extends DataPacket> clazz) {
        this.packetPool[id] = clazz;
    }

    /**
     * 注册handler
     */
    private void registerHandler(int id, AbstractPacketHandler packetHandler, int version) {
        this.packetHandlers.add(packetHandler);

        int index = this.packetHandlers.size() - 1;
        this.packetHandlersMap[id][version] = index;

        if (id == ProtocolInfo.LOGIN_PACKET) {
            // loginPacket只能有一个版本的解析器，因为版本号尚未解析到
            if (this.packetHandlersMap[id][0] != 0) {
                throw new ProphetException(ErrorCode.CORE_EROOR, "只允许注册一个loginPacketHandler");
            } else {
                for (int v = 0; v < MAX_VERSION; v++) {
                    this.packetHandlersMap[id][v] = index;
                }
            }
        }
    }

    /**
     * 根据使用最近版本的原则，填充版本的handler
     */
    private void initFillFreeHandler() {
        // 从1开始
        for (int id = 1; id < MAX_ID; id++) {
            int[] versions = this.packetHandlersMap[id];
            // 针对未赋值的数据直接用之前的值
            for (int i = 1; i < versions.length; i++) {
                if (versions[i] == 0) {
                    versions[i] = versions[i - 1];
                }
            }
        }
    }

    /**
     * 构造某个packet对象
     *
     * @param id
     * @return
     */
    public DataPacket getPacket(int id) {
        Class<? extends DataPacket> clazz = this.packetPool[id & 0xff];
        if (clazz != null) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                LOGGER.error("getPacket failed!", e);
            }
        }
        return null;
    }

    /**
     * 获取handller
     */
    public AbstractPacketHandler getHandler(int id, int version) {
        return this.packetHandlers.get(this.packetHandlersMap[id][version]);
    }

    /**
     * 单例对象
     */
    private static final PacketManager INSTANCE = new PacketManager();

    /**
     * 获取单例
     */
    public static PacketManager getInstance() {
        return PacketManager.INSTANCE;
    }
}
