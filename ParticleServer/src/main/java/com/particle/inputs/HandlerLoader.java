package com.particle.inputs;

import com.particle.event.dispatcher.PacketDispatcher;
import com.particle.inputs.action.*;
import com.particle.inputs.animate.PlayerAnimatePacketHandle;
import com.particle.inputs.command.CommandRequestPacketHandle;
import com.particle.inputs.connector.CreateSessionHandle;
import com.particle.inputs.connector.DestroySessionHandle;
import com.particle.inputs.entity.EntityEventPacketHandle;
import com.particle.inputs.form.ModalFormResponsePacketHandle;
import com.particle.inputs.form.ServerSettingsRequestPacketHandle;
import com.particle.inputs.gamemode.SetPlayerGameTypePacketHandle;
import com.particle.inputs.inventory.*;
import com.particle.inputs.login.LoginPacketHandle;
import com.particle.inputs.map.MapRequestHandle;
import com.particle.inputs.move.MovePlayerPacketHandle;
import com.particle.inputs.movement.PlayerMoveProcessStream;
import com.particle.inputs.netease.NeteaseBuySuccessOperation;
import com.particle.inputs.recipe.CraftingEventPacketHandle;
import com.particle.inputs.resources.ResourcePackChunkHandle;
import com.particle.inputs.resources.ResourcePackRespHandle;
import com.particle.inputs.sound.LevelSoundEventV1PacketHandle;
import com.particle.inputs.sound.LevelSoundEventV2PacketHandle;
import com.particle.inputs.text.BlockEntityDataPacketHandle;
import com.particle.inputs.text.TextPacketHandle;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HandlerLoader {

    private PacketDispatcher packetDispatcher = PacketDispatcher.getInstance();

    // 连接
    @Inject
    private CreateSessionHandle createSessionHandle;
    @Inject
    private DestroySessionHandle destroySessionHandle;

    // 登录
    @Inject
    private LoginPacketHandle loginPacketHandle;

    //移动
    @Inject
    private MovePlayerPacketHandle movePlayerPacketHandle;
    @Inject
    private PlayerMoveProcessStream playerMoveProcessStream;

    //行为
    @Inject
    private PlayerActionPacketHandle playerActionPacketHandle;

    @Inject
    private PlayerAuthInputPacketHandle playerAuthInputPacketHandle;

    @Inject
    private InteractPacketHandle interactPacketHandle;

    @Inject
    private PlayerInputPacketHandle playerInputPacketHandle;

    @Inject
    private PlayerRespawnPacketHandle playerRespawnPacketHandle;

    // 资源
    @Inject
    private ResourcePackChunkHandle resourcePackChunkHandle;
    @Inject
    private ResourcePackRespHandle resourcePackRespHandle;

    // 命令
    @Inject
    private CommandRequestPacketHandle commandRequestPacketHandle;

    // 背包
    @Inject
    private ContainerClosePacketHandle containerClosePacketHandle;
    @Inject
    private InventoryTransactionPacketHandle inventoryTransactionPacketHandle;
    @Inject
    private MobArmorEquipmentPacketHandle mobArmorEquipmentPacketHandle;
    @Inject
    private MobEquipmentPacketHandle mobEquipmentPacketHandle;
    @Inject
    private PlayerHotbarPacketHandle playerHotbarPacketHandle;

    // 表单
    @Inject
    private ModalFormResponsePacketHandle modalFormResponsePacketHandle;
    @Inject
    private ServerSettingsRequestPacketHandle serverSettingsRequestPacketHandle;

    // 配方
    @Inject
    private CraftingEventPacketHandle craftingEventPacketHandle;

    // 玩家模式
    @Inject
    private SetPlayerGameTypePacketHandle setPlayerGameTypePacketHandle;

    // entity
    @Inject
    private EntityEventPacketHandle entityEventPacketHandle;
    @Inject
    private PlayerAnimatePacketHandle playerAnimatePacketHandle;
    @Inject
    private BlockEntityDataPacketHandle blockEntityDataPacketHandle;

    @Inject
    private TextPacketHandle textPacketHandle;

    @Inject
    private LevelSoundEventV2PacketHandle levelSoundEventV2PacketHandle;
    @Inject
    private LevelSoundEventV1PacketHandle levelSoundEventV1PacketHandle;

    // 地图
    @Inject
    private MapRequestHandle mapRequestHandle;

    // 付费
    @Inject
    private NeteaseBuySuccessOperation neteaseBuySuccessOperation;

    public void init() {
        this.packetDispatcher.subscriptPacket(createSessionHandle);
        this.packetDispatcher.subscriptPacket(destroySessionHandle);

        this.packetDispatcher.subscriptPacket(loginPacketHandle);

        this.packetDispatcher.subscriptPacket(playerMoveProcessStream);

        this.packetDispatcher.subscriptPacket(playerActionPacketHandle);
        this.packetDispatcher.subscriptPacket(playerAuthInputPacketHandle);
        this.packetDispatcher.subscriptPacket(interactPacketHandle);
        this.packetDispatcher.subscriptPacket(playerInputPacketHandle);
        this.packetDispatcher.subscriptPacket(playerRespawnPacketHandle);

        this.packetDispatcher.subscriptPacket(resourcePackChunkHandle);
        this.packetDispatcher.subscriptPacket(resourcePackRespHandle);

        this.packetDispatcher.subscriptPacket(commandRequestPacketHandle);

        this.packetDispatcher.subscriptPacket(containerClosePacketHandle);
        this.packetDispatcher.subscriptPacket(inventoryTransactionPacketHandle);
        this.packetDispatcher.subscriptPacket(mobArmorEquipmentPacketHandle);
        this.packetDispatcher.subscriptPacket(mobEquipmentPacketHandle);
        this.packetDispatcher.subscriptPacket(playerHotbarPacketHandle);
        this.packetDispatcher.subscriptPacket(modalFormResponsePacketHandle);
        this.packetDispatcher.subscriptPacket(serverSettingsRequestPacketHandle);
        this.packetDispatcher.subscriptPacket(craftingEventPacketHandle);

        this.packetDispatcher.subscriptPacket(setPlayerGameTypePacketHandle);

        this.packetDispatcher.subscriptPacket(entityEventPacketHandle);
        this.packetDispatcher.subscriptPacket(playerAnimatePacketHandle);
        this.packetDispatcher.subscriptPacket(textPacketHandle);
        this.packetDispatcher.subscriptPacket(blockEntityDataPacketHandle);

        this.packetDispatcher.subscriptPacket(this.levelSoundEventV2PacketHandle);
        this.packetDispatcher.subscriptPacket(this.levelSoundEventV1PacketHandle);

        this.packetDispatcher.subscriptPacket(this.mapRequestHandle);

        this.packetDispatcher.subscriptPacket(this.neteaseBuySuccessOperation);
    }
}
