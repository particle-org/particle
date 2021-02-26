package com.particle.game.item.release.use;

import com.particle.api.entity.ECSComponentServiceAPI;
import com.particle.api.inventory.InventoryAPI;
import com.particle.api.inventory.InventoryService;
import com.particle.api.item.IItemReleaseProcessor;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.DeputyInventoryAPI;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.component.entity.WeaponCarryModule;
import com.particle.model.inventory.DeputyInventory;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.inventory.data.ItemReleaseInventoryData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class ItemCrossbowPostUseProcessor implements IItemReleaseProcessor {

    private static final ECSModuleHandler<WeaponCarryModule> WEAPON_CARRY_MODULE_HANDLER = ECSModuleHandler.buildHandler(WeaponCarryModule.class);

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private DeputyInventoryAPI deputyInventoryAPI;

    @Inject
    private ECSComponentServiceAPI ecsComponentServiceAPI;

    @Inject
    private InventoryService inventoryService;

    @Inject
    private InventoryAPI inventoryAPI;

    @Override
    public void process(Player player, ItemReleaseInventoryData itemReleaseInventoryData, InventoryActionData[] inventoryActionData) {
        // 消耗物品
        if (itemReleaseInventoryData.getItem().getItemType() == ItemPrototype.CROSSBOW) {
            PlayerInventory playerInventory = (PlayerInventory) inventoryService.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
            ItemStack itemInHand = inventoryAPI.getItem(playerInventory, playerInventory.getItemInHandle());
            if (itemInHand.getNbt() == null) {
                itemInHand.updateNBT(new NBTTagCompound());
            }

            // 檢查副手物品
            DeputyInventory deputyInventory = (DeputyInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_OFFHAND);
            if (deputyInventory != null) {
                // 获取副手物品
                ItemStack itemMap = this.deputyInventoryAPI.getItem(deputyInventory, 0);
                // 火箭
                if (itemMap != null && itemMap.getItemType() == ItemPrototype.FIREWORKS) {
                    itemMap.setCount(itemMap.getCount() - 1);
                    if (itemMap.getCount() > 0) {
                        deputyInventoryAPI.setItem(deputyInventory, 0, itemMap);
                    } else {
                        deputyInventoryAPI.setItem(deputyInventory, 0, ItemStack.getItem(ItemPrototype.AIR));
                    }

                    // 弩更新 nbt
                    NBTTagCompound weaponTag = new NBTTagCompound();
                    weaponTag.setByte("Count", (byte) 1);
                    weaponTag.setShort("Damage", (short) 0);
                    weaponTag.setString("Name", itemMap.getItemType().getName());
                    weaponTag.setTag("tag", itemMap.getNbt() == null ? new NBTTagCompound() : itemMap.getNbt());
                    itemInHand.getNbt().setTag("chargedItem", weaponTag);

                    inventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), itemInHand);

                    WeaponCarryModule weaponCarryModule = WEAPON_CARRY_MODULE_HANDLER.bindModule(player);
                    weaponCarryModule.setWeapon(ItemStack.getItem(ItemPrototype.FIREWORKS));
                    player.setOperationCrossbow(true);
                    inventoryManager.notifyPlayerAllInventoryChanged(player);
                    return;
                }
            }

            // 消耗弓箭
            this.playerInventoryAPI.notifyPlayerContentChanged(playerInventory);
            // 如果没有指定消耗的物品，则不操作
            ItemStack arrow = null;
            int arrowSlot = -1;
            for (Map.Entry<Integer, ItemStack> itemStackEntry : playerInventory.getAllSlots().entrySet()) {
                if (itemStackEntry.getValue().getItemType() == ItemPrototype.ARROW) {
                    arrowSlot = itemStackEntry.getKey();
                    arrow = this.playerInventoryAPI.getItem(playerInventory, arrowSlot);
                    break;
                }
            }
            // 校验物品
            if (arrow == null || arrow.getItemType() != ItemPrototype.ARROW) {
                this.playerInventoryAPI.notifyPlayerContentChanged(playerInventory);
                return;
            }
            arrow.setCount(arrow.getCount() - 1);
            if (arrow.getCount() > 0) {
                this.playerInventoryAPI.setItem(playerInventory, arrowSlot, arrow);
            } else {
                this.playerInventoryAPI.setItem(playerInventory, arrowSlot, ItemStack.getItem(ItemPrototype.AIR));
            }

            // 弩更新 nbt
            NBTTagCompound weaponTag = new NBTTagCompound();
            weaponTag.setByte("Count", (byte) 1);
            weaponTag.setShort("Damage", (short) 0);
            weaponTag.setString("Name", arrow.getItemType().getName());
            itemInHand.getNbt().setTag("chargedItem", weaponTag);
            inventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), itemInHand);

            WeaponCarryModule weaponCarryModule = WEAPON_CARRY_MODULE_HANDLER.bindModule(player);
            weaponCarryModule.setWeapon(ItemStack.getItem(ItemPrototype.ARROW));
            player.setOperationCrossbow(true);
            return;
        }
    }
}
