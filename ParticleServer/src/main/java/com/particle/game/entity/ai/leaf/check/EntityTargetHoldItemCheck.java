package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
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
import java.util.ArrayList;
import java.util.List;

public class EntityTargetHoldItemCheck implements ICondition {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityTargetHoldItemCheck.class);

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Inject
    private InventoryManager inventoryManager;

    private List<ItemPrototype> itemStackTempList = new ArrayList<>();
    private List<ItemPrototype> itemStackList = new ArrayList<>();

    @Override
    public void onInitialize() {
        itemStackList.clear();
        for (ItemPrototype itemPrototype : itemStackTempList) {
            itemStackList.add(itemPrototype);
        }
        itemStackTempList.clear();
    }

    @Override
    public EStatus tick(Entity entity) {
        Entity entityTarget = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_TARGET, Entity.class);

        if (entityTarget != null) {
            // 获取武器
            ItemStack weapon = null;
            PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(entityTarget, InventoryConstants.CONTAINER_ID_PLAYER);
            if (inventory != null) {
                weapon = this.playerInventoryAPI.getItem(inventory, inventory.getItemInHandle());
            }

            if (weapon != null && itemStackList.contains(weapon.getItemType())) {
                return EStatus.SUCCESS;
            }
        }


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
        if (key.equalsIgnoreCase("Weapon") && val instanceof String) {
            ItemStack item = ItemStack.getItem((String) val);
            if (item == null) {
                LOGGER.error("Weapon config fail, {} not exist!", (String) val);
            } else {
                itemStackTempList.add(item.getItemType());
            }
        }
    }
}
