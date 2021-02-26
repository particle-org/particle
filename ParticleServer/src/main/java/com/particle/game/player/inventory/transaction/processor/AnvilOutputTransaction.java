package com.particle.game.player.inventory.transaction.processor;

import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.player.inventory.transaction.processor.action.AnvilOutputAction;
import com.particle.game.player.inventory.transaction.processor.action.InventoryAction;
import com.particle.model.inventory.AnvilInventory;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 处理铁砧的输出操作
 */
public class AnvilOutputTransaction extends InventoryTransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnvilOutputTransaction.class);

    public AnvilOutputTransaction(InventoryAPIProxy inventoryServiceProxy,
                                  InventoryManager inventoryManager,
                                  Player player, List<InventoryAction> allActions) {

        this.inventoryServiceProxy = inventoryServiceProxy;
        this.inventoryManager = inventoryManager;
        this.player = player;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.info(this.getClass().getSimpleName());
            for (InventoryAction action : allActions) {
                LOGGER.info((action.getInventory() == null ? "null" : action.getInventory().getName()) + "-" + action.getClass().getSimpleName() + "@" + action.getSlot() + " " + action.getFromItem().toString() + " -> " + action.getToItem().toString());
            }
        }

        // 锁定铁砧的背包，只接受OutputAction的操作
        // 其它Action没有实际的操作意义，且会导致判断流程变复杂
        for (InventoryAction inventoryAction : allActions) {
            if (inventoryAction.getInventory() != null
                    && inventoryAction.getInventory() instanceof AnvilInventory
                    && !(inventoryAction instanceof AnvilOutputAction)) {
                continue;
            }

            this.addAction(inventoryAction);
        }

    }

    @Override
    public boolean isFastFailed() {
        return true;
    }
}
