package com.particle.model.events.level.container;

import com.particle.model.events.level.player.LevelPlayerEvent;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.player.Player;

public class InventoryTransactionLevelEvent extends LevelPlayerEvent {

    private InventoryActionData[] inventoryActionData;

    private boolean needRecoveryOnCancelled = true;

    public InventoryTransactionLevelEvent(Player player) {
        super(player);
    }

    public InventoryActionData[] getInventoryActionData() {
        return inventoryActionData;
    }

    public void setInventoryActionData(InventoryActionData[] inventoryActionData) {
        this.inventoryActionData = inventoryActionData;
    }

    public boolean isNeedRecoveryOnCancelled() {
        return needRecoveryOnCancelled;
    }

    public void setNeedRecoveryOnCancelled(boolean needRecoveryOnCancelled) {
        this.needRecoveryOnCancelled = needRecoveryOnCancelled;
    }
}
