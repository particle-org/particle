package com.particle.game.item;

import com.particle.game.entity.state.EntityStateRecorderService;
import com.particle.game.entity.state.handle.EntityStateHandle;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantment;
import com.particle.model.item.enchantment.IEnchantmentType;
import com.particle.model.item.lore.Lore;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTTagList;
import com.particle.model.nbt.NBTTagString;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ItemAttributeService {

    public static final String LORE_PREFIX = "§0§r";
    public static final String STATE_PREFIX = "§4§r";

    /**
     * 设置物品lore
     *
     * @param lore
     */
    public static void setLore(ItemStack itemStack, Lore lore) {
        itemStack.setLore(lore);

        refreshLoreNbt(itemStack);
    }

    public static void addBindState(ItemStack itemStack, String stateKey) {
        // 判断state是否合法
        EntityStateHandle entityStateHandle = EntityStateRecorderService.get(stateKey);
        if (entityStateHandle == null) {
            return;
        }

        itemStack.getItemBindStates().add(stateKey);

        //更新NBT缓存
        NBTTagList enchantmentsTag = new NBTTagList();
        for (String bindState : itemStack.getItemBindStates()) {
            enchantmentsTag.appendTag(new NBTTagString(bindState));
        }
        itemStack.setNbtData("state", enchantmentsTag);

        refreshLoreNbt(itemStack);
    }

    public static void removeBindState(ItemStack itemStack, String stateKey) {
        itemStack.getItemBindStates().remove(stateKey);

        //更新NBT缓存
        NBTTagList enchantmentsTag = new NBTTagList();
        for (String bindState : itemStack.getItemBindStates()) {
            enchantmentsTag.appendTag(new NBTTagString(bindState));
        }
        itemStack.setNbtData("state", enchantmentsTag);

        refreshLoreNbt(itemStack);
    }

    public static void clearBindState(ItemStack itemStack) {
        itemStack.getItemBindStates().clear();

        //更新NBT缓存
        itemStack.setNbtData("state", new NBTTagList());

        refreshLoreNbt(itemStack);
    }

    public static Set<String> getBindState(ItemStack itemStack) {
        return Collections.unmodifiableSet(itemStack.getItemBindStates());
    }

    private static void refreshLoreNbt(ItemStack itemStack) {
        // 创建Tag
        NBTTagList loreTag = new NBTTagList();

        // 编码State数据
        for (String itemBindState : itemStack.getItemBindStates()) {
            EntityStateHandle entityStateHandle = EntityStateRecorderService.get(itemBindState);
            if (entityStateHandle != null) {
                String displayName = entityStateHandle.getDisplayName();
                if (StringUtils.isNotBlank(displayName)) {
                    loreTag.appendTag(new NBTTagString(STATE_PREFIX + "§4" + displayName + "§r"));
                }
            }
        }

        // 编码Lore数据
        if (itemStack.getLore() != null) {
            for (String line : itemStack.getLore().getLores()) {
                loreTag.appendTag(new NBTTagString(LORE_PREFIX + line));
            }
        }


        // 编码NBT数据
        NBTTagCompound display = itemStack.getNbtData("display");
        if (display == null) {
            display = new NBTTagCompound();
            itemStack.setNbtData("display", display);
        }
        display.setTag("Lore", loreTag);
    }

    /**
     * 判断附魔
     */
    public static boolean hasEnchantment(ItemStack itemStack, IEnchantmentType enchantmentType) {
        for (Enchantment enchantment : itemStack.getEnchantments()) {
            if (enchantment.getType().getId() == enchantmentType.getId()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断附魔
     */
    public static Enchantment getEnchantment(ItemStack itemStack, IEnchantmentType enchantmentType) {
        for (Enchantment enchantment : itemStack.getEnchantments()) {
            if (enchantment.getType().getId() == enchantmentType.getId()) {
                return enchantment;
            }
        }

        return null;
    }

    /**
     * 增加物品附魔
     *
     * @param enchantment
     */
    public static void addEnchantments(ItemStack itemStack, Enchantment enchantment) {
        //更新Enchantment
        boolean updated = false;

        List<Enchantment> enchantments = itemStack.getEnchantments();
        for (int i = 0; i < enchantments.size(); i++) {
            if (enchantments.get(i).getType().getId() == enchantment.getType().getId()) {
                enchantments.set(i, enchantment);

                updated = true;

                break;
            }
        }

        if (!updated) {
            enchantments.add(enchantment);
        }

        //更新NBT缓存
        NBTTagList enchantmentsTag = new NBTTagList();
        for (Enchantment ench : enchantments) {
            NBTTagCompound enchantmentTag = new NBTTagCompound();
            enchantmentTag.setShort("id", ench.getType().getId());
            enchantmentTag.setShort("lvl", (short) ench.getLevel());

            enchantmentsTag.appendTag(enchantmentTag);
        }
        itemStack.setNbtData("ench", enchantmentsTag);
    }

    /**
     * 增加物品附魔
     *
     * @param enchantments
     */
    public static void addEnchantments(ItemStack itemStack, List<Enchantment> enchantments) {
        //更新Enchantment
        boolean updated = false;

        List<Enchantment> enchantmentHaved = itemStack.getEnchantments();
        for (Enchantment enchantment : enchantments) {
            for (int i = 0; i < enchantmentHaved.size(); i++) {
                if (enchantmentHaved.get(i).getType().getId() == enchantment.getType().getId()) {
                    enchantmentHaved.set(i, enchantment);

                    updated = true;

                    break;
                }
            }

            if (!updated) {
                enchantmentHaved.add(enchantment);
            }
        }

        //更新NBT缓存
        NBTTagList enchantmentsTag = new NBTTagList();
        for (Enchantment ench : enchantmentHaved) {
            NBTTagCompound enchantmentTag = new NBTTagCompound();
            enchantmentTag.setShort("id", ench.getType().getId());
            enchantmentTag.setShort("lvl", (short) ench.getLevel());

            enchantmentsTag.appendTag(enchantmentTag);
        }
        itemStack.setNbtData("ench", enchantmentsTag);
    }

    /**
     * 移除附魔
     *
     * @param type
     */
    public static void removeEnchantments(ItemStack itemStack, IEnchantmentType type) {
        //更新Enchantment
        boolean updated = false;

        List<Enchantment> enchantments = itemStack.getEnchantments();
        for (int i = 0; i < enchantments.size(); i++) {
            if (enchantments.get(i).getType().getId() == type.getId()) {
                enchantments.remove(i);

                updated = true;

                break;
            }
        }

        //更新NBT缓存
        if (updated) {
            NBTTagList enchantmentsTag = new NBTTagList();
            for (Enchantment ench : enchantments) {
                NBTTagCompound enchantmentTag = new NBTTagCompound();
                enchantmentTag.setShort("id", ench.getType().getId());
                enchantmentTag.setShort("lvl", (short) ench.getLevel());

                enchantmentsTag.appendTag(enchantmentTag);
            }
            itemStack.setNbtData("ench", enchantmentsTag);
        }
    }

    /**
     * 移除所有附魔
     */
    public static void removeEnchantments(ItemStack itemStack) {
        //更新Enchantment
        List<Enchantment> enchantments = itemStack.getEnchantments();

        boolean updated = enchantments.size() != 0;

        if (enchantments.size() != 0) {
            if (itemStack.getNbt() != null) {
                itemStack.getNbt().removeTag("ench");
            }
        }

        enchantments.clear();
    }

    /**
     * 设置物品的颜色
     *
     * @param color
     */
    public static void setColor(ItemStack itemStack, int color) {
        NBTTagCompound nbt = itemStack.getNbt(true);
        nbt.setInteger("customColor", color);
    }

    /**
     * 获取物品的颜色
     *
     * @return
     */
    public static int getColor(ItemStack itemStack) {
        NBTTagCompound nbt = itemStack.getNbt();
        if (nbt == null) {
            return 0;
        }
        return nbt.getInteger("customColor");
    }

    /**
     * 去除颜色
     *
     * @return
     */
    public static boolean removeColor(ItemStack itemStack) {
        NBTTagCompound nbt = itemStack.getNbt();
        if (nbt == null) {
            return true;
        }
        nbt.removeTag("customColor");
        return true;
    }

    /**
     * 获取nbt中的display name信息
     *
     * @return
     */
    public static String getDisplayName(ItemStack itemStack) {
        NBTTagCompound nbt = itemStack.getNbt();
        if (nbt == null) {
            return "";
        }
        if (!nbt.hasKey("display", 10)) {
            return "";
        }
        NBTTagCompound displayNbt = nbt.getCompoundTag("display");
        if (!displayNbt.hasKey("Name", 8)) {
            return "";
        }
        return displayNbt.getString("Name");
    }

    /**
     * 设置物品名称
     *
     * @param name
     */
    public static void setDisplayName(ItemStack itemStack, String name) {
        NBTTagCompound nbt = itemStack.getNbt(true);
        NBTTagCompound display = nbt.getCompoundTag("display");
        if (display == null) {
            display = new NBTTagCompound();
            nbt.setTag("display", display);
        }
        display.setString("Name", name);
        nbt.setTag("display", display);
    }

}
