package com.particle.game.item;

import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.ArmorInventoryAPI;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.entity.Entity;
import com.particle.model.inventory.ArmorInventory;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantment;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.item.types.ItemTag;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DurabilityService {

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;

    @Inject
    private ArmorInventoryAPI armorInventoryAPI;

    public boolean consumptionItem(Entity entity) {
        // 查询手持物品
        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(entity, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack tools = this.playerInventoryAPI.getItem(playerInventory, playerInventory.getItemInHandle());
        if (!tools.getItemType().hasTag(ItemTag.TOOL)) {
            return false;
        }

        // 魔法物品无视耐久
        if (tools.getItemType().hasTag(ItemTag.MATERIAL_MAGIC)) {
            return false;
        }

        tools = this.calculateToolsConsumption(tools);
        this.playerInventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), tools, true);
        return true;
    }

    public void consumptionEquipments(Entity entity) {
        // 查询装备
        ArmorInventory armorInventory = (ArmorInventory) inventoryManager.getInventoryByContainerId(entity, InventoryConstants.CONTAINER_ID_ARMOR);

        ItemStack helmet = this.armorInventoryAPI.getItem(armorInventory, ArmorInventory.HELMET);
        helmet = this.calculateToolsConsumption(helmet);
        this.armorInventoryAPI.setItem(armorInventory, ArmorInventory.HELMET, helmet, true);

        ItemStack chestplate = this.armorInventoryAPI.getItem(armorInventory, ArmorInventory.CHESTPLATE);
        chestplate = this.calculateToolsConsumption(chestplate);
        this.armorInventoryAPI.setItem(armorInventory, ArmorInventory.CHESTPLATE, chestplate, true);

        ItemStack leggings = this.armorInventoryAPI.getItem(armorInventory, ArmorInventory.LEGGINGS);
        leggings = this.calculateToolsConsumption(leggings);
        this.armorInventoryAPI.setItem(armorInventory, ArmorInventory.LEGGINGS, leggings, true);

        ItemStack boots = this.armorInventoryAPI.getItem(armorInventory, ArmorInventory.BOOTS);
        boots = this.calculateToolsConsumption(boots);
        this.armorInventoryAPI.setItem(armorInventory, ArmorInventory.BOOTS, boots, true);
    }

    private ItemStack calculateToolsConsumption(ItemStack itemStack) {
        // 如果是工具，则消耗耐久
        if (itemStack.getItemType().hasTag(ItemTag.TOOL)) {
            // 消耗耐久的可能性
            double probility = 1d;

            // 耐久附魔加成
            Enchantment enchantment = ItemAttributeService.getEnchantment(itemStack, Enchantments.DURABILITY);
            if (enchantment != null) {
                probility = probility / (1 + enchantment.getLevel());
            }

            if (probility == 1d || Math.random() < probility) {
                int meta = itemStack.getMeta() + 1;
                if (meta >= itemStack.getItemType().getMaxMate()) {
                    return ItemStack.getItem(ItemPrototype.AIR, 0);
                } else {
                    itemStack.setMeta(meta);
                    itemStack.setNbtData("Damage", meta);

                    return itemStack;
                }
            }
        }

        return itemStack;
    }

    public void consumptionFood(Entity entity) {
        // 查询手持物品
        PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(entity, InventoryConstants.CONTAINER_ID_PLAYER);
        ItemStack tools = this.playerInventoryAPI.getItem(playerInventory, playerInventory.getItemInHandle());

        if (!tools.getItemType().hasTag(ItemTag.TOOL)) {
            // 如果不是工具，则消耗数量
            tools.setCount(tools.getCount() - 1);
            if (tools.getCount() == 0) {
                this.playerInventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), ItemStack.getItem(ItemPrototype.AIR, 0), true);
            } else {
                this.playerInventoryAPI.setItem(playerInventory, playerInventory.getItemInHandle(), tools, true);
            }
        }
    }
}
