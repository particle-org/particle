package com.particle.model.item.enchantment;

public interface IEnchantmentType {
    /**
     * 该种附魔类型的ID
     *
     * @return 该种附魔类型的ID
     */
    short getId();

    /**
     * 该种附魔类型的最低Level
     *
     * @return 该种附魔类型的最低Level
     */
    short getMinimumLevel();

    /**
     * 该种附魔类型的最高Level.
     *
     * @return 该种附魔类型的最高Level
     */
    short getMaximumLevel();

    /**
     * 附魔时的随机权重.
     *
     * @return 该附魔类型的权重
     */
    int getWeight();

    /**
     * 附魔时的最小等级.
     *
     * @return 附魔时的最小等级
     */
    short getMinEnchantAbilityForLevel(int level);

    /**
     * 附魔时的最大等级.
     *
     * @return 附魔时的最大等级
     */
    short getMaxEnchantAbilityForLevel(int level);

    /**
     * 附魔名称
     *
     * @return
     */
    String getName();
}
