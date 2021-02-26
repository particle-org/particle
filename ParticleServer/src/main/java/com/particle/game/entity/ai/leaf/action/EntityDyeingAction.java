package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.DyeColor;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.Player;

import javax.inject.Inject;

public class EntityDyeingAction implements IAction {

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        // 查询交互者
        Entity interactor = this.entityDecisionServiceProxy.getKnowledge(entity, Knowledge.ENTITY_INTERACTOR, Entity.class);
        if (interactor == null) {
            return EStatus.FAILURE;
        }

        // 获取手持物品
        if (interactor instanceof Player) {
            Player player = (Player) interactor;
            PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
            if (inventory != null) {
                ItemStack itemStack = this.playerInventoryAPI.getItem(inventory, inventory.getItemInHandle());
                if (itemStack != null && itemStack.getItemType() == ItemPrototype.DYE) {
                    this.metaDataService.setByteData(entity, EntityMetadataType.COLOR, (byte) DyeColor.getByDyeData(itemStack.getMeta()).getWoolData(), true);

                    return EStatus.SUCCESS;
                } else {
                    return EStatus.FAILURE;
                }
            }
        }


        return null;
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
