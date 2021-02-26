package com.particle.game.player.inventory.transaction;

import com.particle.game.player.inventory.transaction.processor.action.ContainerChangeAction;
import com.particle.game.player.inventory.transaction.processor.action.InventoryAction;
import com.particle.model.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class InventoryActionsPreProcessor {
    private static Logger logger = LoggerFactory.getLogger(InventoryActionsPreProcessor.class);

    public List<InventoryAction> beautify(List<InventoryAction> actions) {
        List<ContainerChangeAction> processList = new LinkedList<>();
        List<InventoryAction> skipList = new LinkedList<>();
        //只处理container change action
        for (InventoryAction action : actions) {
            if (action instanceof ContainerChangeAction) {
                processList.add((ContainerChangeAction) action);
            } else {
                skipList.add(action);
            }
        }

        //结果直接包含skip的action
        List<InventoryAction> results = new LinkedList<>(skipList);
        InventoryAction action;
        while ((action = extractAction(processList)) != null) {
            results.add(action);
        }

        return results;
    }

    /**
     * 提取slot上最终action
     * <p>
     * 每次调用从actions中提取一个slot上的action，并移除actions中的action
     * 返回null代表提取完成或出现异常
     *
     * @param actions
     * @return
     */
    private ContainerChangeAction extractAction(List<ContainerChangeAction> actions) {
        if (actions.size() == 0) {
            return null;
        }

        /*
         *  Step 1 : 提取相同格子中的元素
         */
        ContainerChangeAction slotAction = actions.remove(0);

        List<ContainerChangeAction> slotActions = new LinkedList<>();
        slotActions.add(slotAction);

        for (ContainerChangeAction action : actions) {
            if (action.getSlot() == slotAction.getSlot() && action.getInventory() != null &&
                    action.getInventory().getContainerType() == slotAction.getInventory().getContainerType()) {
                slotActions.add(action);
            }
        }

        actions.removeAll(slotActions);

        /*
         *  Step 2 : 消除冗余action
         */
        if (slotActions.size() > 2) {
            List<ContainerChangeAction> waitToCompare = new LinkedList<>();

            //提取from数量不为0的action
            for (ContainerChangeAction action : slotActions) {
                if (action.getFromItem().getCount() != 0) {
                    waitToCompare.add(action);
                }
            }

            for (ContainerChangeAction action : waitToCompare) {
                removePair(slotActions, action);
            }
        }

        /*
         *  Step 3 : 处理最终结果
         *
         *  Case 1 : size == 2
         *    这时只剩下首尾action，直接合并导出
         *  Case 2 : size == 1
         *    这个操作本身就是从0变化或变为0，不需要合并，直接返回
         *  Case 3 : size = 0
         *    该slot所有的操作都是冗余操作，直接主动递归下去
         */
        if (slotActions.size() == 2) {
            ContainerChangeAction fromAction = slotActions.remove(0);
            ContainerChangeAction toAction = slotActions.remove(0);

            //检查是否需要调换顺序
            if (fromAction.getFromItem().getCount() == 0) {
                ContainerChangeAction tempAction = fromAction;
                fromAction = toAction;
                toAction = tempAction;
            }

            ContainerChangeAction resultAction = fromAction.shadowCopy();
            resultAction.setToItem(toAction.getToItem());

            return resultAction;
        } else if (slotActions.size() == 1) {
            return slotActions.get(0);
        } else if (slotActions.size() == 0) {
            return extractAction(actions);
        } else {
            logger.error("Fail to extract action at slot {}", slotAction.getSlot());
            throw new InventoryTransactionException(ErrorCode.CORE_EROOR, "物品转换过程中拖动错误，预处理后单槽多于两个以上转换！");
        }
    }

    /**
     * 消除一对冗余操作
     * (冗余操作指同在一个格子上放置并移除相同数量的物品)
     *
     * @param slotActions
     */
    private void removePair(List<ContainerChangeAction> slotActions, ContainerChangeAction action) {
        for (int i = 0; i < slotActions.size(); i++) {
            if (slotActions.get(i).getToItem().equalsWithCounts(action.getFromItem())) {
                slotActions.remove(i);
                slotActions.remove(action);
                return;
            }
        }
    }
}
