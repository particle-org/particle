package com.particle.model.item.enchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

public enum Enchantments implements IEnchantmentType {
    // 保护-减少多数的伤害
    PROTECTION(0, "minecraft:protection", 1, 4, 10, (level) -> {
        return 1 + (level - 1) * 11;
    }, (level) -> {
        return 21 + (level - 1) * 11;
    }),
    // 火焰保护-减少火焰的伤害
    FIRE_PROTECTION(1, "minecraft:fire_protection", 1, 4, 5, (level) -> {
        return 10 + (level - 1) * 8;
    }, (level) -> {
        return 22 + (level - 1) * 8;
    }),
    // 摔落保护-减少跌落的伤害
    FEATHER_FALLING(2, "minecraft:feather_falling", 1, 4, 5, (level) -> {
        return 5 + (level - 1) * 6;
    }, (level) -> {
        return 15 + (level - 1) * 6;
    }),
    // 爆炸保护-减少爆炸伤
    BLAST_PROTECTION(3, "minecraft:blast_protection", 1, 4, 2, (level) -> {
        return 5 + (level - 1) * 8;
    }, (level) -> {
        return 17 + (level - 1) * 8;
    }),
    // 弹射物保护-减少来源于弹射物的伤害
    PROJECTILE_PROTECTION(4, "minecraft:projectile_protection", 1, 4, 5, (level) -> {
        return 3 + (level - 1) * 6;
    }, (level) -> {
        return 18 + (level - 1) * 6;
    }),
    // 荆棘-给予攻击者伤害
    THORNS(5, "minecraft:thorns", 1, 3, 1, (level) -> {
        return 10 + (level - 1) * 20;
    }, (level) -> {
        return 60 + (level - 1) * 20;
    }),
    // 水下呼吸-延长水下呼吸时间
    RESPIRATION(6, "minecraft:respiration", 1, 3, 2, (level) -> {
        return 10 + (level - 1) * 20;
    }, (level) -> {
        return 60 + (level - 1) * 20;
    }),
    // 深海探索者-增加水下行走速度
    DEPTH_STRIDER(7, "minecraft:depth_strider", 1, 3, 2, (level) -> {
        return level * 10;
    }, (level) -> {
        return 15 + level * 10;
    }),
    // 水下速崛-加快水下挖掘速度
    AQUA_AFFINITY(8, "minecraft:aqua_affinity", 1, 1, 2, (level) -> {
        return 1;
    }, (level) -> {
        return 41;
    }),
    // 锋利-增加近战攻击伤害
    SHARPNESS(9, "minecraft:sharpness", 1, 5, 10, (level) -> {
        return 1 + (level - 1) * 11;
    }, (level) -> {
        return 21 + (level - 1) * 11;
    }),
    // 亡灵杀手-对亡灵生物造成额外伤害
    SMITE(10, "minecraft:smite", 1, 5, 5, (level) -> {
        return 5 + (level - 1) * 8;
    }, (level) -> {
        return 25 + (level - 1) * 8;
    }),
    // 节肢杀手-对节肢生物造成额外伤害
    BANE_OF_ARTHROPODS(11, "minecraft:bane_of_arthropods", 1, 5, 5, (level) -> {
        return 5 + (level - 1) * 8;
    }, (level) -> {
        return 25 + (level - 1) * 8;
    }),
    // 击退-增加击退距离
    KNOCKBACK(12, "minecraft:knockback", 1, 2, 5, (level) -> {
        return 5 + (level - 1) * 20;
    }, (level) -> {
        return 55 + (level - 1) * 20;
    }),
    // 火焰附加-使目标着火
    FIRE_ASPECT(13, "minecraft:fire_aspect", 1, 2, 2, (level) -> {
        return 10 + (level - 1) * 20;
    }, (level) -> {
        return 60 + (level - 1) * 20;
    }),
    // 抢夺-生物能掉落更多物品
    LOOTING(14, "minecraft:looting", 1, 3, 2, (level) -> {
        return 15 + (level - 1) * 9;
    }, (level) -> {
        return 65 + (level - 1) * 9;
    }),
    // 效率-加快挖掘速度
    EFFICIENCY(15, "minecraft:efficiency", 1, 5, 10, (level) -> {
        return 1 + (level - 1) * 10;
    }, (level) -> {
        return 51 + (level - 1) * 10;
    }),
    // 精准采集-被开采的方块掉落本身
    SILK_TOUCH(16, "minecraft:silk_touch", 1, 1, 1, (level) -> {
        return 15;
    }, (level) -> {
        return 65;
    }),
    // 耐久-减少物品掉耐久的几率
    DURABILITY(17, "minecraft:durability", 1, 3, 5, (level) -> {
        return 5 + (level - 1) * 8;
    }, (level) -> {
        return 55 + (level - 1) * 8;
    }),
    // 时运-增加方块掉落
    FORTUNE(18, "minecraft:fortune", 1, 3, 2, (level) -> {
        return 15 + (level - 1) * 9;
    }, (level) -> {
        return 65 + (level - 1) * 9;
    }),
    // 力量-增加弓箭伤害
    POWER(19, "minecraft:power", 1, 5, 10, (level) -> {
        return 1 + (level - 1) * 20;
    }, (level) -> {
        return 16 + (level - 1) * 20;
    }),
    // 冲击-增加弓箭的击退距离
    PUNCH(20, "minecraft:punch", 1, 2, 2, (level) -> {
        return 12 + (level - 1) * 20;
    }, (level) -> {
        return 62 + (level - 1) * 20;
    }),
    // 火矢-箭矢使目标着火
    FLAME(21, "minecraft:flame", 1, 1, 2, (level) -> {
        return 20;
    }, (level) -> {
        return 50;
    }),
    // 无限-射箭不会消耗普通箭矢
    INFINITY(22, "minecraft:infinity", 1, 1, 1, (level) -> {
        return 20;
    }, (level) -> {
        return 50;
    }),
    // 海之眷顾-提高钓鱼时获得宝藏的几率
    LUCK_OF_SEA(23, "minecraft:luck_of_the_sea", 1, 3, 2, (level) -> {
        return 15 + (level - 1) * 9;
    }, (level) -> {
        return 65 + (level - 1) * 9;
    }),
    // 饵调-提高鱼咬钩的速度
    LURE(24, "minecraft:lure", 1, 3, 2, (level) -> {
        return 15 + (level - 1) * 9;
    }, (level) -> {
        return 65 + (level - 1) * 9;
    }),
    // 冰霜行者-允许水上行走
    FROST_WALKER(25, "minecraft:frost_walker", 1, 2, 2, (level) -> {
        return level * 10;
    }, (level) -> {
        return level * 10 + 15;
    }),
    // 经验修补-用经验修补工具的耐久度
    MENDING(26, "minecraft:mending", 1, 1, 2, (level) -> {
        return level * 10;
    }, (level) -> {
        return level * 10 + 15;
    }),
    // ----TODO----
    // 绑定诅咒-物品无法被丢弃
    BINDING_CURSE(27, "minecraft:binding_curse", 1, 1, 1, (level) -> {
        return 1 + level * 10;
    }, (level) -> {
        return 6 + level * 10;
    }),
    // 消失诅咒-物品会在玩家死亡时消失
    VANISHING_CURSE(28, "minecraft:vanishing_curse", 1, 1, 1, (level) -> {
        return 1 + level * 10;
    }, (level) -> {
        return 6 + level * 10;
    }),
    // 穿刺-对水生生物造成额外伤害
    IMPALING(29, "minecraft:impaling", 1, 5, 2, (level) -> {
        return 1 + level * 10;
    }, (level) -> {
        return 6 + level * 10;
    }),
    // 激流-将玩家向掷出三叉戟的方向发射
    RIPTIDE(30, "minecraft:riptide", 1, 3, 2, (level) -> {
        return 1 + level * 10;
    }, (level) -> {
        return 6 + level * 10;
    }),
    // 忠诚-使掷出后的三叉戟返回
    LOYALTY(31, "minecraft:loyalty", 1, 3, 5, (level) -> {
        return 1 + level * 10;
    }, (level) -> {
        return 6 + level * 10;
    }),
    // 引累-召唤闪电攻击生物
    CHANNELING(32, "minecraft:channeling", 1, 1, 1, (level) -> {
        return 1 + level * 10;
    }, (level) -> {
        return 6 + level * 10;
    }),
    // 多重射擊
    MULTISHOT(33, "minecraft:multishot", 1, 1, 3, (level) -> {
        return 1 + level * 10;
    }, (level) -> {
        return 6 + level * 10;
    }),
    // 穿透
    PIERCING(34, "minecraft:piercing", 1, 4, 1, (level) -> {
        return 1 + level * 10;
    }, (level) -> {
        return 6 + level * 10;
    }),
    // 快速裝填
    QUICK_CHARGE(35, "minecraft:quick_charge", 1, 3, 1, (level) -> {
        return 1 + level * 10;
    }, (level) -> {
        return 6 + level * 10;
    }),

    NULL(50, "minecraft:null", 1, 3, 1, (level) -> 1 + level * 10, (level) -> 6 + level * 10);


    private short id;
    private String name;
    private short minimumLevel;
    private short maximumLevel;
    private int weight;
    private IntFunction<Integer> minEnchantLevelFunction;
    private IntFunction<Integer> maxEnchantLevelFunction;

    private static final Map<Short, Enchantments> allEnchantmentsById = new HashMap<>();
    private static final Map<String, Enchantments> allEnchantmentsByName = new HashMap<>();

    static {
        Enchantments[] enchantments = Enchantments.values();
        for (Enchantments enchantments1 : enchantments) {
            allEnchantmentsById.put(enchantments1.id, enchantments1);
            allEnchantmentsByName.put(enchantments1.name, enchantments1);
        }
    }

    private Enchantments(int id, String name, int minimumLevel, int maximumLevel, int weight,
                         IntFunction<Integer> minEnchantAbilityForLevel, IntFunction<Integer> maxEnchantAbilityForLevel) {
        this.id = (short) id;
        this.name = name;
        this.minimumLevel = (short) minimumLevel;
        this.maximumLevel = (short) maximumLevel;
        this.weight = weight;
        this.minEnchantLevelFunction = minEnchantAbilityForLevel;
        this.maxEnchantLevelFunction = maxEnchantAbilityForLevel;
    }

    /**
     * 根据id获取附魔类型
     *
     * @param id
     * @return
     */
    public static IEnchantmentType fromId(int id) {
        return allEnchantmentsById.get(Integer.valueOf(id).shortValue());
    }

    /**
     * 根据name获取附魔类型
     *
     * @param name
     * @return
     */
    public static IEnchantmentType fromName(String name) {
        return allEnchantmentsByName.get(name);
    }

    @Override
    public short getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public short getMinimumLevel() {
        return this.minimumLevel;
    }

    @Override
    public short getMaximumLevel() {
        return this.maximumLevel;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public short getMinEnchantAbilityForLevel(int level) {
        return minEnchantLevelFunction.apply(level).shortValue();
    }

    @Override
    public short getMaxEnchantAbilityForLevel(int level) {
        return maxEnchantLevelFunction.apply(level).shortValue();
    }


}
