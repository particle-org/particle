package com.particle.game.item.use.use;

import com.particle.api.item.IItemUseProcessor;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.world.map.MapGenerateService;
import com.particle.model.events.level.player.PlayerCreateMapEvent;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemMapPreUseProcessor implements IItemUseProcessor {

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Inject
    private InventoryManager inventoryManager;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private MapGenerateService mapGenerateService;

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        // 只消耗手持的物品
        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack emptyMap = this.playerInventoryAPI.getItem(playerInventory, playerInventory.getItemInHandle());

        // 校验物品
        if (emptyMap == null || emptyMap.getItemType() != ItemPrototype.EMPTYMAP) {
            return;
        }

        // 检查背包剩余空间
        if (this.playerInventoryAPI.firstEmpty(playerInventory) == -1) {
            return;
        }

        // 消耗物品
        emptyMap.setCount(emptyMap.getCount() - 1);
        if (emptyMap.getCount() > 0) {
            this.playerInventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), emptyMap);
        } else {
            this.playerInventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), ItemStack.getItem(ItemPrototype.AIR));
        }

        // 发放地图
        ItemStack itemMap = this.mapGenerateService.generateSingleContentMap(emptyMap.getMeta() == 2, "新生存家园地图");
        PlayerCreateMapEvent playerCreateMapEvent = new PlayerCreateMapEvent(player, itemMap, emptyMap);
        this.eventDispatcher.dispatchEvent(playerCreateMapEvent);

        this.playerInventoryAPI.addItem(playerInventory, itemMap);
        this.playerInventoryAPI.notifyPlayerContentChanged(playerInventory);
    }
}
