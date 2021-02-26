package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.Entity;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

import javax.inject.Inject;

public class EntityTargetItemInHandCheck implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private MovementServiceProxy movementServiceProxy;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Integer itemID = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ITEM_IN_HAND_CACHE, Integer.class);
        Player player = entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Player.class);

        if (itemID != null) {
            PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
            if (inventory != null) {
                ItemStack itemStack = this.playerInventoryAPI.getItem(inventory, inventory.getItemInHandle());
                if (itemStack != null && itemStack.getItemType().getId() == itemID) {
                    movementServiceProxy.setRunning(entity, true);
                    return EStatus.SUCCESS;
                } else {
                    entityDecisionServiceProxy.removeKnowledge(entity, Knowledge.ITEM_IN_HAND_CACHE);
                }
            }
        }

        movementServiceProxy.setRunning(entity, false);
        return EStatus.FAILURE;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {
    }
}
