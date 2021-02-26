package com.particle.model.item;

import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.item.enchantment.IEnchantmentType;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentsDisplayNamesDictionary {

    private static Map<IEnchantmentType, String> enchantmentMap = new HashMap<IEnchantmentType, String>() {{
        put(Enchantments.PROTECTION, "保护");
        put(Enchantments.FIRE_PROTECTION, "火焰保护");
        put(Enchantments.FEATHER_FALLING, "摔落保护");
        put(Enchantments.BLAST_PROTECTION, "爆炸保护");
        put(Enchantments.PROJECTILE_PROTECTION, "弹射物保护");
        put(Enchantments.RESPIRATION, "水下呼吸");
        put(Enchantments.DEPTH_STRIDER, "深海探索者");
        put(Enchantments.AQUA_AFFINITY, "水下速崛");
        put(Enchantments.SHARPNESS, "锋利");
        put(Enchantments.SMITE, "亡灵杀手");
        put(Enchantments.BANE_OF_ARTHROPODS, "节肢杀手");
        put(Enchantments.KNOCKBACK, "击退");
        put(Enchantments.FIRE_ASPECT, "火焰附加");
        put(Enchantments.LOOTING, "抢夺");
        put(Enchantments.EFFICIENCY, "效率");
        put(Enchantments.SILK_TOUCH, "精准采集");
        put(Enchantments.DURABILITY, "耐久");
        put(Enchantments.FORTUNE, "时运");
        put(Enchantments.POWER, "力量");
        put(Enchantments.PUNCH, "冲击");
        put(Enchantments.FLAME, "火矢");
        put(Enchantments.INFINITY, "无限");
        put(Enchantments.LUCK_OF_SEA, "海之眷顾");
        put(Enchantments.LURE, "饵调");
        put(Enchantments.FROST_WALKER, "冰霜行者");
        put(Enchantments.MENDING, "经验修补");
        put(Enchantments.BINDING_CURSE, "绑定诅咒");
        put(Enchantments.VANISHING_CURSE, "消失诅咒");
        put(Enchantments.IMPALING, "穿刺");
        put(Enchantments.RIPTIDE, "激流");
        put(Enchantments.LOYALTY, "忠诚");
        put(Enchantments.CHANNELING, "引雷");
        put(Enchantments.MULTISHOT, "多重射擊");
        put(Enchantments.PIERCING, "穿透");
        put(Enchantments.QUICK_CHARGE, "快速裝填");
    }};

    public static String getName(IEnchantmentType type) {
        String name = enchantmentMap.get(type);
        return name == null ? "" : name;
    }

}
