package com.particle.game.player.inventory;

import com.alibaba.fastjson.JSONObject;
import com.particle.api.inventory.InventoryService;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.item.ItemDropService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.inventory.holder.EntityInventoryHolder;
import com.particle.game.player.inventory.modules.MultiObservedContainerModule;
import com.particle.game.player.inventory.modules.MultiOwedContainerModule;
import com.particle.game.player.inventory.modules.SingleContainerModule;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.player.inventory.transaction.TransactionManager;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.player.OpenContainerStatusModule;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.events.level.container.InventoryLoadLevelEvent;
import com.particle.model.inventory.*;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTToHexString;
import com.particle.model.network.packets.data.ContainerClosePacket;
import com.particle.model.network.packets.data.CreativeContentPacket;
import com.particle.model.network.packets.data.InventoryContentPacket;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import com.particle.network.handler.AbstractPacketHandler;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class InventoryManager implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryManager.class);

    private static final ECSModuleHandler<MultiObservedContainerModule> MULTI_OBSERVED_CONTAINER_MODULE_HANDLER = ECSModuleHandler.buildHandler(MultiObservedContainerModule.class);

    private static final ECSModuleHandler<MultiOwedContainerModule> MULTI_OWED_CONTAINER_MODULE_HANDLER = ECSModuleHandler.buildHandler(MultiOwedContainerModule.class);

    private static final ECSModuleHandler<OpenContainerStatusModule> OPEN_CONTAINER_STATUS_MODULE_HANDLER = ECSModuleHandler.buildHandler(OpenContainerStatusModule.class);

    private static final ECSModuleHandler<SingleContainerModule> SINGLE_CONTAINER_MODULE_HANDLER = ECSModuleHandler.buildHandler(SingleContainerModule.class);


    @Inject
    private InventoryAPIProxy inventoryServiceProxy;

    @Inject
    private NetworkManager networkManager;

    @Inject
    private PlayerService playerService;

    /**
     * 存储所有创造模式具备的物品
     */
    private final List<ItemStack> creativeItems = new ArrayList<>();

    /**
     * 是否已经加载了创造模式需要的物品
     */
    private boolean isLoaded = false;

    @Inject
    private TransactionManager transactionManager;

    @Inject
    private ItemDropService itemDropService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private EntityNameService entityNameService;

    @Override
    public void setOpenContainerStatus(Entity entity, ContainerType status) {
        OpenContainerStatusModule ocsComponent = OPEN_CONTAINER_STATUS_MODULE_HANDLER.getModule(entity);
        if (ocsComponent == null) {
            return;
        }
        ocsComponent.setCurrentOpenContainer(status);
    }

    @Override
    public ContainerType getOpenContainerStatus(Entity entity) {
        OpenContainerStatusModule ocsComponent = OPEN_CONTAINER_STATUS_MODULE_HANDLER.getModule(entity);
        if (ocsComponent == null) {
            return null;
        }
        return ocsComponent.getCurrentOpenContainer();
    }

    @Override
    public Collection<Inventory> getAllOwnedInventory(Entity entity) {
        MultiOwedContainerModule multiOwedContainerModule = MULTI_OWED_CONTAINER_MODULE_HANDLER.getModule(entity);
        return multiOwedContainerModule == null ? Collections.EMPTY_LIST : multiOwedContainerModule.values();
    }

    @Override
    public Inventory getInventoryByContainerId(Entity entity, int containerId) {
        // 所属
        MultiOwedContainerModule multiOwedContainerModule = MULTI_OWED_CONTAINER_MODULE_HANDLER.getModule(entity);
        if (multiOwedContainerModule == null) {
            return null;
        }
        if (MultiOwedContainerModule.checkContainerIdValid(containerId)) {
            return multiOwedContainerModule.getInventoryByContainerId(containerId);
        }

        // 观察
        MultiObservedContainerModule multiObservedContainerModule = MULTI_OBSERVED_CONTAINER_MODULE_HANDLER.getModule(entity);
        if (multiObservedContainerModule == null) {
            return null;
        }
        if (multiObservedContainerModule.checkContainerIdValid(containerId)) {
            return multiObservedContainerModule.getInventoryByContainerId(containerId);
        }
        return null;
    }

    @Override
    public Inventory getInventoryByTileEntity(TileEntity tileEntity) {
        SingleContainerModule singleContainerModule = SINGLE_CONTAINER_MODULE_HANDLER.getModule(tileEntity);
        if (singleContainerModule == null) {
            return null;
        }

        return singleContainerModule.getInventory();
    }

    @Override
    public boolean bindMultiInventory(Entity entity, Inventory inventory) {
        int containerId = inventory.getContainerType().getConstantId();
        if (containerId >= 0) {
            // 所属
            if (MultiOwedContainerModule.checkContainerIdValid(containerId)) {
                MultiOwedContainerModule multiOwedContainerModule = MULTI_OWED_CONTAINER_MODULE_HANDLER.bindModule(entity);
                if (inventory.getInventoryHolder() == null) {
                    inventory.setInventoryHolder(new EntityInventoryHolder(entity, inventory));
                }
                return multiOwedContainerModule.addInventory(containerId, inventory);
            }

            // 观察
            MultiObservedContainerModule multiObservedContainerModule = MULTI_OBSERVED_CONTAINER_MODULE_HANDLER.getModule(entity);
            if (multiObservedContainerModule == null) {
                return false;
            }

            if (multiObservedContainerModule.checkContainerIdValid(containerId)) {
                return multiObservedContainerModule.addInventory(containerId, inventory);
            }
        } else {
            //观察
            MultiObservedContainerModule multiObservedContainerModule = MULTI_OBSERVED_CONTAINER_MODULE_HANDLER.getModule(entity);
            if (multiObservedContainerModule == null) {
                return false;
            }
            return multiObservedContainerModule.addInventory(inventory);
        }
        return false;
    }

    @Override
    public void unbindMultiInventory(Entity entity, int containerId) {
        // 所属
        MultiOwedContainerModule multiOwedContainerModule = MULTI_OWED_CONTAINER_MODULE_HANDLER.getModule(entity);
        if (multiOwedContainerModule == null) {
            return;
        }
        if (MultiOwedContainerModule.checkContainerIdValid(containerId)) {
            multiOwedContainerModule.removeInventory(containerId);
            return;
        }

        // 观察
        MultiObservedContainerModule multiObservedContainerModule = MULTI_OBSERVED_CONTAINER_MODULE_HANDLER.getModule(entity);
        if (multiObservedContainerModule == null) {
            return;
        }
        if (multiObservedContainerModule.checkContainerIdValid(containerId)) {
            multiObservedContainerModule.removeInventory(containerId);
        }
    }

    @Override
    public int getMapIdFromMultiOwned(Entity entity, Inventory inventory) {
        MultiOwedContainerModule multiOwedContainerModule = MULTI_OWED_CONTAINER_MODULE_HANDLER.getModule(entity);
        if (multiOwedContainerModule == null) {
            return -1;
        }
        int containerId = multiOwedContainerModule.getMapIdFromMultiOwned(inventory);
        if (containerId != -1) {
            return containerId;
        }

        // 观察
        MultiObservedContainerModule multiObservedContainerModule = MULTI_OBSERVED_CONTAINER_MODULE_HANDLER.getModule(entity);
        return multiObservedContainerModule == null ? -1 : multiObservedContainerModule.getMapIdFromMultiOwned(inventory);
    }

    @Override
    public void clearPlayerInventory(Player player) {
        logger.info("player[{}] clearPlayerInventory", this.entityNameService.getEntityName(player));
        // 玩家背包
        PlayerInventory playerInventory = (PlayerInventory) this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        if (playerInventory != null) {
            this.inventoryServiceProxy.clearAll(playerInventory);
        }

        // 盔甲
        Inventory armorInventory = this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ARMOR);
        if (armorInventory != null) {
            this.inventoryServiceProxy.clearAll(armorInventory);
        }

        // 副手
        Inventory deputyInventory = this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_OFFHAND);
        if (deputyInventory != null) {
            this.inventoryServiceProxy.equipItem(deputyInventory, DeputyInventory.DEPUTY,
                    ItemStack.getItem(ItemPrototype.AIR, 0), true);
        }

        // 末影箱
        Inventory playerEnderChestInventory = this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ENDER);
        if (playerEnderChestInventory != null) {
            this.inventoryServiceProxy.clearAll(playerEnderChestInventory);
        }
    }

    @Override
    public void notifyClientsAllInventoryUiClosed(Player player) {
        // 服务端主动关闭
        ContainerClosePacket containerClosePacket = new ContainerClosePacket();
        containerClosePacket.setContainerId(InventoryConstants.CONTAINER_ID_NONE);
        this.networkManager.sendMessage(player.getClientAddress(), containerClosePacket);
    }

    @Override
    public void notifyPlayerAllInventoryChanged(Player player) {
        // 观察者模式，不用更新
        if (this.playerService.getGameMode(player) == GameMode.SURVIVAL_VIEWER) {
            return;
        }
        Collection<Inventory> allInventory = this.getAllOwnedInventory(player);
        for (Inventory inventory : allInventory) {
            this.inventoryServiceProxy.notifyPlayerContentChanged(inventory);
        }
    }

    @Override
    public void notifyPlayerWithCreativeContents(Player player) {
        ItemStack[] slots = this.creativeItems.stream().toArray(ItemStack[]::new);
        if (player.getProtocolVersion() < AbstractPacketHandler.VERSION_1_16) {
            InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
            inventoryContentPacket.setContainerId(InventoryConstants.CONTAINER_ID_CREATIVE);
            inventoryContentPacket.setSlots(slots);
            this.networkManager.sendMessage(player.getClientAddress(), inventoryContentPacket);
        } else {
            CreativeContentPacket creativeContentPacket = new CreativeContentPacket();
            creativeContentPacket.setSlots(slots);
            this.networkManager.sendMessage(player.getClientAddress(), creativeContentPacket);
        }
    }

    @Override
    public void notifyPlayerWithObserverContents(Player player) {
        if (this.playerService.getGameMode(player) != GameMode.SURVIVAL_VIEWER) {
            return;
        }
        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setContainerId(InventoryConstants.CONTAINER_ID_CREATIVE);

        this.networkManager.sendMessage(player.getClientAddress(), inventoryContentPacket);
    }

    /**
     * 打开entity的默认背包
     *
     * @param entity
     */
    @Override
    public void openEntityMultiInventory(Entity entity) {
        MultiOwedContainerModule multiOwedContainerModule = MULTI_OWED_CONTAINER_MODULE_HANDLER.getModule(entity);
        if (multiOwedContainerModule == null) {
            return;
        }
        // 玩家背包
        PlayerInventory playerInventory = (PlayerInventory) this.getInventoryByContainerId(entity, InventoryConstants.CONTAINER_ID_PLAYER);
        if (logger.isDebugEnabled()) {
            logger.debug("player[{}], playerInventory:{}", this.entityNameService.getEntityName(entity), playerInventory.toString());
        }
        // 盔甲背包
        Inventory armorInventory = this.getInventoryByContainerId(entity, InventoryConstants.CONTAINER_ID_ARMOR);
        if (logger.isDebugEnabled()) {
            logger.debug("player[{}], armorInventory:{}", this.entityNameService.getEntityName(entity), armorInventory.toString());
        }
        // 副手背包
        Inventory deputyInventory = this.getInventoryByContainerId(entity, InventoryConstants.CONTAINER_ID_OFFHAND);

        if (entity instanceof Player) {
            Player player = (Player) entity;
            InventoryLoadLevelEvent inventoryLoadLevelEvent = new InventoryLoadLevelEvent(player, player.getLevel());
            inventoryLoadLevelEvent.setInventory(playerInventory);
            inventoryLoadLevelEvent.overrideAfterEventExecuted(() -> {
                if (playerInventory != null) {
                    this.inventoryServiceProxy.notifyPlayerContentChanged(playerInventory);
                    this.inventoryServiceProxy.equipItem(playerInventory, playerInventory.getItemInHandle(), null, true);
                }
                if (armorInventory != null) {
                    this.inventoryServiceProxy.notifyPlayerContentChanged(armorInventory);
                }
                if (deputyInventory != null) {
                    ItemStack toItemStack = this.inventoryServiceProxy.getItem(deputyInventory, DeputyInventory.DEPUTY);
                    this.inventoryServiceProxy.equipItem(deputyInventory, DeputyInventory.DEPUTY, toItemStack, true);
                    this.inventoryServiceProxy.notifyPlayerContentChanged(deputyInventory);
                }
            });
            inventoryLoadLevelEvent.overrideOnEventCancelled(() -> {
                if (playerInventory != null) {
                    playerInventory.getAllSlots().clear();
                }
                if (armorInventory != null) {
                    armorInventory.getAllSlots().clear();
                }
                if (deputyInventory != null) {
                    deputyInventory.getAllSlots().clear();
                }
            });
            this.eventDispatcher.dispatchEvent(inventoryLoadLevelEvent);
        } else {
            if (playerInventory != null) {
                this.inventoryServiceProxy.notifyPlayerContentChanged(playerInventory);
                this.inventoryServiceProxy.equipItem(playerInventory,
                        playerInventory.getItemInHandle(), null, true);
            }
            if (armorInventory != null) {
                this.inventoryServiceProxy.notifyPlayerContentChanged(armorInventory);
            }
            if (deputyInventory != null) {
                ItemStack toItemStack = this.inventoryServiceProxy.getItem(deputyInventory, DeputyInventory.DEPUTY);
                this.inventoryServiceProxy.
                        equipItem(deputyInventory, DeputyInventory.DEPUTY, toItemStack, true);
            }
        }
    }

    /**
     * 加载默认的背包
     *
     * @param player the entity
     */
    public void onPlayerLoadDefaultInventory(Player player) {
        MultiOwedContainerModule multiOwedContainerModule = MULTI_OWED_CONTAINER_MODULE_HANDLER.getModule(player);
        if (multiOwedContainerModule == null) {
            return;
        }
        // 玩家背包
        PlayerInventory playerInventory = (PlayerInventory) this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        if (playerInventory == null) {
            playerInventory = new PlayerInventory();
            playerInventory.setInventoryHolder(new EntityInventoryHolder(player, playerInventory));
            this.bindMultiInventory(player, playerInventory);
            this.inventoryServiceProxy.addView(player, playerInventory);
        } else {
            playerInventory.setInventoryHolder(new EntityInventoryHolder(player, playerInventory));
            this.inventoryServiceProxy.addView(player, playerInventory);
        }

        // 盔甲背包
        Inventory armorInventory = this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ARMOR);
        if (armorInventory == null) {
            armorInventory = new ArmorInventory();
            armorInventory.setInventoryHolder(new EntityInventoryHolder(player, armorInventory));
            this.bindMultiInventory(player, armorInventory);
            this.inventoryServiceProxy.addView(player, armorInventory);
        } else {
            armorInventory.setInventoryHolder(new EntityInventoryHolder(player, armorInventory));
            this.inventoryServiceProxy.addView(player, armorInventory);
        }

        // 副手背包
        Inventory deputyInventory = this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_OFFHAND);
        if (deputyInventory == null) {
            deputyInventory = new DeputyInventory();
            deputyInventory.setInventoryHolder(new EntityInventoryHolder(player, deputyInventory));
            this.bindMultiInventory(player, deputyInventory);
            this.inventoryServiceProxy.addView(player, deputyInventory);
        } else {
            deputyInventory.setInventoryHolder(new EntityInventoryHolder(player, deputyInventory));
            this.inventoryServiceProxy.addView(player, deputyInventory);
        }

        // win10版本左手单手坐标
        PlayerCursorInventory playerCursorInventory = new PlayerCursorInventory();
        playerCursorInventory.setInventoryHolder(new EntityInventoryHolder(player, playerCursorInventory));
        this.bindMultiInventory(player, playerCursorInventory);
        this.inventoryServiceProxy.addView(player, playerCursorInventory);

        // 默认合成台
        BigWorkBenchInventory workBenchInventory = new BigWorkBenchInventory();
        workBenchInventory.setInventoryHolder(new EntityInventoryHolder(player, playerCursorInventory));
        this.bindMultiInventory(player, workBenchInventory);

        // 末影箱
        Inventory playerEnderChestInventory = this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ENDER);
        if (playerEnderChestInventory == null) {
            playerEnderChestInventory = new PlayerEnderChestInventory();
            this.bindMultiInventory(player, playerEnderChestInventory);
        }
        playerEnderChestInventory.setInventoryHolder(null);
    }

    /**
     * 关闭合成台
     *
     * @param player
     */
    public void onCloseWorkBench(Player player) {
        // 清除inventory缓存
        this.transactionManager.clearCache(player);
        Inventory workBench = this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_WORKBENCH);
        // 当玩家没有登录完成时，合成台可能为空
        if (workBench == null) {
            return;
        }

        Inventory playerInventory = this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);

        Collection<ItemStack> itemStacks = workBench
                .getAllSlots()
                .values();
        if (!itemStacks.isEmpty()) {
            // 先恢复背包内容
            this.inventoryServiceProxy.notifyPlayerContentChanged(playerInventory);
            List<ItemStack> drops = this.inventoryServiceProxy.addItem(playerInventory, new ArrayList<>(itemStacks));
            if (!drops.isEmpty()) {
                this.itemDropService.playerDropItem(player, drops);
            }
        }
        // 清除bench内容
        this.inventoryServiceProxy.clearAll(workBench, false);
    }

    /**
     * 玩家关闭附魔台时，服务端会自动将附魔台的数据，同步到玩家背包中
     */
    public void onCloseEnchantTable(Player player) {
        // 清除inventory缓存
        this.transactionManager.clearCache(player);

        Inventory enchantInventory = this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ENCHANT);
        if (enchantInventory == null) {
            return;
        }
        Inventory playerInventory = this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);

        Collection<ItemStack> itemStacks = enchantInventory.getAllSlots().values();
        if (!itemStacks.isEmpty()) {
            // 先恢复背包内容
            this.inventoryServiceProxy.notifyPlayerContentChanged(playerInventory);
            List<ItemStack> drops = this.inventoryServiceProxy.addItem(playerInventory, new ArrayList<>(itemStacks));
            if (!drops.isEmpty()) {
                this.itemDropService.playerDropItem(player, drops);
            }
        }

        // 清除enchant内容
        this.inventoryServiceProxy.clearAll(enchantInventory, false);
    }

    /**
     * 玩家关闭铁砧时，服务端会自动将铁砧的数据，同步到玩家背包中
     *
     * @param player
     */
    public void onCloseAnvil(Player player) {
        // 清除inventory缓存
        this.transactionManager.clearCache(player);

        Inventory anvilInventory = this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ANVIL);
        if (anvilInventory == null) {
            return;
        }
        Inventory playerInventory = this.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);

        Collection<ItemStack> itemStacks = anvilInventory.getAllSlots().values();
        if (!itemStacks.isEmpty()) {
            // 先恢复背包内容
            this.inventoryServiceProxy.notifyPlayerContentChanged(playerInventory);
            List<ItemStack> drops = this.inventoryServiceProxy.addItem(playerInventory, new ArrayList<>(itemStacks));
            if (!drops.isEmpty()) {
                this.itemDropService.playerDropItem(player, drops);
                ;
            }
        }

        this.inventoryServiceProxy.clearAll(anvilInventory, false);
    }


    /**
     * 加载创造模式下的物品
     */
    public void loadCreativeItems() {
        if (isLoaded) {
            return;
        }
        try {
            String result = IOUtils.resourceToString("new_creativeitems.json", Charsets.toCharset("utf-8"), this.getClass().getClassLoader());
            if (StringUtils.isEmpty(result)) {
                logger.error("找不到创造模式下的物品文件");
                return;
            }
            JSONObject jsonResult = JSONObject.parseObject(result);
            List<Map<String, Object>> allItems = (List<Map<String, Object>>) jsonResult.get("items");
            if (allItems == null) {
                logger.error("创造模式下的物品文件错误");
                return;
            }
            for (Map<String, Object> itemInfo : allItems) {
                int id = (int) itemInfo.get("id");
                String name = (String) itemInfo.get("name");
                int meta = (int) itemInfo.getOrDefault("damage", 0);
                String nbtHex = (String) itemInfo.get("nbt_hex");

                ItemStack itemStack = ItemStack.getItem(name);
                if (itemStack == null) {
                    continue;
                }

                itemStack.setMeta(meta);
                if (!StringUtils.isEmpty(nbtHex)) {
                    itemStack.updateNBT((NBTTagCompound) NBTToHexString.convertToTag(nbtHex));
                }
                creativeItems.add(itemStack);
            }
            isLoaded = true;
        } catch (Exception e) {
            logger.error("loadCreativeItems失败。", e);
        }
    }
}
