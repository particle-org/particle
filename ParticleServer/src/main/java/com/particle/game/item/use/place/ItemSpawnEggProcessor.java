package com.particle.game.item.use.place;

import com.particle.api.item.IItemPlaceProcessor;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.service.MobEntityService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemSpawnEggProcessor implements IItemPlaceProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemSpawnEggProcessor.class);

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PlayerInventoryAPI playerInventoryService;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private MobEntityService mobEntityService;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        ItemStack item = itemUseInventoryData.getItem();

        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);

        Vector3f position = itemUseInventoryData.getPosition().getSide(itemUseInventoryData.getFace()).add(itemUseInventoryData.getClickPosition());

        MobEntity entity = this.mobEntityService.createEntity(item.getMeta(), position);

        // 若没有对应的entity，则重置玩家背包
        if (entity == null) {
            ItemStack itemStack = playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());
            itemStack.setCount(itemStack.getCount());
            playerInventoryService.setItem(playerInventory, playerInventory.getItemInHandle(), itemStack);

            return;
        }

        // spawn生物
        boolean state = this.entitySpawnService.spawnEntity(player.getLevel(), entity);
        // 消耗物品
        if (state) {
            ItemStack itemStack = playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());
            itemStack.setCount(itemStack.getCount() - 1);
            if (itemStack.getCount() <= 0) {
                itemStack = ItemStack.getItem(ItemPrototype.AIR, 0);
            }
            playerInventoryService.setItem(playerInventory, playerInventory.getItemInHandle(), itemStack);
        } else {
            ItemStack itemStack = playerInventoryService.getItem(playerInventory, playerInventory.getItemInHandle());
            itemStack.setCount(itemStack.getCount());
            playerInventoryService.setItem(playerInventory, playerInventory.getItemInHandle(), itemStack);
        }

        // 配置AI
        if (state) {
            entity.getLevel().getLevelSchedule().scheduleSimpleTask("UpdateAI", () -> {
                this.entityDecisionServiceProxy.updateResponse(entity, entity.getActorType());
                this.entityDecisionServiceProxy.updateDecision(entity, entity.getActorType());
            });
        }
    }
}
