package com.particle.game.block.enchantment;

import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.item.types.ItemTag;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentsRandom {

    private Map<ItemTag, Map<Enchantments, Integer>> enchantmentsConfig = new HashMap<>();

    public EnchantmentsRandom() {
        Map<Enchantments, Integer> axeEnchantments = new HashMap<>();
        axeEnchantments.put(Enchantments.DURABILITY, 40);
        axeEnchantments.put(Enchantments.EFFICIENCY, 40);
        axeEnchantments.put(Enchantments.FORTUNE, 10);
        axeEnchantments.put(Enchantments.SILK_TOUCH, 10);
        this.enchantmentsConfig.put(ItemTag.TOOL_AXE, axeEnchantments);

        Map<Enchantments, Integer> swordEnchantments = new HashMap<>();
        swordEnchantments.put(Enchantments.DURABILITY, 15);
        swordEnchantments.put(Enchantments.SHARPNESS, 30);
        swordEnchantments.put(Enchantments.BANE_OF_ARTHROPODS, 10);
        swordEnchantments.put(Enchantments.SMITE, 10);
        swordEnchantments.put(Enchantments.LOOTING, 5);
        swordEnchantments.put(Enchantments.FIRE_ASPECT, 15);
        swordEnchantments.put(Enchantments.KNOCKBACK, 15);
        this.enchantmentsConfig.put(ItemTag.TOOL_SWORD, swordEnchantments);

        Map<Enchantments, Integer> pickaxeEnchantments = new HashMap<>();
        pickaxeEnchantments.put(Enchantments.DURABILITY, 40);
        pickaxeEnchantments.put(Enchantments.EFFICIENCY, 50);
        pickaxeEnchantments.put(Enchantments.FORTUNE, 5);
        pickaxeEnchantments.put(Enchantments.SILK_TOUCH, 5);
        this.enchantmentsConfig.put(ItemTag.TOOL_PICKAXE, pickaxeEnchantments);

        Map<Enchantments, Integer> shovelEnchantments = new HashMap<>();
        shovelEnchantments.put(Enchantments.DURABILITY, 40);
        shovelEnchantments.put(Enchantments.EFFICIENCY, 40);
        shovelEnchantments.put(Enchantments.FORTUNE, 10);
        shovelEnchantments.put(Enchantments.SILK_TOUCH, 10);
        this.enchantmentsConfig.put(ItemTag.TOOL_SHOVEL, shovelEnchantments);

        Map<Enchantments, Integer> bowEnchantments = new HashMap<>();
        bowEnchantments.put(Enchantments.DURABILITY, 30);
        bowEnchantments.put(Enchantments.POWER, 30);
        bowEnchantments.put(Enchantments.PUNCH, 15);
        bowEnchantments.put(Enchantments.INFINITY, 10);
        bowEnchantments.put(Enchantments.FLAME, 15);
        this.enchantmentsConfig.put(ItemTag.TOOL_BOW, bowEnchantments);

        Map<Enchantments, Integer> crossbowEnchantments = new HashMap<>();
        crossbowEnchantments.put(Enchantments.DURABILITY, 50);
        crossbowEnchantments.put(Enchantments.PIERCING, 30);
        crossbowEnchantments.put(Enchantments.QUICK_CHARGE, 20);
        this.enchantmentsConfig.put(ItemTag.TOOL_CROSSBOW, crossbowEnchantments);

        Map<Enchantments, Integer> tridentEnchantments = new HashMap<>();
        crossbowEnchantments.put(Enchantments.PIERCING, 60);
        crossbowEnchantments.put(Enchantments.LOYALTY, 40); // 其他的附魔还没完成
        this.enchantmentsConfig.put(ItemTag.TOOL_TRIDENT, crossbowEnchantments);

        Map<Enchantments, Integer> helmetEnchantments = new HashMap<>();
        helmetEnchantments.put(Enchantments.DURABILITY, 40);
        helmetEnchantments.put(Enchantments.AQUA_AFFINITY, 5);
        helmetEnchantments.put(Enchantments.FIRE_PROTECTION, 10);
        helmetEnchantments.put(Enchantments.PROJECTILE_PROTECTION, 10);
        helmetEnchantments.put(Enchantments.PROTECTION, 30);
        helmetEnchantments.put(Enchantments.RESPIRATION, 5);
        this.enchantmentsConfig.put(ItemTag.ARMOR_HELMET, helmetEnchantments);

        Map<Enchantments, Integer> chestplateEnchantments = new HashMap<>();
        chestplateEnchantments.put(Enchantments.DURABILITY, 40);
        chestplateEnchantments.put(Enchantments.FIRE_PROTECTION, 10);
        chestplateEnchantments.put(Enchantments.PROJECTILE_PROTECTION, 10);
        chestplateEnchantments.put(Enchantments.PROTECTION, 30);
        chestplateEnchantments.put(Enchantments.THORNS, 10);
        this.enchantmentsConfig.put(ItemTag.ARMOR_CHESTPLATE, chestplateEnchantments);

        Map<Enchantments, Integer> leggingsEnchantments = new HashMap<>();
        leggingsEnchantments.put(Enchantments.DURABILITY, 40);
        leggingsEnchantments.put(Enchantments.FIRE_PROTECTION, 10);
        leggingsEnchantments.put(Enchantments.PROJECTILE_PROTECTION, 10);
        leggingsEnchantments.put(Enchantments.PROTECTION, 40);
        this.enchantmentsConfig.put(ItemTag.ARMOR_LEGGINGS, leggingsEnchantments);

        Map<Enchantments, Integer> bootsEnchantments = new HashMap<>();
        bootsEnchantments.put(Enchantments.DURABILITY, 35);
        bootsEnchantments.put(Enchantments.DEPTH_STRIDER, 10);
        bootsEnchantments.put(Enchantments.FEATHER_FALLING, 10);
        bootsEnchantments.put(Enchantments.FIRE_PROTECTION, 10);
        bootsEnchantments.put(Enchantments.PROJECTILE_PROTECTION, 10);
        bootsEnchantments.put(Enchantments.PROTECTION, 25);
        this.enchantmentsConfig.put(ItemTag.ARMOR_BOOTS, bootsEnchantments);

        Map<Enchantments, Integer> bookEnchantments = new HashMap<>();
        bookEnchantments.put(Enchantments.DURABILITY, 10);
        bookEnchantments.put(Enchantments.EFFICIENCY, 10);
        bookEnchantments.put(Enchantments.FORTUNE, 2);
        bookEnchantments.put(Enchantments.SILK_TOUCH, 2);
        bookEnchantments.put(Enchantments.SHARPNESS, 5);
        bookEnchantments.put(Enchantments.BANE_OF_ARTHROPODS, 10);
        bookEnchantments.put(Enchantments.SMITE, 10);
        bookEnchantments.put(Enchantments.LOOTING, 2);
        bookEnchantments.put(Enchantments.FIRE_ASPECT, 5);
        bookEnchantments.put(Enchantments.KNOCKBACK, 5);
        bookEnchantments.put(Enchantments.AQUA_AFFINITY, 5);
        bookEnchantments.put(Enchantments.FIRE_PROTECTION, 5);
        bookEnchantments.put(Enchantments.PROTECTION, 10);
        bookEnchantments.put(Enchantments.RESPIRATION, 2);
        bookEnchantments.put(Enchantments.THORNS, 5);
        bookEnchantments.put(Enchantments.DEPTH_STRIDER, 2);
        bookEnchantments.put(Enchantments.FEATHER_FALLING, 10);
        this.enchantmentsConfig.put(ItemTag.ENCHANTMENT_BOOK, bookEnchantments);
    }

    public Enchantments getEnchantment(ItemTag tag) {
        int count = 0;

        Map<Enchantments, Integer> enchantmentsIntegerMap = this.enchantmentsConfig.get(tag);
        for (Map.Entry<Enchantments, Integer> entry : enchantmentsIntegerMap.entrySet()) {
            count += entry.getValue();
        }

        int index = (int) (Math.random() * count);
        for (Map.Entry<Enchantments, Integer> entry : enchantmentsIntegerMap.entrySet()) {
            index -= entry.getValue();

            if (index < 0) {
                return entry.getKey();
            }
        }

        return null;
    }
}
