package com.particle.game.entity.attack;

import com.particle.game.item.ItemAttributeService;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantment;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.item.types.ItemTag;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class EntityDefenseService {

    private Map<ItemPrototype, Float> armorDefenseRecorder = new HashMap<>();

    @Inject
    private void init() {
        // 护甲防御力
        this.armorDefenseRecorder.put(ItemPrototype.LEATHER_HELMET, 1f);
        this.armorDefenseRecorder.put(ItemPrototype.GOLDEN_HELMET, 2f);
        this.armorDefenseRecorder.put(ItemPrototype.CHAINMAIL_HELMET, 2f);
        this.armorDefenseRecorder.put(ItemPrototype.IRON_HELMET, 2f);
        this.armorDefenseRecorder.put(ItemPrototype.TURTLE_SHELL, 2f);
        this.armorDefenseRecorder.put(ItemPrototype.DIAMOND_HELMET, 3f);

        this.armorDefenseRecorder.put(ItemPrototype.LEATHER_CHESTPLATE, 3f);
        this.armorDefenseRecorder.put(ItemPrototype.GOLDEN_CHESTPLATE, 5f);
        this.armorDefenseRecorder.put(ItemPrototype.CHAINMAIL_CHESTPLATE, 5f);
        this.armorDefenseRecorder.put(ItemPrototype.IRON_CHESTPLATE, 6f);
        this.armorDefenseRecorder.put(ItemPrototype.DIAMOND_CHESTPLATE, 8f);

        this.armorDefenseRecorder.put(ItemPrototype.LEATHER_LEGGINGS, 2f);
        this.armorDefenseRecorder.put(ItemPrototype.GOLDEN_LEGGINGS, 3f);
        this.armorDefenseRecorder.put(ItemPrototype.CHAINMAIL_LEGGINGS, 4f);
        this.armorDefenseRecorder.put(ItemPrototype.IRON_LEGGINGS, 5f);
        this.armorDefenseRecorder.put(ItemPrototype.DIAMOND_LEGGINGS, 6f);

        this.armorDefenseRecorder.put(ItemPrototype.LEATHER_BOOTS, 1f);
        this.armorDefenseRecorder.put(ItemPrototype.GOLDEN_BOOTS, 1f);
        this.armorDefenseRecorder.put(ItemPrototype.CHAINMAIL_BOOTS, 1f);
        this.armorDefenseRecorder.put(ItemPrototype.IRON_BOOTS, 2f);
        this.armorDefenseRecorder.put(ItemPrototype.DIAMOND_BOOTS, 3f);
    }

    /**
     * 计算护甲防护能力
     *
     * @param damage
     * @return
     */
    public float caculateArmorDefense(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, float damage) {
        // 防御值
        float defense = 0;
        // 附魔保护
        int epf = 0;
        // 韧性值
        int toughness = 0;

        // 计算头盔防护
        Float helmetDefense = this.armorDefenseRecorder.get(helmet.getItemType());
        if (helmetDefense != null) {
            defense += helmetDefense;
        }
        Enchantment helmetEnchantment = ItemAttributeService.getEnchantment(helmet, Enchantments.PROTECTION);
        if (helmetEnchantment != null) {
            epf += helmetEnchantment.getLevel();
        }
        if (helmet.getItemType().hasTag(ItemTag.MATERIAL_DIAMOND)) {
            toughness += 2;
        }

        // 计算盔甲防护
        Float chestplateDefense = this.armorDefenseRecorder.get(chestplate.getItemType());
        if (chestplateDefense != null) {
            defense += chestplateDefense;
        }
        Enchantment chestplateEnchantment = ItemAttributeService.getEnchantment(chestplate, Enchantments.PROTECTION);
        if (chestplateEnchantment != null) {
            epf += chestplateEnchantment.getLevel();
        }
        if (chestplate.getItemType().hasTag(ItemTag.MATERIAL_DIAMOND)) {
            toughness += 2;
        }

        // 计算裤腿防护
        Float leggingsDefense = this.armorDefenseRecorder.get(leggings.getItemType());
        if (leggingsDefense != null) {
            defense += leggingsDefense;
        }
        Enchantment leggingsEnchantment = ItemAttributeService.getEnchantment(leggings, Enchantments.PROTECTION);
        if (leggingsEnchantment != null) {
            epf += leggingsEnchantment.getLevel();
        }
        if (leggings.getItemType().hasTag(ItemTag.MATERIAL_DIAMOND)) {
            toughness += 2;
        }

        // 计算靴子防护
        Float bootsDefense = this.armorDefenseRecorder.get(boots.getItemType());
        if (bootsDefense != null) {
            defense += bootsDefense;
        }
        Enchantment bootsEnchantment = ItemAttributeService.getEnchantment(boots, Enchantments.PROTECTION);
        if (bootsEnchantment != null) {
            epf += bootsEnchantment.getLevel();
        }
        if (boots.getItemType().hasTag(ItemTag.MATERIAL_DIAMOND)) {
            toughness += 2;
        }

        // 伤害×(1-MIN(20, MAX(防御点数/ 5, 防御点数-伤害/(2+(韧性/4))))/25)
        damage = damage * (1 - Math.min(20, Math.max(defense / 5, defense - damage / (2 + (toughness / 4f)))) / 25);

        // damage = damage * ( 1 - cappedEPF / 25 )
        damage = damage * (1 - epf / 25f);

        return damage;
    }

    /**
     * 计算抛射物防御
     *
     * @param helmet
     * @param chestplate
     * @param leggings
     * @param boots
     * @param damage
     * @return
     */
    public float caculateProjectileDefense(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, float damage) {
        // 最高附魔等级
        int maxDefense = 0;

        // 计算头盔防护
        Enchantment helmetEnchantment = ItemAttributeService.getEnchantment(helmet, Enchantments.PROJECTILE_PROTECTION);
        if (helmetEnchantment != null) {
            if (maxDefense < helmetEnchantment.getLevel()) {
                maxDefense = helmetEnchantment.getLevel();
            }
        }

        // 计算盔甲防护
        Enchantment chestplateEnchantment = ItemAttributeService.getEnchantment(chestplate, Enchantments.PROJECTILE_PROTECTION);
        if (chestplateEnchantment != null) {
            if (maxDefense < chestplateEnchantment.getLevel()) {
                maxDefense = chestplateEnchantment.getLevel();
            }
        }

        // 计算裤腿防护
        Enchantment leggingsEnchantment = ItemAttributeService.getEnchantment(leggings, Enchantments.PROJECTILE_PROTECTION);
        if (leggingsEnchantment != null) {
            if (maxDefense < leggingsEnchantment.getLevel()) {
                maxDefense = leggingsEnchantment.getLevel();
            }
        }

        // 计算靴子防护
        Enchantment bootsEnchantment = ItemAttributeService.getEnchantment(boots, Enchantments.PROJECTILE_PROTECTION);
        if (bootsEnchantment != null) {
            if (maxDefense < bootsEnchantment.getLevel()) {
                maxDefense = bootsEnchantment.getLevel();
            }
        }

        // 计算最终伤害
        damage = (float) (damage * (1 - 0.08 * maxDefense));
        if (damage < 0) {
            damage = 0;
        }

        return damage;
    }
}
