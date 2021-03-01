package com.particle.game.entity.ai.leaf.config;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.Entity;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ArmorConfig implements IAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArmorConfig.class);

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;
    @Inject
    private InventoryManager inventoryManager;

    private ItemPrototype weapon = ItemPrototype.AIR;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(entity, InventoryConstants.CONTAINER_ID_PLAYER);
        if (inventory != null && this.weapon != ItemPrototype.AIR) {
            this.playerInventoryAPI.setItem(inventory, inventory.getItemInHandle(), ItemStack.getItem(weapon));
        }

        return EStatus.SUCCESS;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {
        if (key.equals("Weapon") && val instanceof String) {
            ItemPrototype item = ItemStack.getItem((String) val).getItemType();
            if (item == null) {
                LOGGER.error("Weapon config fail, {} not exist!", (String) val);
            } else {
                this.weapon = item;
            }
        }
    }
}
