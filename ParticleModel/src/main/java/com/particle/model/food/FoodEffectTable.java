package com.particle.model.food;

import com.particle.model.item.types.ItemPrototype;

import java.util.ArrayList;
import java.util.List;

public class FoodEffectTable {

    private static List<FoodEffectTable> foodEffectTables = new ArrayList<>();

    private static FoodEffectTable[] foodEffectTableDict = new FoodEffectTable[512];

    static {
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.APPLE, 4, 2.4f, 0.6f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.BAKED_POTATO, 5, 6, 1.2f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.BEETROOT, 1, 1.2f, 1.2f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.BEETROOT_SOUP, 6, 7.2f, 1.2f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.BREAD, 5, 6, 1.2f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.CAKE, 2, 0.4f, 0.2f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.CARROT, 3, 3.6F, 1.2f));
        // 紫颂果具有瞬移的效果
        FoodEffectTable foodEffectTable = new FoodEffectTable(ItemPrototype.CHORUS_FRUIT, 4, 2.4F, 0.6f);
        foodEffectTables.add(foodEffectTable);
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.COOKED_CHICKEN, 6, 7.2F, 1.2f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.COOKED_FISH, 5, 6, 1.2f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.MUTTONCOOKED, 6, 9.6F, 1.6f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.COOKED_PORKCHOP, 8, 12.8F, 1.6f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.COOKED_RABBIT, 5, 6, 1.2f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.COOKED_SALMON, 6, 9.6F, 1.6f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.COOKIE, 2, 0.4F, 0.2f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.DRIED_KELP, 1, 0.6F, 0.6f));
        // 金苹果
        foodEffectTable = new FoodEffectTable(ItemPrototype.GOLDEN_APPLE, 4, 9.6F, 2.4f);
        foodEffectTable.addEffectType(FoodEffectType.GOLD_APPLE_ABSORPTION);
        foodEffectTable.addEffectType(FoodEffectType.GOLD_APPLE_REGERATION);
        foodEffectTables.add(foodEffectTable);
        // 附魔金苹果
        foodEffectTable = new FoodEffectTable(ItemPrototype.APPLEENCHANTED, 4, 9.6F, 2.4f);
        foodEffectTable.addEffectType(FoodEffectType.ENCHANT_APPLE_ABSORPTION);
        foodEffectTable.addEffectType(FoodEffectType.ENCHANT_APPLE_DAMAGE_RESISTANCE);
        foodEffectTable.addEffectType(FoodEffectType.ENCHANT_APPLE_FIRE_RESISTANCE);
        foodEffectTable.addEffectType(FoodEffectType.ENCHANT_APPLE_REGERATION);
        foodEffectTables.add(foodEffectTable);
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.GOLDEN_CARROT, 6, 14.4F, 2.4f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.MELON, 2, 1.2F, 0.6f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.MUSHROOM_STEW, 6, 7.2F, 1.2f));
        // 毒马铃薯
        foodEffectTable = new FoodEffectTable(ItemPrototype.POISONOUS_POTATO, 2, 1.2F, 0.6f);
        foodEffectTable.addEffectType(FoodEffectType.POTATO_POISON);
        foodEffectTables.add(foodEffectTable);
        // 马铃薯
        foodEffectTable = new FoodEffectTable(ItemPrototype.POTATO, 1, 0.6F, 0.6f);
        foodEffectTables.add(foodEffectTable);
        // 河豚
        foodEffectTable = new FoodEffectTable(ItemPrototype.PUFFERFISH, 1, 0.2F, 0.2f);
        foodEffectTable.addEffectType(FoodEffectType.PUFFERFISH_HUNGER);
        foodEffectTable.addEffectType(FoodEffectType.PUFFERFISH_NAUSEA);
        foodEffectTable.addEffectType(FoodEffectType.PUFFERFISH_POISON);
        foodEffectTables.add(foodEffectTable);
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.PUMPKIN_PIE, 8, 4.8F, 0.6f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.RABBIT_STEW, 10, 12, 1.2f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.BEEF, 5, 6, 1.2f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.PORKCHOP, 5, 6, 1.2f));
        // 生鸡肉
        foodEffectTable = new FoodEffectTable(ItemPrototype.CHICKEN, 2, 1.2F, 0.6f);
        foodEffectTable.addEffectType(FoodEffectType.RAW_CHICKEN_HUNGER);
        foodEffectTables.add(foodEffectTable);

        foodEffectTables.add(new FoodEffectTable(ItemPrototype.MUTTONRAW, 2, 0.4F, 0.2f));
        // 腐肉
        foodEffectTable = new FoodEffectTable(ItemPrototype.ROTTEN_FLESH, 4, 0.8F, 0.2f);
        foodEffectTable.addEffectType(FoodEffectType.ROTTEN_FLESH_HUNGER);
        foodEffectTables.add(foodEffectTable);
        // 蜘蛛眼
        foodEffectTable = new FoodEffectTable(ItemPrototype.SPIDER_EYE, 2, 3.2F, 1.6f);
        foodEffectTable.addEffectType(FoodEffectType.SPIDER_EYE_POISON);
        foodEffectTables.add(foodEffectTable);
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.COOKED_BEEF, 8, 12.8F, 1.6f));
        foodEffectTables.add(new FoodEffectTable(ItemPrototype.CLOWNFISH, 1, 0.2F, 0.2f));

        // 填充
        for (FoodEffectTable effectTable : foodEffectTables) {
            foodEffectTableDict[effectTable.getItemType().getId()] = effectTable;
        }
    }

    /**
     * 获取该食物的效果
     *
     * @param itemType
     * @return
     */
    public static FoodEffectTable get(ItemPrototype itemType) {
        if (itemType.getId() >= 512) {
            return null;
        }
        return foodEffectTableDict[itemType.getId()];
    }

    private ItemPrototype itemType;

    private int foodLevel;

    private float foodSaturationLevel;

    private float nutritionalValue;

    private List<FoodEffectType> effectTypes = new ArrayList<>();

    private FoodEffectTable(ItemPrototype itemType, int foodLevel, float foodSaturationLevel, float nutritionalValue) {
        this.itemType = itemType;
        this.foodLevel = foodLevel;
        this.foodSaturationLevel = foodSaturationLevel;
        this.nutritionalValue = nutritionalValue;
    }

    public ItemPrototype getItemType() {
        return itemType;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public float getFoodSaturationLevel() {
        return foodSaturationLevel;
    }

    public float getNutritionalValue() {
        return nutritionalValue;
    }

    public List<FoodEffectType> getEffectTypes() {
        return effectTypes;
    }

    private void addEffectType(FoodEffectType effectType) {
        this.effectTypes.add(effectType);
    }
}
