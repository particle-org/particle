package com.particle.inputs.action;

import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.physical.PhysicalService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.events.level.player.*;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.level.LevelEventType;
import com.particle.model.math.BlockFace;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.LevelEventPacket;
import com.particle.model.network.packets.data.PlayerActionPacket;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import com.particle.network.handler.AbstractPacketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerActionPacketHandle extends PlayerPacketHandle<PlayerActionPacket> {

    private static final Logger logger = LoggerFactory.getLogger(PlayerActionPacketHandle.class);

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
    private BlockService blockService;

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.PLAYER_ACTION_PACKET;
    }

    @Override
    protected void handlePacket(Player player, PlayerActionPacket packet) {
        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);

        if (playerInventory == null) {
            logger.error("Fail to handle player {} action packer because inventory missing!", player.getIdentifiedStr());
            return;
        }

        ItemStack holdItem = inventoryService.getItem(playerInventory, playerInventory.getItemInHandle());

        switch (packet.getAction()) {
            //开始破坏方块时触发
            case ActionStartDestroyBlock:
                if (this.playerService.getGameMode(player) != GameMode.CREATIVE) {
                    player.setLastOperation(packet.getPosition());

                    blockService.blockDamagedByPlayer(player, packet.getPosition(), holdItem, BlockFace.fromIndex(packet.getFace()));
                }

                break;
            //中途放弃破坏方块时触发
            case ActionAbortDestroyBlock:
                if (player.getLastOperation() != null) {
                    blockService.blockStopDamagedByPlayer(player, player.getLastOperation());

                    player.setLastOperation(null);
                }

                break;
            //破坏方块成功时触发
            case ActionStopDestroyBlock:
                //玩家回复
                if (player.getLastOperation() != null) {
                    LevelEventPacket levelEventPacket = new LevelEventPacket();
                    levelEventPacket.setEventType(LevelEventType.StopBlockCracking.getType());
                    levelEventPacket.setPosition(new Vector3f(player.getLastOperation()));
                    levelEventPacket.setData(0);

                    networkManager.sendMessage(player.getClientAddress(), levelEventPacket);
                }


                player.setLastOperation(null);

                break;
            //持续破坏方块时不停的发
            case ActionCrackBlock:
                break;
            case ActionStartJump:
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

                break;
            case ActionStartSneaking:
                PlayerStartSneakEvent playerStartSneakEvent = new PlayerStartSneakEvent(player);
                eventDispatcher.dispatchEvent(playerStartSneakEvent);

                metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_SNEAKING, true, true);

                break;
            case ActionStopSneaking:
                PlayerStopSneakEvent playerStopSneakEvent = new PlayerStopSneakEvent(player);
                eventDispatcher.dispatchEvent(playerStopSneakEvent);

                metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_SNEAKING, false, true);

                break;
            case ActionRespawn:
                // 1.12及以前版本的兼容
                if (player.getProtocolVersion() < AbstractPacketHandler.VERSION_1_13) {
                    this.playerService.respawnPlayer(player);
                }

                break;
            case ActionStartGliding:
                metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_GLIDING, true, true);

                break;
            case ActionStopGliding:
                metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_GLIDING, false, true);

                break;
            case ActionStartSprinting:
                metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_SPRINTING, true, true);

                break;
            case ActionStopSprinting:
                metaDataService.setDataFlag(player, MetadataDataFlag.DATA_FLAG_SPRINTING, false, true);

                break;
            case ActionStartSwimming:
                PlayerStartSwimmingEvent playerStartSwimmingEvent = new PlayerStartSwimmingEvent(player);
                eventDispatcher.dispatchEvent(playerStartSwimmingEvent);

                // 修改碰撞體
                metaDataService.setFloatData(player, EntityMetadataType.BOUNDING_BOX_WIDTH, 0.9f);
                metaDataService.setFloatData(player, EntityMetadataType.BOUNDING_BOX_HEIGHT, 0.9f);

                metaDataService.setDataFlag(player, MetadataDataFlag.SWIMMING, true, true);
                metaDataService.refreshPlayerMetadata(player);
                break;
            case ActionStopSwimming:
                PlayerStopSwimmingEvent playerStopSwimmingEvent = new PlayerStopSwimmingEvent(player);
                eventDispatcher.dispatchEvent(playerStopSwimmingEvent);

                // 恢復碰撞體
                metaDataService.setFloatData(player, EntityMetadataType.BOUNDING_BOX_WIDTH, 0.9f);
                metaDataService.setFloatData(player, EntityMetadataType.BOUNDING_BOX_HEIGHT, 1.9f);

                metaDataService.setDataFlag(player, MetadataDataFlag.SWIMMING, false, true);
                metaDataService.refreshPlayerMetadata(player);
                break;
            default:
                logger.debug("Action {}", packet.getAction());
                break;
        }
    }

    protected boolean isRealTimeHandle() {
        return true;
    }
}
