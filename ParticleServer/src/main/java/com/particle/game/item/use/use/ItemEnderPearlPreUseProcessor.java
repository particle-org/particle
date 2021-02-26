package com.particle.game.item.use.use;

import com.particle.api.item.IItemUseProcessor;
import com.particle.game.entity.attack.EntityRemoteAttackService;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemUseInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemEnderPearlPreUseProcessor implements IItemUseProcessor {

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private EntityRemoteAttackService entityRemoteAttackService;

    @Inject
    private PositionService positionService;

    @Inject
    private PlayerService playerService;

    @Override
    public void process(Player player, ItemUseInventoryData itemUseInventoryData, InventoryActionData[] inventoryActionData) {
        // 只消耗手持的物品
        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack enderPearl = this.playerInventoryAPI.getItem(playerInventory, playerInventory.getItemInHandle());

        // 校验物品
        if (enderPearl == null || enderPearl.getItemType() != ItemPrototype.ENDER_PEARL) {
            return;
        }

        // 生存模式则消耗物品
        if (this.playerService.getGameMode(player) == GameMode.SURVIVE) {
            enderPearl.setCount(enderPearl.getCount() - 1);
            if (enderPearl.getCount() > 0) {
                this.playerInventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), enderPearl);
            } else {
                this.playerInventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), ItemStack.getItem(ItemPrototype.AIR));
            }
        }

        //射出抛射物
        this.entityRemoteAttackService.projectileShoot(player, enderPearl, this.positionService.getDirection(player).getDirectionVector().multiply(30).add(0, 1, 0));
    }
}
