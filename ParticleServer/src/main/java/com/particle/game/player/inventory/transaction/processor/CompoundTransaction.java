package com.particle.game.player.inventory.transaction.processor;

import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.player.inventory.transaction.processor.action.InventoryAction;
import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.player.Player;

import java.util.List;

/**
 * 处理合成台的输出操作
 */
public class CompoundTransaction extends InventoryTransaction {

    public CompoundTransaction(InventoryAPIProxy inventoryServiceProxy,
                               InventoryManager inventoryManager,
                               Player player, List<InventoryAction> allActions) {
        super(inventoryServiceProxy, inventoryManager, player, allActions);

        Inventory workBench = inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_WORKBENCH);
        if (workBench == null) {
            throw new ProphetException(ErrorCode.CORE_EROOR, "该玩家尚未打开合成台");
        }
    }
}
