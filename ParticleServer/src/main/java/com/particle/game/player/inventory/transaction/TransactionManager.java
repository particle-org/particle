package com.particle.game.player.inventory.transaction;

import com.particle.api.server.PrisonServiceProviderApi;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.enchantment.AnvilService;
import com.particle.game.block.enchantment.EnchantmentService;
import com.particle.game.item.ItemDropService;
import com.particle.game.player.craft.CraftService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.player.inventory.transaction.processor.AnvilOutputTransaction;
import com.particle.game.player.inventory.transaction.processor.CompoundTransaction;
import com.particle.game.player.inventory.transaction.processor.EnchantOutputTransaction;
import com.particle.game.player.inventory.transaction.processor.InventoryTransaction;
import com.particle.game.player.inventory.transaction.processor.action.*;
import com.particle.model.events.level.container.InventoryTransactionLevelEvent;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.*;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.InventorySourceData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.network.packets.data.ContainerClosePacket;
import com.particle.model.player.Player;
import com.particle.model.utils.Pair;
import com.particle.network.NetworkManager;
import com.particle.network.handler.AbstractPacketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class TransactionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryManager.class);

    /**
     * Fake layout IDs for the SOURCE_TODO type (99999)
     * <p>
     * These identifiers are used for inventory source types which are not currently implemented server-side in MCPE.
     * As a general rule of thumb, anything that doesn't have a permanent inventory is client-side. These types are
     * to allow servers to track what is going on in client-side windows.
     * <p>
     * Expect these to change in the future.
     */
    public static final int SOURCE_TYPE_CRAFTING_ADD_INGREDIENT = -2;
    public static final int SOURCE_TYPE_CRAFTING_REMOVE_INGREDIENT = -3;
    public static final int SOURCE_TYPE_CRAFTING_RESULT = -4;
    public static final int SOURCE_TYPE_CRAFTING_USE_INGREDIENT = -5;

    public static final int SOURCE_TYPE_ANVIL_INPUT = -10;
    public static final int SOURCE_TYPE_ANVIL_MATERIAL = -11;
    public static final int SOURCE_TYPE_ANVIL_RESULT = -12;
    public static final int SOURCE_TYPE_ANVIL_OUTPUT = -13;

    public static final int SOURCE_TYPE_ENCHANT_INPUT = -15;
    public static final int SOURCE_TYPE_ENCHANT_MATERIAL = -16;
    public static final int SOURCE_TYPE_ENCHANT_OUTPUT = -17;

    public static final int SOURCE_TYPE_TRADING_INPUT_1 = -20;
    public static final int SOURCE_TYPE_TRADING_INPUT_2 = -21;
    public static final int SOURCE_TYPE_TRADING_USE_INPUTS = -22;
    public static final int SOURCE_TYPE_TRADING_OUTPUT = -23;

    public static final int SOURCE_TYPE_BEACON = -24;
    /**
     * Any client-side layout dropping its contents when the entity closes it
     */
    public static final int SOURCE_TYPE_CONTAINER_DROP_CONTENTS = -100;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private InventoryAPIProxy inventoryServiceProxy;

    @Inject
    private NetworkManager networkManager;

    @Inject
    private InventoryActionsPreProcessor inventoryActionsPreProcessor;

    @Inject
    private ItemDropService itemDropService;

    @Inject
    private CraftService craftService;

    @Inject
    private AnvilService anvilService;

    @Inject
    private EnchantmentService enchantmentService;

    @Inject
    private PrisonServiceProviderApi prisonServiceProviderApi;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    private boolean DEBUG_LOG = false;

    /**
     * 存储所有玩家的transaction
     */
    private Map<InetSocketAddress, InventoryTransaction> allCacheTransactions = new ConcurrentHashMap<>();

    /**
     * 清除玩家的缓存
     *
     * @param player
     */
    public void clearCache(Player player) {
        InventoryTransaction transaction = this.allCacheTransactions.remove(player.getClientAddress());
        if (transaction != null) {
            // 当存在transaction过期的时候，首先恢复背包内容
            LOGGER.error("transaction出错，删除先前缓存，同时开始恢复背包内容.");
            transaction.restoreInventoryContent(this.craftService, false);
        }
    }

    /**
     * 预处理InventoryTransactionPacket的transaction过程
     *
     * @param player
     * @param transactionType
     * @param inventoryActionData
     */
    public void preHandleInventoryTransaction(Player player, InventoryTransactionType transactionType, InventoryActionData[] inventoryActionData) {
        if (DEBUG_LOG) {
            for (InventoryActionData inventoryActionDatum : inventoryActionData) {
                System.out.printf("Item %s:%s changed at type:%s,containId:%d, slot:%d, %d -> %d\r\n",
                        inventoryActionDatum.getFromItem().getItemType().getName(),
                        inventoryActionDatum.getToItem().getItemType().getName(),
                        inventoryActionDatum.getSource().getSourceType().getName(),
                        inventoryActionDatum.getSource().getContainerId(),
                        inventoryActionDatum.getSlot(),
                        inventoryActionDatum.getFromItem().getCount(),
                        inventoryActionDatum.getToItem().getCount());
            }
        }

        if (transactionType != InventoryTransactionType.NORMAL) {
            LOGGER.error(String.format("当前的actionType[%s]，不应该在此被处理。", transactionType));
            return;
        }
        if (inventoryActionData == null || inventoryActionData.length == 0) {
            return;
        }


        // 是否附魔包
        boolean isEnchant = false;
        for (InventoryActionData actionData : inventoryActionData) {
            if (actionData.getSource().getContainerId() == SOURCE_TYPE_ENCHANT_INPUT ||
                    actionData.getSource().getContainerId() == SOURCE_TYPE_ENCHANT_OUTPUT ||
                    actionData.getSource().getContainerId() == SOURCE_TYPE_ENCHANT_MATERIAL) {
                isEnchant = true;
                break;
            }
        }

        // 1.16 附魔台修改 將1.16包改為 1.14之前的包內容
        if (player.getProtocolVersion() >= AbstractPacketHandler.VERSION_1_16 && isEnchant) {
            if (inventoryActionData.length == 3) {
                InventoryActionData inputActionData = null;
                InventoryActionData outputActionData = null;
                InventoryActionData containerActionData = null;

                // 抽取 input 資料
                for (InventoryActionData actionData : inventoryActionData) {
                    if (actionData.getSource().getContainerId() == SOURCE_TYPE_ENCHANT_OUTPUT) {
                        outputActionData = actionData;
                    } else if (actionData.getSource().getContainerId() == SOURCE_TYPE_ENCHANT_INPUT) {
                        inputActionData = actionData;
                    } else {
                        containerActionData = actionData;
                    }
                }

                // 更改資料
                outputActionData.setToItem(inputActionData.getToItem());

                // 重組包
                inventoryActionData = new InventoryActionData[]{outputActionData, containerActionData};
            } else if (inventoryActionData.length == 2) {
                for (InventoryActionData actionData : inventoryActionData) {
                    if (actionData.getSource().getContainerId() == SOURCE_TYPE_ENCHANT_MATERIAL) {
                        actionData.getSource().setContainerId(SOURCE_TYPE_ENCHANT_OUTPUT);
                        actionData.getToItem().setCount(actionData.getToItem().getCount() - actionData.getFromItem().getCount());
                        actionData.setFromItem(ItemStack.getItem(ItemPrototype.AIR));
                        break;
                    }
                }
            }
        }

        InventoryTransactionLevelEvent inventoryTransactionLevelEvent = new InventoryTransactionLevelEvent(player);
        inventoryTransactionLevelEvent.setInventoryActionData(inventoryActionData);

        final InventoryActionData[] afterInventoryActionData = inventoryActionData;
        inventoryTransactionLevelEvent.overrideOnEventCancelled(() -> {
            // 适配假箱子，事件取消时不用缓存，等箱子相关API完善后更新移除
            if (!inventoryTransactionLevelEvent.isNeedRecoveryOnCancelled()) {
                return;
            }

            // 如果取消的话，需要将各个背包的数据还原
            Pair<Class, List<InventoryAction>> result = this.parseInventoryAction(player, afterInventoryActionData);
            if (result == null) {
                // 玩家背包更新
                this.inventoryServiceProxy.notifyPlayerContentChanged(
                        this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER));
                return;
            }
            Class product = result.getKey();
            List<InventoryAction> actions = result.getValue();
            InventoryTransaction transaction = this.constructTransaction(player, actions, product);
            if (transaction == null) {
                return;
            }
            this.clearCache(player);
            transaction.restoreInventoryContent(this.craftService, true);
            // 关闭合成台
            inventoryManager.notifyClientsAllInventoryUiClosed(player);
        });

        inventoryTransactionLevelEvent.overrideAfterEventExecuted(() -> {
            this.postHandleInventoryTransaction(inventoryTransactionLevelEvent.getPlayer(), inventoryTransactionLevelEvent.getInventoryActionData());
        });

        this.eventDispatcher.dispatchEvent(inventoryTransactionLevelEvent);

    }

    /**
     * 处理背包拖动
     *
     * @param player
     * @param inventoryActionData
     */
    private void postHandleInventoryTransaction(Player player, InventoryActionData[] inventoryActionData) {
        Pair<Class, List<InventoryAction>> result = this.parseInventoryAction(player, inventoryActionData);
        if (result == null) {
            return;
        }
        Class product = result.getKey();
        List<InventoryAction> actions = result.getValue();

        // 获取玩家上次的Transaction
        InventoryTransaction transaction = this.allCacheTransactions.remove(player.getClientAddress());
        if (transaction != null) {
            // 当存在缓存时候，同时没过期，会把上次的action累计到本次中
            if (!transaction.isExpired()) {
                // 注意顺序
                transaction.getAllActions().addAll(actions);
                actions = transaction.getAllActions();
                if (transaction instanceof CompoundTransaction && product != CompoundTransaction.class) {
                    product = CompoundTransaction.class;
                } else if (transaction instanceof AnvilOutputTransaction && product != AnvilOutputTransaction.class) {
                    product = AnvilOutputTransaction.class;
                } else if (transaction instanceof EnchantOutputTransaction && product != EnchantOutputTransaction.class) {
                    product = EnchantOutputTransaction.class;
                }
            } else {
                // 当存在transaction过期的时候，首先恢复背包内容
                LOGGER.error("Recovery player {} inventory because of illegal transaction.", player.getIdentifiedStr());
                transaction.restoreInventoryContent(this.craftService, false);
                // 关闭合成台
                inventoryManager.notifyClientsAllInventoryUiClosed(player);
                return;
            }
        }

        this.handleInventoryTransaction(player, product, actions);
    }

    /**
     * 切到主线程中执行
     *
     * @param player
     */
    public void handleCraftTransactionAfterCrafting(Player player) {
        // 先判断
        InventoryTransaction transaction = this.allCacheTransactions.get(player.getClientAddress());
        if (transaction == null) {
            return;
        }
        List<InventoryAction> allActions = transaction.getAllActions();
        boolean isFind = false;
        for (InventoryAction action : allActions) {
            if (action instanceof CompoundOutputAction) {
                isFind = true;
                break;
            }
        }
        if (!isFind) {
            return;
        }
        // 删除
        this.allCacheTransactions.remove(player.getClientAddress());
        this.handleInventoryTransaction(player, transaction.getClass(), allActions);
    }


    /**
     * 收到合成事件后，触发一次合成，解决PC按住shift键，延迟收到craftEvent事件的问题
     *
     * @param player
     */
    private void handleInventoryTransaction(Player player, Class product, List<InventoryAction> actions) {
        List<InventoryAction> beautify = null;
        InventoryTransaction transaction = null;
        boolean isError = false;
        try {
            // 合并冗余Action
            beautify = inventoryActionsPreProcessor.beautify(actions);
            if (DEBUG_LOG) {
                System.out.println("Bealtyfy:");
                for (InventoryAction action : beautify) {
                    System.out.printf(
                            "Item changed at %s:%d, %s:%d -> %s:%d\r\n",
                            action.getInventory() == null ? "null" : action.getInventory().getContainerType(),
                            action.getSlot(),
                            action.getFromItem().getItemType().getName(),
                            action.getFromItem().getCount(),
                            action.getToItem().getItemType().getName(),
                            action.getToItem().getCount());
                }
                System.out.println();
                System.out.println();
            }
        } catch (InventoryTransactionException ite) {
            // 这里只有一个地方会抛异常，不需要打堆栈
            LOGGER.warn("背包物品预处理失败，缓存其action.");
            isError = true;
        }

        // 合并后操作为空，则直接结束
        if (!isError && beautify.isEmpty()) {
            return;
        }

        // 合并结果正常，重新构造Transaction
        if (!isError) {
            transaction = this.constructTransaction(player, beautify, product);
            if (transaction == null) {
                return;
            }

            // 针对特殊玩家记录日志
            if (this.prisonServiceProviderApi.get().isInJail(player)) {
                transaction.logTransaction(player.getIdentifiedStr());
            }

            if (!transaction.checkTransaction()) {
                isError = true;
            }
        }

        // 根据合并结果处理操作
        if (isError && transaction.isFastFailed()) {
            // 当存在transaction过期的时候，首先恢复背包内容
            LOGGER.error("restore player {} inventory because of illegal inventory transaction.", player.getIdentifiedStr());
            transaction.restoreInventoryContent(this.craftService, false);
            // 关闭合成台
            inventoryManager.notifyClientsAllInventoryUiClosed(player);
        } else if (isError) {
            LOGGER.error("{}校验不通过，缓存inventoryAction", transaction.getClass().getSimpleName());
            transaction = this.constructTransaction(player, actions, product);
            this.allCacheTransactions.put(player.getClientAddress(), transaction);
        } else {
            // 合并通过，开始处理操作
            this.HandleRealInventoryTransaction(player, transaction);
        }
    }

    /**
     * 解析
     *
     * @param player
     * @param inventoryActionData
     * @return
     */
    private Pair<Class, List<InventoryAction>> parseInventoryAction(Player player, InventoryActionData[] inventoryActionData) {
        // 是否是物品自动收回
        Class product = InventoryTransaction.class;
        boolean isItemRecycle = false;
        List<InventoryAction> actions = new ArrayList<>(inventoryActionData.length);
        for (InventoryActionData actionData : inventoryActionData) {
            InventorySourceData sourceData = actionData.getSource();
            InventorySourceType sourceType = sourceData.getSourceType();

            // 日志抓到sourceType可能为空，需要进一步检查，这里先行规避
            if (sourceType == null) {
                continue;
            }

            InventoryAction action = null;
            switch (sourceType) {
                case CONTAINER:
                    action = this.parseContainerSource(player, actionData);
                    if (action != null) {
                        actions.add(action);
                    }
                    break;
                case GLOBAL:
                    // 暂无
                    break;
                case WORLD:
                    action = this.parseWorldSource(player, actionData);
                    if (action != null) {
                        actions.add(action);
                    }
                    break;
                case CREATIVE:
                    action = this.parseCreativeSource(player, actionData);
                    if (action != null) {
                        actions.add(action);
                    }
                    break;
                case CRAFTING_GRID:
                case INVALID:
                    int containerId = sourceData.getContainerId();
                    if (containerId == SOURCE_TYPE_CRAFTING_RESULT ||
                            containerId == SOURCE_TYPE_CRAFTING_USE_INGREDIENT) {
                        product = CompoundTransaction.class;
                    } else if (containerId == SOURCE_TYPE_ANVIL_RESULT) {
                        product = AnvilOutputTransaction.class;
                    } else if (containerId == SOURCE_TYPE_ENCHANT_OUTPUT) {
                        product = EnchantOutputTransaction.class;
                    } else if (containerId == SOURCE_TYPE_CONTAINER_DROP_CONTENTS) {
                        isItemRecycle = true;
                    }
                    action = this.parseInvalidSource(player, actionData);
                    if (action != null) {
                        actions.add(action);
                    }
                    break;
            }
        }

        // 这边不处理客户端的物品回收操作
        // 主要针对合成台、铁砧、附魔台
        if (isItemRecycle) {
            return null;
        }
        return new Pair<>(product, actions);
    }

    /**
     * 处理真正的transaction的相关操作
     *
     * @param player
     * @param transaction
     */
    private void HandleRealInventoryTransaction(Player player, InventoryTransaction transaction) {
        // TODO: 2019/6/22 如果处理失败，是否需要刷新背包？
        if (!transaction.execute()) {
            LOGGER.error(String.format("handleTransaction失败，entity[%s]", player));
            ContainerClosePacket containerClosePacket = new ContainerClosePacket();
            containerClosePacket.setContainerId(InventoryConstants.CONTAINER_ID_NONE);
            this.networkManager.sendMessage(player.getClientAddress(), containerClosePacket);
        }
    }

    /**
     * 构造transaction
     *
     * @param player
     * @param actions
     * @param product
     * @return
     */
    private InventoryTransaction constructTransaction(Player player, List<InventoryAction> actions, Class product) {
        if (product == null) {
            product = InventoryTransaction.class;
        }
        try {
            Constructor constructor = product.getConstructor(InventoryAPIProxy.class, InventoryManager.class,
                    Player.class, List.class);
            InventoryTransaction transaction = (InventoryTransaction) constructor.newInstance(this.inventoryServiceProxy,
                    this.inventoryManager, player, actions);
            return transaction;
        } catch (Exception e) {
            LOGGER.error("构造transaction失败", e);
            return null;
        }

    }

    /**
     * 该玩家是否存在transaction缓存
     *
     * @param player
     * @return
     */
    public boolean isExistedCacheTransaction(Player player) {
        if (this.allCacheTransactions.containsKey(player.getClientAddress())) {
            return true;
        }
        return false;
    }

    /**
     * 解析container source
     *
     * @param player
     * @param actionData
     * @return
     */
    private InventoryAction parseContainerSource(Player player, InventoryActionData actionData) {
        InventorySourceData sourceData = actionData.getSource();
        int containerId = sourceData.getContainerId();

        InventoryAction action = null;

        if (containerId == InventoryConstants.CONTAINER_ID_PLAYER_UI) {
            Inventory inventory = null;
            PlayerUIInventorySlot uiInventory = PlayerUIInventorySlot.getUIInventory(actionData.getSlot());
            if (uiInventory == null) {
                return null;
            }
            switch (uiInventory) {
                case PLAYER_CURSOR:
                    actionData.setSlot(0);
                    return new ContainerChangeAction(this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER_UI), actionData);
                case ANVIL_CRAFT_INPUT:
                    actionData.setSlot(actionData.getSlot() - PlayerUIInventorySlot.ANVIL_CRAFT_INPUT.getStartSlot());
                    return new AnvilInputAction(this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ANVIL), actionData);
                case ENCHANT_CRAFT_INPUT:
                    actionData.setSlot(actionData.getSlot() - PlayerUIInventorySlot.ENCHANT_CRAFT_INPUT.getStartSlot());
                    return new EnchantInputAction(this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ENCHANT), actionData, this.enchantmentService);
                case PLAYER_CRAFT_INPUT:
                    actionData.setSlot(actionData.getSlot() - PlayerUIInventorySlot.PLAYER_CRAFT_INPUT.getStartSlot());
                    return new ContainerChangeAction(this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_WORKBENCH), actionData);
                case WORKBENCH_CRAFT_INPUT:
                    actionData.setSlot(actionData.getSlot() - PlayerUIInventorySlot.WORKBENCH_CRAFT_INPUT.getStartSlot());
                    return new ContainerChangeAction(this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_WORKBENCH), actionData);
                case LOOM_CRAFT_INPUT:
                    actionData.setSlot(actionData.getSlot() - PlayerUIInventorySlot.LOOM_CRAFT_INPUT.getStartSlot());
                    return new ContainerChangeAction(this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_WORKBENCH), actionData);
                case CRAFT_OUTPUT:
                    return new PlayerUIOutputAction(inventory, actionData);
            }
        } else {
            Inventory inventory = this.inventoryManager.getInventoryByContainerId(player, containerId);
            if (inventory == null) {
                LOGGER.error(String.format("处理transaction[%s]异常，找不到containerId[%s]的背包", actionData, containerId));
            } else if (inventory.isActive()) {
                action = new ContainerChangeAction(inventory, actionData);
            }
        }

        return action;
    }

    /**
     * 解析world source
     *
     * @param player
     * @param actionData
     * @return
     */
    private InventoryAction parseWorldSource(Player player, InventoryActionData actionData) {
        InventorySourceData sourceData = actionData.getSource();
        InventorySourceFlags flag = sourceData.getBitFlags();
        if (flag != InventorySourceFlags.NO_FLAG) {
            LOGGER.error(String.format("处理transaction[%s]异常， flag[%s]异常", actionData, flag));
            return null;
        }
        InventoryAction action = new WorldInteraction(actionData, this.itemDropService);
        return action;
    }

    /**
     * 解析 creative source
     *
     * @param player
     * @param actionData
     * @return
     */
    private InventoryAction parseCreativeSource(Player player, InventoryActionData actionData) {
        InventoryAction action = new CreativeAction(actionData);
        return action;
    }

    /**
     * 解析服务端与客户端不一致的transaction包
     *
     * @param player
     * @param actionData
     * @return
     */
    private InventoryAction parseInvalidSource(Player player, InventoryActionData actionData) {
        InventorySourceData sourceData = actionData.getSource();
        int containerId = sourceData.getContainerId();
        int slot = actionData.getSlot();
        InventoryAction action = null;
        Inventory inventory = null;
        // 处理合成台相关
        switch (containerId) {
            case SOURCE_TYPE_CRAFTING_ADD_INGREDIENT:
            case SOURCE_TYPE_CRAFTING_REMOVE_INGREDIENT:
                inventory = this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_WORKBENCH);
                if (inventory == null) {
                    LOGGER.error(String.format("处理transaction[%s]失败，inventory[%s]不存在",
                            actionData, InventoryConstants.CONTAINER_ID_WORKBENCH));
                    return null;
                }
                return new ContainerChangeAction(inventory, actionData);
            case SOURCE_TYPE_CRAFTING_RESULT:
                return new CompoundOutputAction(this.craftService, actionData);
            case SOURCE_TYPE_CRAFTING_USE_INGREDIENT:
                return new CompoundInputAction(this.craftService, actionData);
            case SOURCE_TYPE_CONTAINER_DROP_CONTENTS:
                inventory = this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_WORKBENCH);
                if (inventory == null) {
                    LOGGER.error(String.format("处理transaction[%s]失败，inventory[%s]不存在",
                            actionData, InventoryConstants.CONTAINER_ID_WORKBENCH));
                    return null;
                }

                if (slot == -1) {
                    return new WorldInteraction(actionData, this.itemDropService);
                }
                // 当合成台存在物品时候，直接关掉合成台，此时微软版本的slot=0
                return new WorkBenchClearAction(inventory, actionData);
        }
        // 处理铁砧相关
        if (containerId >= SOURCE_TYPE_ANVIL_OUTPUT && containerId <= SOURCE_TYPE_ANVIL_INPUT) {
            inventory = this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ANVIL);
            if (inventory == null || inventory.getContainerType() != ContainerType.ANVIL) {
                LOGGER.error(String.format("处理transaction[%s]失败，inventory[%s]不存在或不合法",
                        actionData, InventoryConstants.CONTAINER_ID_ANVIL));
                return null;
            }
            switch (containerId) {
                case SOURCE_TYPE_ANVIL_INPUT:
                    actionData.setSlot(0);
                    return new AnvilInputAction(inventory, actionData);
                case SOURCE_TYPE_ANVIL_MATERIAL:
                    actionData.setSlot(1);
                    return new AnvilInputAction(inventory, actionData);
                case SOURCE_TYPE_ANVIL_OUTPUT:
                    return null;
                case SOURCE_TYPE_ANVIL_RESULT:
                    actionData.setSlot(2);
                    return new AnvilOutputAction(inventory, actionData, this.anvilService);
            }
        }

        //处理附魔台相关
        if (containerId >= SOURCE_TYPE_ENCHANT_OUTPUT && containerId <= SOURCE_TYPE_ENCHANT_INPUT) {
            inventory = this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ENCHANT);
            if (inventory == null || inventory.getContainerType() != ContainerType.ENCHANT_TABLE) {
                LOGGER.error(String.format("处理transaction[%s]失败，inventory[%s]不存在或不合法",
                        actionData, InventoryConstants.CONTAINER_ID_ENCHANT));
                return null;
            }
            switch (containerId) {
                case SOURCE_TYPE_ENCHANT_INPUT:
                    actionData.setSlot(0);
                    return new EnchantInputAction(inventory, actionData, this.enchantmentService);
                case SOURCE_TYPE_ENCHANT_MATERIAL:
                    actionData.setSlot(1);
                    return new EnchantInputAction(inventory, actionData, this.enchantmentService);
                case SOURCE_TYPE_ENCHANT_OUTPUT:
                    actionData.setSlot(2);
                    return new EnchantOutputAction(inventory, actionData, this.enchantmentService);
            }
            return new ContainerChangeAction(inventory, actionData);
        }
        LOGGER.error(String.format("处理transaction[%s]失败，entity[%s], actionData[%s]", player, actionData));
        return null;
    }

    /**
     * 直接处理inventoryActionData
     *
     * @param player
     * @param inventoryActionDatas
     * @return
     */
    @Deprecated
    public boolean handleDirectInventoryAction(Player player, InventoryActionData[] inventoryActionDatas) {

        if (inventoryActionDatas == null || inventoryActionDatas.length == 0) {
            return true;
        }
        /**
         * 校验
         */
        for (InventoryActionData data : inventoryActionDatas) {
            if (data == null) {
                continue;
            }
            Inventory inventory = this.inventoryManager.getInventoryByContainerId(player, data.getSource().getContainerId());
            ItemStack fromItem = data.getFromItem();
            if (!this.inventoryServiceProxy.getItem(inventory, data.getSlot()).equalsAll(fromItem)) {
                return false;
            }

        }
        /**
         * 设置
         */
        for (InventoryActionData data : inventoryActionDatas) {
            if (data == null) {
                continue;
            }
            Inventory inventory = this.inventoryManager.getInventoryByContainerId(player, data.getSource().getContainerId());
            ItemStack toItem = data.getToItem();
            this.inventoryServiceProxy.setItem(inventory, data.getSlot(), toItem);
        }
        return true;
    }
}
