package com.particle.game.player.inventory.transaction.processor;

import com.particle.game.player.craft.CraftService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.modules.PlayerUIInventoryModule;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.player.inventory.transaction.processor.action.CompoundOutputAction;
import com.particle.game.player.inventory.transaction.processor.action.ContainerChangeAction;
import com.particle.game.player.inventory.transaction.processor.action.InventoryAction;
import com.particle.game.player.inventory.transaction.processor.action.PlayerUIOutputAction;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.ContainerType;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.Player;
import com.particle.model.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class InventoryTransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryTransaction.class);

    /**
     * transaction缓存的最大间隔时间是500毫秒
     */
    private static final long MAX_INTERVALS = 500;

    /**
     * transaction缓存的最大堆叠数
     */
    private static final long MAX_STACK_COUNTS = 2;

    protected Player player;

    private boolean hasEnchant = false;

    private boolean hasFurnace = false;

    private boolean hasAnvil = false;

    protected InventoryAPIProxy inventoryServiceProxy;

    protected InventoryManager inventoryManager;

    protected List<InventoryAction> allActions = new LinkedList<>();

    /**
     * 时间戳
     */
    private long timeStamp = System.currentTimeMillis();

    /**
     * transaction堆积次数
     * 表示几个transaction合并成此transaction
     */
    private int stackCounts = 0;

    private Set<Inventory> allPrintInventories = new HashSet<>();

    public InventoryTransaction() {

    }

    public InventoryTransaction(InventoryAPIProxy inventoryServiceProxy, InventoryManager inventoryManager,
                                Player player, List<InventoryAction> allActions) {
        this.inventoryServiceProxy = inventoryServiceProxy;
        this.inventoryManager = inventoryManager;
        this.player = player;
        for (InventoryAction inventoryAction : allActions) {
            this.addAction(inventoryAction);
        }
    }

    public void logTransaction(String playerId) {
    }

    public List<InventoryAction> getAllActions() {
        return allActions;
    }

    /**
     * 是否过期
     *
     * @return
     */
    public boolean isExpired() {
        long now = System.currentTimeMillis();
        if (now - this.timeStamp > MAX_INTERVALS) {
            if (LOGGER.isDebugEnabled()) {
                this.printInfos(0);
            }
            return true;
        }
        if (this.stackCounts >= MAX_STACK_COUNTS) {
            if (LOGGER.isDebugEnabled()) {
                this.printInfos(1);
            }
            return true;
        }
        return false;
    }

    /**
     * 打印
     *
     * @param errorType
     */
    private void printInfos(int errorType) {
        LOGGER.info(String.format("entity:%s, errorType:%s", player.getIdentifiedStr(), errorType));
        for (InventoryAction action : this.allActions) {
            Inventory inventory = action.getInventory();
            if (inventory != null) {
                Map<Integer, ItemStack> allSlots = inventory.getAllSlots();
                StringBuilder sb = new StringBuilder();
                sb.append("transaction: ").append(this).append(", ");
                sb.append("action:").append(action.getClass());
                sb.append(", inventory:").append(inventory.getName()).append(": ");
                sb.append("{fromItem=").append(action.getFromItem()).append(", toItem=").append(action.getToItem()).append(", slot=").append(action.getSlot());
                LOGGER.debug(sb.toString());
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("action:").append(action.getClass());
                sb.append(", inventory:null").append(": ");
                sb.append("{fromItem=").append(action.getFromItem()).append(", toItem=").append(action.getToItem()).append(", slot=").append(action.getSlot());
                LOGGER.info(sb.toString());
            }
        }
    }

    /**
     * 添加action
     *
     * @param actions
     */
    public void addAction(List<InventoryAction> actions) {
        if (actions == null || actions.isEmpty()) {
            return;
        }
        for (InventoryAction action : actions) {
            this.addAction(action);
        }
    }

    /**
     * 添加action
     *
     * @param action
     */
    public void addAction(InventoryAction action) {
        this.diffAction(action);
        Inventory inventory = action.getInventory();
        if (inventory != null && !this.allPrintInventories.contains(inventory)) {
            this.allPrintInventories.add(inventory);
        }

        this.allActions.add(action);
    }

    /**
     * 区分action
     *
     * @param action
     */
    private void diffAction(InventoryAction action) {
        if (action instanceof ContainerChangeAction) {
            ContainerChangeAction changeAction = (ContainerChangeAction) action;
            Inventory inventory = changeAction.getInventory();
            if (inventory.getContainerType() == ContainerType.ENCHANT_TABLE) {
                this.hasEnchant = true;
            } else if (inventory.getContainerType() == ContainerType.FURNACE) {
                this.hasFurnace = true;
            } else if (inventory.getContainerType() == ContainerType.ANVIL) {
                this.hasAnvil = true;
            }
        }
    }

    /**
     * 执行方法
     *
     * @return
     */
    public boolean execute() {
        // 数据同步，对于物品上绑定的特殊数据，保证移动后不丢失
        for (InventoryAction action : this.allActions) {
            if (action.execute(this.inventoryManager, this.inventoryServiceProxy, player)) {
                action.onSuccess(this.inventoryManager, this.inventoryServiceProxy, player);
            } else {
                action.onFailed(this.inventoryManager, this.inventoryServiceProxy, player);
            }

        }

        return true;
    }

    /**
     * 恢复玩家附属背包
     *
     * @param allClear false (玩家背包、装备、副手、合成台、末影箱、单格、铁砧、附魔台)
     */
    public void restoreInventoryContent(CraftService craftService, boolean allClear) {
        LOGGER.debug("移动错误，服务端主动恢复玩家附属背包的数据...");
        Set<Inventory> inventories = new HashSet<>();
        boolean isFindCompoundOutput = false;
        for (InventoryAction action : this.allActions) {
            if (action instanceof ContainerChangeAction) {
                Inventory inventory = action.getInventory();
                if (inventories.contains(inventory)) {
                    continue;
                }
                inventories.add(inventory);
                if (allClear || inventory.getContainerType().isPlayerOwned()) {
                    this.inventoryServiceProxy.notifyPlayerContentChanged(inventory);
                } else {
                    LOGGER.warn("玩家【{}】操作失败，未回滚其背包内容", player.getIdentifiedStr());
                }

            } else if (action instanceof CompoundOutputAction) {
                isFindCompoundOutput = true;
            }
        }

        // 如果存在合成台的输出被出错，需要回滚，将输入恢复到背包中。
        if (isFindCompoundOutput) {
            craftService.recycleCompoundInput(player);
        }
    }

    /**
     * 检测该transaction是否合法
     *
     * @return
     */
    public boolean checkTransaction() {
        // 合成操作,保证对单个格子的一系列操作正确，且去除冗余操作
        List<InventoryAction> newAction = this.squashDuplicateSlotChanges();
        if (newAction == null || newAction.isEmpty()) {
            return false;
        }

        // 预校验
        List<PlayerUIOutputAction> playerUIOutputActions = new ArrayList<>();
        for (InventoryAction inventoryAction : newAction) {
            if (inventoryAction instanceof PlayerUIOutputAction) {
                playerUIOutputActions.add((PlayerUIOutputAction) inventoryAction);
            } else {
                if (!inventoryAction.isValid(this.inventoryManager, this.inventoryServiceProxy, player)) {
                    return false;
                }
            }
        }

        // 针对合成台特殊处理
        if (playerUIOutputActions.size() > 1) {
            // 解决pc shift的合成台拼包后无法解析的问题
            PlayerUIInventoryModule playerUIInventoryModule = new PlayerUIInventoryModule();
            for (PlayerUIOutputAction playerUIOutputAction : playerUIOutputActions) {
                // 预先执行
                playerUIOutputAction.setPlayerUIInventoryModule(playerUIInventoryModule);
                if (!playerUIOutputAction.isValid(this.inventoryManager, this.inventoryServiceProxy, player)) {
                    return false;
                } else {
                    playerUIOutputAction.execute(this.inventoryManager, this.inventoryServiceProxy, player);
                }
                playerUIOutputAction.setPlayerUIInventoryModule(null);


            }
        } else if (playerUIOutputActions.size() == 1) {
            if (!playerUIOutputActions.get(0).isValid(this.inventoryManager, this.inventoryServiceProxy, player)) {
                return false;
            }
        }

        // 防刷校验，保证所有的物品只是移动位置，不会凭空增加
        boolean matchState = this.matchItems(newAction);
        if (!matchState) {
            return false;
        }

        return true;
    }

    /**
     * 比较转换过程中数量是否一致
     */
    protected boolean matchItems(List<InventoryAction> newAction) {
        // 缓存action列表，用于匹配
        List<ItemStack> fromItems = new ArrayList<>();
        List<ItemStack> toItems = new ArrayList<>();
        for (InventoryAction inventoryAction : newAction) {
            if (inventoryAction.getToItem().getItemType().getId() != ItemPrototype.AIR.getId()) {
                toItems.add(inventoryAction.getToItem());
            }
            if (inventoryAction.getFromItem().getItemType().getId() != ItemPrototype.AIR.getId()) {
                fromItems.add(inventoryAction.getFromItem());
            }
        }

        Iterator<ItemStack> fromIterators = fromItems.iterator();
        while (fromIterators.hasNext()) {
            ItemStack fromItem = fromIterators.next();
            Iterator<ItemStack> toIterators = toItems.iterator();
            while (toIterators.hasNext()) {
                ItemStack toItem = toIterators.next();
                if (fromItem.equalsAll(toItem)) {
                    int amount = Math.min(fromItem.getCount(), toItem.getCount());
                    fromItem.setCount(fromItem.getCount() - amount);
                    toItem.setCount(toItem.getCount() - amount);
                    if (toItem.getCount() == 0) {
                        toIterators.remove();
                    }
                    if (fromItem.getCount() == 0) {
                        fromIterators.remove();
                        break;
                    }

                }
            }
        }

        return fromItems.isEmpty() && toItems.isEmpty();
    }

    /**
     * 合并transaction操作
     *
     * @return
     */
    protected List<InventoryAction> squashDuplicateSlotChanges() {
        // 按找格子分组action
        List<InventoryAction> newActions = new LinkedList<>();
        Map<Integer, List<ContainerChangeAction>> slotChanges = this.filterContainerChangeAction(newActions);

        // 迭代所有的格子
        for (Map.Entry<Integer, List<ContainerChangeAction>> entry : slotChanges.entrySet()) {
            // 针对没有或只有一次操作的，直接跳过
            List<ContainerChangeAction> originActions = entry.getValue();
            if (originActions.size() == 1) {
                InventoryAction iAction = originActions.get(0);
                newActions.add(iAction);
                continue;
            } else if (originActions.size() == 0) {
                continue;
            }

            // 查找到源Action
            List<ContainerChangeAction> handleActions = new ArrayList<>(originActions);
            ContainerChangeAction originAction = this.findFirstOriginChangeAction(handleActions);
            if (originAction == null) {
                LOGGER.error("没法找到originAction");
                return null;
            }

            // 迭代搜索，确定所有的Action能拼接在一起（这部分代码能优化一下效率）
            ItemStack lastItem = originAction.getToItem();
            boolean canLoopSearch;
            do {
                Pair<Boolean, ItemStack> pairResult = this.canLoopSearch(handleActions, lastItem, true);
                canLoopSearch = pairResult.getKey();
                if (!canLoopSearch) {
                    pairResult = this.canLoopSearch(handleActions, lastItem, false);
                    canLoopSearch = pairResult.getKey();
                }
                if (canLoopSearch) {
                    lastItem = pairResult.getValue();
                }
            } while (canLoopSearch);

            if (!handleActions.isEmpty()) {
                LOGGER.error(String.format("合并错误，originActions的大小：%s，玩家名称：%s", originActions.size(), player.getIdentifiedStr()));
                return null;
            }

            // 缓存最终结果
            originAction.setToItem(lastItem);
            newActions.add(originAction);
        }
        return newActions;
    }

    /**
     * 查找originAction
     * 先根据背包内容查找，若没有找到，则寻找第一个非空
     *
     * @param handleActions
     * @return
     */
    private ContainerChangeAction findFirstOriginChangeAction(List<ContainerChangeAction> handleActions) {
        ContainerChangeAction originAction = null;
        int slot = handleActions.get(0).getSlot();
        ItemStack fromItem = this.inventoryServiceProxy.getItem(handleActions.get(0).getInventory(), slot);

        // 查找是否有action的fromItem与背包中对应格子存储的数据一致
        for (int i = 0; i < handleActions.size(); i++) {
            ContainerChangeAction cAction = handleActions.get(i);
            if (cAction.getFromItem().equalsWithCounts(fromItem)) {
                originAction = cAction;
                handleActions.remove(i);
                break;
            }
        }

        // 附魔、铁砧等场景凭空构造出的新的Action
        if (originAction == null) {
            for (int i = 0; i < handleActions.size(); i++) {
                ContainerChangeAction cAction = handleActions.get(i);
                if (cAction.getFromItem().getItemType() != ItemPrototype.AIR) {
                    originAction = cAction;
                    handleActions.remove(i);
                    break;
                }
            }
        }
        return originAction;
    }

    /**
     * 判断是否允许loopSearch
     *
     * @param handleActions
     * @param lastItem
     * @param isExacted
     * @return
     */
    private Pair<Boolean, ItemStack> canLoopSearch(List<ContainerChangeAction> handleActions, ItemStack lastItem, boolean isExacted) {
        boolean canLoopSearch = false;
        if (isExacted) {
            for (int index = 0; index < handleActions.size(); index++) {
                ContainerChangeAction cAction = handleActions.get(index);
                ItemStack ffItem = cAction.getFromItem();
                if (ffItem.equalsWithCounts(lastItem)) {
                    lastItem = cAction.getToItem();
                    handleActions.remove(index);
                    canLoopSearch = true;
                    break;
                }
            }
        } else {
            for (int index = 0; index < handleActions.size(); index++) {
                ContainerChangeAction cAction = handleActions.get(index);
                ItemStack cfItem = cAction.getFromItem();
                ItemStack ctItem = cAction.getToItem();
                if (cfItem.equalsAll(lastItem)) {
                    // 解决：
                    // 原始数据30-24,16-5，8-0
                    // 变成30-13， 8-0
                    // 变成30-5
                    lastItem.setCount(lastItem.getCount() -
                            cfItem.getCount() + ctItem.getCount());
                    handleActions.remove(index);
                    canLoopSearch = true;
                    break;
                }
            }
        }
        return new Pair<>(canLoopSearch, lastItem);
    }

    /**
     * 对于附魔台，错误action删除
     *
     * @return 返回true，表示有错误，并删除
     */
    @Deprecated
    private boolean tryFixEnchantingTransaction() {
        boolean isError = false;
        Iterator<InventoryAction> actionIterator = this.allActions.iterator();
        while (actionIterator.hasNext()) {
            InventoryAction action = actionIterator.next();
            if (action instanceof ContainerChangeAction && action.getSlot() == -1) {
                actionIterator.remove();
                isError = true;
            }
        }
        return isError;
    }

    /**
     * 过滤该transaction过程中，对于同一个槽操作的action
     *
     * @return
     */
    protected Map<Integer, List<ContainerChangeAction>> filterContainerChangeAction(List<InventoryAction> newActions) {
        Map<Integer, List<ContainerChangeAction>> slotChanges = new HashMap<>();
        for (InventoryAction action : this.allActions) {
            if (action instanceof ContainerChangeAction) {
                ContainerChangeAction changeAction = (ContainerChangeAction) action;
                int hash = Objects.hash(changeAction.getInventory(), changeAction.getSlot());
                List<ContainerChangeAction> list = slotChanges.get(hash);
                if (list == null) {
                    list = new ArrayList<>();
                    slotChanges.put(hash, list);
                }
                list.add(changeAction);
            } else {
                // 对于不是ContainerChangeAction，fromItem跟toItem的物品属性和数量一致，也需要处理
                newActions.add(action);
            }
        }
        return slotChanges;
    }

    /**
     * 是否是立即失败
     *
     * @return
     */
    public boolean isFastFailed() {
        return false;
    }


}
