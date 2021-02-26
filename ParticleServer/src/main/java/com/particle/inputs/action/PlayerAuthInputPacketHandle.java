package com.particle.inputs.action;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.attribute.satisfaction.HungerService;
import com.particle.game.entity.link.EntityLinkServiceProxy;
import com.particle.game.entity.link.EntityMountControlService;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.player.state.PlayerMovePacketModule;
import com.particle.game.utils.blueprint.BluePrintBuilder;
import com.particle.game.utils.blueprint.context.BackgroundContext;
import com.particle.game.utils.blueprint.node.RootNode;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.physical.PhysicalService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.events.level.player.*;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.PlayerAuthInputPacket;
import com.particle.model.player.Player;
import com.particle.model.player.PositionMode;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.particle.model.player.action.PlayerAuthInputType.*;

@Singleton
public class PlayerAuthInputPacketHandle extends PlayerPacketHandle<PlayerAuthInputPacket> {

    private static final Logger logger = LoggerFactory.getLogger(PlayerAuthInputPacketHandle.class);
    private static final ECSModuleHandler<PlayerMovePacketModule> PLAYER_MOVE_PACKET_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlayerMovePacketModule.class);

    @Inject
    private PlayerInventoryAPI inventoryService;

    @Inject
    private MovementServiceProxy movementServiceProxy;
    @Inject
    private PositionService positionService;
    @Inject
    private PhysicalService physicalService;

    @Inject
    private NetworkManager networkManager;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private PlayerService playerService;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private HungerService hungerService;

    @Inject
    private BlockService blockService;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private EntityLinkServiceProxy entityLinkServiceProxy;

    @Inject
    private EntityMountControlService entityMountControlService;

    private RootNode<PlayerAuthInputPacketHandle.PlayerMoveContext> rootNode;

    @Inject
    public void initProcessStream() {
        this.rootNode = new BluePrintBuilder<PlayerAuthInputPacketHandle.PlayerMoveContext>("PlayerMoveProcess")
                .check((context) -> movementServiceProxy.checkPlayerMove(context.getPlayer(), context.getPosition(), context.getDirection(), context.getPositionMode()))
                .event((context -> new PlayerMoveEvent(context.getPlayer(), context.getPosition(), context.getDirection())))
                .job((context -> hungerService.onPlayerMoveConsumption(context.getPlayer(), context.getPosition())))
                .job((context) -> positionService.setPositionAndDirection(context.getPlayer(), context.getPosition(), context.getDirection()))
                .job((context) -> entitySpawnService.respawn(context.getPlayer().getLevel(), context.getPlayer()))
                .onHandle()
                .job((context) -> movementServiceProxy.refreshPlayerPosition(context.getPlayer()))
                .onCancel()
                .dispatcher()
                .checked()
                .build();
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.PLAYER_AUTH_INPUT_PACKET;
    }

    @Override
    protected void handlePacket(Player player, PlayerAuthInputPacket packet) {
        PlayerMovePacketModule playerMovePacketModule = PLAYER_MOVE_PACKET_MODULE_ECS_MODULE_HANDLER.getModule(player);
        // 若位置不等於前一次 或是 腦袋位置不等於前一次
        if (playerMovePacketModule.getPosition().getX() != packet.getPlayerPosition().getX() ||
                playerMovePacketModule.getPosition().getY() != packet.getPlayerPosition().getY() ||
                playerMovePacketModule.getPosition().getZ() != packet.getPlayerPosition().getZ() ||
                playerMovePacketModule.getDirection().getPitch() != packet.getPlayerRotation().getX() ||
                playerMovePacketModule.getDirection().getYaw() != packet.getPlayerRotation().getY() ||
                playerMovePacketModule.getDirection().getYawHead() != packet.getHeadRotation()) {
            // 换算脚下坐标
            PlayerAuthInputPacketHandle.PlayerMoveContext playerMoveContext = new PlayerAuthInputPacketHandle.PlayerMoveContext(player);
            playerMoveContext.setPosition(packet.getPlayerPosition().subtract(0, 1.62f, 0));
            playerMoveContext.setDirection(new Direction(packet.getPlayerRotation().getX(), packet.getPlayerRotation().getY(), packet.getHeadRotation()));
            playerMoveContext.setPositionMode(PositionMode.NORMAL);

            this.rootNode.run(playerMoveContext);

            // 紀錄這次包內容
            playerMovePacketModule.setPosition(new Vector3f(packet.getPlayerPosition().getX(), packet.getPlayerPosition().getY(), packet.getPlayerPosition().getZ()));
            playerMovePacketModule.setDirection(new Direction(packet.getPlayerRotation().getX(), packet.getPlayerRotation().getY(), packet.getHeadRotation()));
        }

        // 若有騎乘寵物 且 移動非 0
        if (entityLinkServiceProxy.getVehicle(player) != null) {
            entityMountControlService.setAim(player, packet.getMoveVector());
        }

        long inputData = packet.getInputData();
        // 開始跳躍
        if (checkAction(inputData, ActionStartJump.getStatus())) {
            PlayerJumpGameEvent playerJumpGameEvent = new PlayerJumpGameEvent(player);
            eventDispatcher.dispatchEvent(playerJumpGameEvent);

            if (!this.physicalService.isKinematic(player)) {
                // 往前的速度
                Vector3f aheadDirectionVector = this.positionService.getDirection(player).getAheadDirectionVector();
                if (this.metaDataService.getDataFlag(player, MetadataDataFlag.DATA_FLAG_SPRINTING)) {
                    aheadDirectionVector = aheadDirectionVector.multiply(5);
                } else {
                    aheadDirectionVector = aheadDirectionVector.multiply(3);
                }

                // 往上的速度
                aheadDirectionVector.setY(6f);

                this.movementServiceProxy.setMotion(player, aheadDirectionVector);
            }
        }


        // 開始蹲下
        if (checkAction(inputData, ActionStartSneaking.getStatus())) {
            PlayerStartSneakEvent playerStartSneakEvent = new PlayerStartSneakEvent(player);
            eventDispatcher.dispatchEvent(playerStartSneakEvent);

            metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_SNEAKING, true, true);
        }
        // 結束蹲下
        else if (checkAction(inputData, ActionStopSneaking.getStatus())) {
            PlayerStopSneakEvent playerStopSneakEvent = new PlayerStopSneakEvent(player);
            eventDispatcher.dispatchEvent(playerStopSneakEvent);

            metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_SNEAKING, false, true);
        }


        // 開始滑翔
        if (checkAction(inputData, ActionStartGliding.getStatus())) {
            metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_GLIDING, true, true);
        }
        // 結束滑翔
        else if (checkAction(inputData, ActionStopGliding.getStatus())) {
            metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_GLIDING, false, true);
        }


        // 開始疾跑
        if (checkAction(inputData, ActionStartSprinting.getStatus())) {
            metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_SPRINTING, true, true);
        }
        // 結束疾跑
        else if (checkAction(inputData, ActionStopSprinting.getStatus())) {
            metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_SPRINTING, false, true);
        }


        // 開始游泳
        if (checkAction(inputData, ActionStartSwimming.getStatus())) {
            PlayerStartSwimmingEvent playerStartSwimmingEvent = new PlayerStartSwimmingEvent(player);
            eventDispatcher.dispatchEvent(playerStartSwimmingEvent);

            // 修改碰撞體
            metaDataService.setFloatData(player, EntityMetadataType.BOUNDING_BOX_WIDTH, 0.9f);
            metaDataService.setFloatData(player, EntityMetadataType.BOUNDING_BOX_HEIGHT, 0.9f);

            metaDataService.setDataFlag(player, MetadataDataFlag.SWIMMING, true, true);
            metaDataService.refreshPlayerMetadata(player);
        }
        // 結束游泳
        else if (checkAction(inputData, ActionStopSwimming.getStatus())) {
            PlayerStopSwimmingEvent playerStopSwimmingEvent = new PlayerStopSwimmingEvent(player);
            eventDispatcher.dispatchEvent(playerStopSwimmingEvent);

            // 恢復碰撞體
            metaDataService.setFloatData(player, EntityMetadataType.BOUNDING_BOX_WIDTH, 0.9f);
            metaDataService.setFloatData(player, EntityMetadataType.BOUNDING_BOX_HEIGHT, 1.9f);

            metaDataService.setDataFlag(player, MetadataDataFlag.SWIMMING, false, true);
            metaDataService.refreshPlayerMetadata(player);
        }
    }

    private boolean checkAction(long inputData, long actionType) {
        return (inputData & actionType) == actionType;
    }

    private class PlayerMoveContext extends BackgroundContext {

        private Vector3f position;
        private Direction direction;
        private PositionMode positionMode;

        public PlayerMoveContext(Player player) {
            super(player);
        }

        public Vector3f getPosition() {
            return position;
        }

        public void setPosition(Vector3f position) {
            this.position = position;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public PositionMode getPositionMode() {
            return positionMode;
        }

        public void setPositionMode(PositionMode positionMode) {
            this.positionMode = positionMode;
        }
    }

    protected boolean isRealTimeHandle() {
        return true;
    }
}
