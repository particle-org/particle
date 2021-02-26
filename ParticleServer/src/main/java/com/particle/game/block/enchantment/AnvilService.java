package com.particle.game.block.enchantment;

import com.particle.game.entity.attribute.explevel.ExperienceService;
import com.particle.game.item.ItemAttributeService;
import com.particle.game.player.PlayerService;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.AnvilInventory;
import com.particle.model.inventory.Inventory;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantment;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.GameMode;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class AnvilService {

    @Inject
    private ExperienceService experienceService;

    @Inject
    private PlayerService playerService;

    @Inject
    private InventoryAPIProxy inventoryAPIProxy;

    public ItemStack processAnvilRepaire(Player player, Inventory anvilInventory) {
        // 校验是否有经验消耗
        if (this.playerService.getGameMode(player) == GameMode.SURVIVE) {
            if (this.experienceService.confirmLevelSpaend(player) <= 0) {
                return null;
            }
        }

        // 原料
        ItemStack source = inventoryAPIProxy.getItem(anvilInventory, AnvilInventory.TARGET);

        // 产物
        ItemStack createItem = source.clone();

        // 校验增强的物品
        ItemStack enchantItem = inventoryAPIProxy.getItem(anvilInventory, AnvilInventory.SACRIFICE);
        // 物品不一致特殊处理
        if (enchantItem.getItemType() != source.getItemType()) {
            if (enchantItem.getItemType() != ItemPrototype.AIR
                    && enchantItem.getItemType() != ItemPrototype.ENCHANTED_BOOK
                    && enchantItem.getItemType() != ItemPrototype.IRON_INGOT
                    && enchantItem.getItemType() != ItemPrototype.GOLD_INGOT
                    && enchantItem.getItemType() != ItemPrototype.DIAMOND) {
                return null;
            }
        }


        // 处理附魔
        List<Enchantment> enchantments = new LinkedList<>(createItem.getEnchantments());
        for (Enchantment newEnchant : enchantItem.getEnchantments()) {
            boolean matched = false;
            for (Enchantment savedEnchant : enchantments) {
                // 如果有相同的附魔，则计算是否升级
                if (savedEnchant.getType().getId() == newEnchant.getType().getId()) {
                    if (savedEnchant.getLevel() == newEnchant.getLevel()) {
                        // 相同等级，则升级
                        ItemAttributeService.addEnchantments(createItem, new Enchantment(savedEnchant.getType(), (short) (savedEnchant.getLevel() + 1)));
                    } else {
                        // 不同等级，则取更大的等级
                        ItemAttributeService.addEnchantments(createItem, new Enchantment(savedEnchant.getType(), (short) Math.max(savedEnchant.getLevel(), newEnchant.getLevel())));
                    }

                    // 标记匹配成功
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                // 如果没有匹配成功，则作为新附魔加入

                // 特殊处理一下精准和时运冲突问题
                if (newEnchant.getType() == Enchantments.FORTUNE && ItemAttributeService.hasEnchantment(createItem, Enchantments.SILK_TOUCH)) {
                    // 源物品有精准，新物品有时运，则移除时运
                    continue;
                } else if (newEnchant.getType() == Enchantments.SILK_TOUCH && ItemAttributeService.hasEnchantment(createItem, Enchantments.FORTUNE)) {
                    // 源物品有时运，新物品有精准，则移除精准
                    continue;
                } else {
                    ItemAttributeService.addEnchantments(createItem, newEnchant);
                }
            }
        }
        return createItem;
    }

}
