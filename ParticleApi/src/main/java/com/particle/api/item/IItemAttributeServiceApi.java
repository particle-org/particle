package com.particle.api.item;

import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantment;
import com.particle.model.item.enchantment.IEnchantmentType;

import java.util.List;

public interface IItemAttributeServiceApi {

    void setLore(ItemStack itemStack, String... lore);

    void enableItemState(ItemStack itemStack, String stateId);

    void clearItemState(ItemStack itemStack);

    void addEnchantments(ItemStack itemStack, Enchantment enchantment);

    void addEnchantments(ItemStack itemStack, List<Enchantment> enchantments);

    boolean hasEnchantment(ItemStack itemStack, IEnchantmentType enchantmentType);

    Enchantment getEnchantment(ItemStack itemStack, IEnchantmentType enchantmentType);

    void removeEnchantments(ItemStack itemStack, IEnchantmentType type);

    void removeEnchantments(ItemStack itemStack);

    String getDisplayName(ItemStack itemStack);

    void setDisplayName(ItemStack itemStack, String name);
}
