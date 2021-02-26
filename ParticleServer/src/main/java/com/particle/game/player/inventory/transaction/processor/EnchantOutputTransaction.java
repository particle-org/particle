package com.particle.game.player.inventory.transaction.processor;

import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.player.inventory.transaction.processor.action.InventoryAction;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 处理附魔台的transaction
 */
public class EnchantOutputTransaction extends InventoryTransaction {

    private static final Logger logger = LoggerFactory.getLogger(EnchantOutputTransaction.class);

    public EnchantOutputTransaction(InventoryAPIProxy inventoryServiceProxy,
                                    InventoryManager inventoryManager,
                                    Player player, List<InventoryAction> allActions) {
        super(inventoryServiceProxy, inventoryManager, player, allActions);
    }
}
