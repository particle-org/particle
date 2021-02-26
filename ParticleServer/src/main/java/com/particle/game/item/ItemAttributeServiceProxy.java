package com.particle.game.item;

import com.google.inject.Singleton;
import com.particle.api.item.IItemAttributeServiceApi;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantment;
import com.particle.model.item.enchantment.IEnchantmentType;
import com.particle.model.item.lore.Lore;

import java.util.List;

@Singleton
public class ItemAttributeServiceProxy implements IItemAttributeServiceApi {

    /**
     * 设置物品lore
     *
     * @param lore
     */
    @Override
    public void setLore(ItemStack itemStack, String... lore) {
        ItemAttributeService.setLore(itemStack, new Lore(lore));
    }

    @Override
    public void enableItemState(ItemStack itemStack, String stateId) {
        ItemAttributeService.addBindState(itemStack, stateId);
    }

    @Override
    public void clearItemState(ItemStack itemStack) {
        ItemAttributeService.clearBindState(itemStack);
    }

    @Override
    public void addEnchantments(ItemStack itemStack, Enchantment enchantment) {
        ItemAttributeService.addEnchantments(itemStack, enchantment);
    }

    @Override
    public void addEnchantments(ItemStack itemStack, List<Enchantment> enchantments) {
        ItemAttributeService.addEnchantments(itemStack, enchantments);
    }

    @Override
    public boolean hasEnchantment(ItemStack itemStack, IEnchantmentType enchantmentType) {
        return ItemAttributeService.hasEnchantment(itemStack, enchantmentType);
    }

    @Override
    public Enchantment getEnchantment(ItemStack itemStack, IEnchantmentType enchantmentType) {
        return ItemAttributeService.getEnchantment(itemStack, enchantmentType);
    }

    @Override
    public void removeEnchantments(ItemStack itemStack, IEnchantmentType type) {
        ItemAttributeService.removeEnchantments(itemStack, type);
    }

    @Override
    public void removeEnchantments(ItemStack itemStack) {
        ItemAttributeService.removeEnchantments(itemStack);
    }

    @Override
    public String getDisplayName(ItemStack itemStack) {
        return ItemAttributeService.getDisplayName(itemStack);
    }

    @Override
    public void setDisplayName(ItemStack itemStack, String name) {
        ItemAttributeService.setDisplayName(itemStack, name);
    }
}
