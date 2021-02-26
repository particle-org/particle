package com.particle.inputs.inventory;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.interactor.BlockInteractiveService;
import com.particle.game.entity.attack.EntityAttackedHandleService;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.entity.service.EntityRecorderService;
import com.particle.game.entity.service.VirtualEntityService;
import com.particle.game.item.ItemService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.interactive.EntityInteractiveService;
import com.particle.game.player.interactive.EntityInteractiveTTLModule;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.player.inventory.transaction.TransactionManager;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.LevelService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.block.Block;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.entity.EntityInteractModule;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.entity.model.others.NpcEntity;
import com.particle.model.events.level.block.BlockBreakEvent;
import com.particle.model.events.level.player.PlayerInteractEvent;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.common.InventoryTransactionType;
import com.particle.model.inventory.common.ItemUseInventoryActionType;
import com.particle.model.inventory.common.ItemUseOnEntityInventoryType;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemReleaseInventoryData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.inventory.data.ItemUseOnEntityInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.InventoryTransactionPacket;
import com.particle.model.network.packets.data.UpdateBlockPacket;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InventoryTransactionPacketHandle extends PlayerPacketHandle<InventoryTransactionPacket> {

    private static final Logger logger = LoggerFactory.getLogger(InventoryTransactionPacketHandle.class);

    private static final ECSModuleHandler<EntityInteractModule> ENTITY_INTERACT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityInteractModule.class);
    private static final ECSModuleHandler<EntityInteractiveTTLModule> ENTITY_INTERACTIVE_TTL_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityInteractiveTTLModule.class);


    @Inject
    private InventoryManager inventoryManager;
    @Inject
    private PlayerInventoryAPI inventoryService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private TransactionManager transactionManager;

    @Inject
    private EntityAttackedHandleService entityAttackedHandleService;
    @Inject
    private EntityInteractiveService entityInteractiveService;

    @Inject
    private LevelService levelService;

    @Inject
    private ItemService itemService;

    @Inject
    private PlayerService playerService;

    @Inject
    private BlockService blockService;

    @Inject
    private BlockInteractiveService blockInteractiveService;

    @Inject
    private VirtualEntityService virtualEntityService;

    @Inject
    private EntityRecorderService entityRecorderService;

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private NetworkManager networkManager;

    @Override
    protected void handlePacket(Player player, InventoryTransactionPacket packet) {
        // 玩家是观察者模式，更新其玩家的背包数据，不做其他操作
        if (this.playerService.getGameMode(player) == GameMode.SURVIVAL_VIEWER) {
            inventoryManager.notifyPlayerAllInventoryChanged(player);
            return;
        }

        InventoryTransactionType transactionType = packet.getTransactionType();

        logger.debug("inventory transaction:" + packet);
        switch (transactionType) {
            case NORMAL:
                transactionManager.preHandleInventoryTransaction(player, transactionType, packet.getInventoryActionData());
                break;
            case MISMATCH:
                // 服务端重新向客户端发送玩家背包数据
                inventoryManager.notifyPlayerAllInventoryChanged(player);
                break;
            case ITEM_USE:
                if (!(packet.getComplexInventoryTransaction() instanceof ItemUseInventoryData)) {
                    return;
                }

                // 检查TTL组件是否就绪
                EntityInteractiveTTLModule entityInteractiveTTLModule = ENTITY_INTERACTIVE_TTL_MODULE_HANDLER.getModule(player);
                if (entityInteractiveTTLModule == null) {
                    return;
                }

                //在Handle中对packet做初步解析，并路由至对应功能的模块中
                ItemUseInventoryData itemTransactionData = (ItemUseInventoryData) packet.getComplexInventoryTransaction();

                // 检查操作间隔
                if (itemTransactionData.getActionType() == ItemUseInventoryActionType.PLACE) {
                    if (!entityInteractiveTTLModule.requestPlaceBlockValid()) {
                        levelService.refreshBlockAt(player.getLevel(), player, itemTransactionData.getPosition().getSide(itemTransactionData.getFace()));
                        return;
                    }
                }

                PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
                ItemStack holdItem = inventoryService.getItem(playerInventory, playerInventory.getItemInHandle());

                // 强制覆盖手持物品
                itemTransactionData.setItem(holdItem);

                //方块破坏需要同步至Level层，有单独的处理逻辑
                if (itemTransactionData.getActionType() == ItemUseInventoryActionType.DESTROY) {
                    // 将PlayerActionPacketHandle中的事件移动到这里统一处理，保证方块被破坏时的事件能被cancel掉
                    PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(player, holdItem, itemTransactionData.getPosition(), itemTransactionData.getFace());
                    playerInteractEvent.overrideOnEventCancelled(() -> {
                        // 取消破坏， 则还原物品，仅发送给本人即可
                        Block sourceBlock = this.levelService.getBlockAt(player.getLevel(), itemTransactionData.getPosition());
                        UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
                        updateBlockPacket.setBlock(sourceBlock);
                        updateBlockPacket.setVector3(itemTransactionData.getPosition());
                        updateBlockPacket.setRuntimeId(sourceBlock.getRuntimeId());
                        updateBlockPacket.setLayer(0);
                        updateBlockPacket.setFlag(UpdateBlockPacket.All);
                        this.networkManager.sendMessage(player.getClientAddress(), updateBlockPacket);
                    });

                    this.eventDispatcher.dispatchEvent(playerInteractEvent);

                    this.blockService.breakBlockByPlayer(player, itemTransactionData.getPosition(), BlockBreakEvent.Caused.PLAYER);
                } else {
                    PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(player, holdItem, itemTransactionData.getPosition(), itemTransactionData.getFace());
                    this.eventDispatcher.dispatchEvent(playerInteractEvent);

                    playerInteractEvent.overrideOnEventCancelled(() -> {
                        this.inventoryService.notifyPlayerContentChanged(playerInventory);
                    });

                    InventoryActionData[] inventoryActionData = packet.getInventoryActionData();

                    //判断玩家是否可以与方块交互
                    boolean canInteractive = false;
                    // 不是跪着，同时是放置物品
                    if (!this.metaDataService.getDataFlag(player, MetadataDataFlag.DATA_FLAG_SNEAKING)
                            && itemTransactionData.getActionType() == ItemUseInventoryActionType.PLACE) {
                        Block block = this.levelService.getBlockAt(player.getLevel(), itemTransactionData.getPosition());

                        canInteractive = blockInteractiveService.onBlockInteractive(player, block, itemTransactionData.getPosition(), itemTransactionData.getClickPosition());
                    }

                    //若玩家处于蹲着的状态或方块不可交互，则使用物品
                    if (!canInteractive) {
                        switch (itemTransactionData.getActionType()) {
                            case PLACE:
                                this.itemService.onItemPlaced(player, itemTransactionData, inventoryActionData);
                                break;
                            case USE:
                                this.itemService.onPreItemUsed(player, itemTransactionData, inventoryActionData);
                                break;
                        }
                    }

                }

                break;
            case ITEM_RELEASE:
                if (!(packet.getComplexInventoryTransaction() instanceof ItemReleaseInventoryData)) {
                    return;
                }

                ItemReleaseInventoryData itemReleaseInventoryData = (ItemReleaseInventoryData) packet.getComplexInventoryTransaction();

                playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
                holdItem = inventoryService.getItem(playerInventory, playerInventory.getItemInHandle());

                // 强制覆盖手持物品
                itemReleaseInventoryData.setItem(holdItem);

                InventoryActionData[] inventoryActionData1 = packet.getInventoryActionData();
                switch (itemReleaseInventoryData.getActionType()) {
                    case RELEASE:
                        this.itemService.onItemRelease(player, itemReleaseInventoryData, inventoryActionData1);
                        break;
                    case USE:
                        this.itemService.onPostItemUsed(player, itemReleaseInventoryData, inventoryActionData1);
                        break;
                }

                break;
            case ITEM_USE_ON_ENTITY:
                if (!(packet.getComplexInventoryTransaction() instanceof ItemUseOnEntityInventoryData)) {
                    return;
                }

                ItemUseOnEntityInventoryData onEntityInventoryData = (ItemUseOnEntityInventoryData) packet.getComplexInventoryTransaction();

                playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
                holdItem = inventoryService.getItem(playerInventory, playerInventory.getItemInHandle());

                // 强制覆盖手持物品
                onEntityInventoryData.setItem(holdItem);

                // 判断交互的具体Entity
                Entity entity = this.entityRecorderService.getEntity(onEntityInventoryData.getEntityRuntimeId());
                if (entity == null) {
                    entity = this.virtualEntityService.getVirtualEntity(onEntityInventoryData.getEntityRuntimeId());
                }

                if (entity != null) {
                    if (entity instanceof NpcEntity) {
                        NpcEntity npcEntity = (NpcEntity) entity;

                        EntityInteractModule entityInteractModule = ENTITY_INTERACT_MODULE_HANDLER.getModule(npcEntity);
                        if (entityInteractModule != null) {
                            entityInteractModule.onInteractive(player, npcEntity, onEntityInventoryData.getItem());

                            if (entityInteractModule.isCancelAfterProcess()) {
                                return;
                            }
                        }
                    }

                    // 执行被交互者的相关操作
                    this.entityInteractiveService.onEntityInteractived(entity, player, onEntityInventoryData.getItem());

                    if (onEntityInventoryData.getActionType() == ItemUseOnEntityInventoryType.ATTACK) {
                        // 执行被攻击者的相关操作
                        this.entityAttackedHandleService.entityAttackedByEntity(player, entity);
                    }
                }

                break;
            default:
                break;
        }
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.INVENTORY_TRANSACTION_PACKET;
    }
}
