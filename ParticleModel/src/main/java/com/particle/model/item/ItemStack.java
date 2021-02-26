package com.particle.model.item;

import com.particle.model.item.enchantment.Enchantment;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.item.enchantment.IEnchantmentType;
import com.particle.model.item.lore.Lore;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.item.types.ItemPrototypeDictionary;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTTagList;
import com.particle.model.nbt.NBTTagString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ItemStack implements Cloneable {

    public static final String LORE_PREFIX = "§0§r";
    public static final String STATE_PREFIX = "§4§r";

    private static ItemPrototypeDictionary itemPrototypeDictionary = ItemPrototypeDictionary.getDictionary();

    private ItemPrototype itemType;

    private int count = 0;

    private int meta = 0;

    private List<Enchantment> enchantments = new LinkedList<>();

    private Lore lore;

    /**
     * 物品携带的状态信息，在该物品被玩家装备或使用时生效
     */
    private Set<String> itemBindStates = new HashSet<>();

    /**
     * 物品NBT信息
     * NBT信息包括enchantments、lore或其它客户端使用的信息
     * <p>
     * 为方便使用，部分通用的结构化数据从NBT信息重拆解出来，如enchantments、lore，在set或get时特殊处理
     * 对于其它非固定的NBT信息如烟火携带的Explosions，则直接放在NBT中
     */
    private NBTTagCompound nbt;


    private static final Logger logger = LoggerFactory.getLogger(ItemStack.class);

    /**
     * 通过物品的名称获取物品
     */
    public static ItemStack getItem(String name) {
        ItemPrototype itemPrototype = itemPrototypeDictionary.map(name);

        if (itemPrototype == null)
            return null;

        ItemStack itemStack = new ItemStack(itemPrototype);
        itemStack.setCount(1);
        itemStack.setMeta(itemPrototype.getMaxMate());
        return itemStack;
    }

    public static ItemStack getItem(String name, int amount) {
        ItemPrototype type = itemPrototypeDictionary.map(name);
        if (type == null) {
            return null;
        }
        ItemStack itemStack = new ItemStack(type);
        itemStack.setCount(amount);
        itemStack.setMeta(type.getMaxMate());

        return itemStack;
    }

    public static ItemStack getItem(String name, int meta, int count) {
        ItemStack itemStack = getItem(name, count);
        if (itemStack == null) {
            return null;
        }
        itemStack.setMeta(meta);
        return itemStack;
    }

    /**
     * 通过物品的原型获取物品
     */
    public static ItemStack getItem(ItemPrototype itemPrototype) {
        return getItem(itemPrototype, 1);
    }

    public static ItemStack getItem(ItemPrototype itemPrototype, int amount) {
        return getItem(itemPrototype, 0, amount);
    }

    public static ItemStack getItem(ItemPrototype itemPrototype, int meta, int amount) {
        if (itemPrototype == null) {
            return null;
        }

        ItemStack itemStack = new ItemStack(itemPrototype);
        itemStack.setCount(amount);
        itemStack.setMeta(meta);

        return itemStack;
    }

    /**
     * 根据物品的ID获取物品
     */
    public static ItemStack getItem(Integer id) {
        return getItem(id, 1);
    }

    public static ItemStack getItem(Integer id, int amount) {
        ItemPrototype type = itemPrototypeDictionary.map(id);
        if (type == null) {
            return null;
        }
        ItemStack itemStack = new ItemStack(type);
        itemStack.setCount(amount);

        return itemStack;
    }

    public static ItemStack getItem(Integer id, int meta, int amount) {
        ItemStack itemStack = getItem(id, amount);
        if (itemStack == null) {
            return null;
        }
        itemStack.setMeta(meta);
        return itemStack;
    }

    /**
     * 构造函数，不允许外界调用
     *
     * @param iItemType
     */
    private ItemStack(ItemPrototype iItemType) {
        this.itemType = iItemType;
    }

    /**
     * 获取物品类型
     *
     * @return
     */
    public ItemPrototype getItemType() {
        return itemType;
    }

    /**
     * 物品的最大堆叠数
     *
     * @return
     */
    public int getMaxStackSize() {
        if (this.itemType == ItemPrototype.BUCKET) {
            switch (this.meta) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 10:
                    return 1;
                default:
                    return this.itemType.getMaxStackSize();
            }
        }
        return this.itemType.getMaxStackSize();
    }

    /**
     * 获取物品堆叠数量
     *
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * 设置物品堆叠数量
     *
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 获取物品meta信息
     *
     * @return
     */
    public int getMeta() {
        return meta;
    }

    /**
     * 判断是否存在meta信息
     *
     * @return
     */
    public boolean hasMeta() {
        return this.meta >= 0;
    }

    /**
     * 设置物品meta信息
     *
     * @param meta
     */
    public void setMeta(int meta) {
        this.meta = meta;
    }

    /**
     * 获取物品附魔
     *
     * @return
     */
    public List<Enchantment> getEnchantments() {
        return enchantments;
    }

    /**
     * 判断附魔
     * 已经移到ItemAttributeService中，ItemStack本身不再带业务逻辑
     */
    @Deprecated
    public boolean hasEnchantment(IEnchantmentType enchantmentType) {
        for (Enchantment enchantment : this.getEnchantments()) {
            if (enchantment.getType().getId() == enchantmentType.getId()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断附魔
     * 已经移到ItemAttributeService中，ItemStack本身不再带业务逻辑
     */
    @Deprecated
    public Enchantment getEnchantment(IEnchantmentType enchantmentType) {
        for (Enchantment enchantment : this.getEnchantments()) {
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
    @Deprecated
    public void addEnchantments(Enchantment enchantment) {
        //更新Enchantment
        boolean updated = false;

        List<Enchantment> enchantments = this.getEnchantments();
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
        this.setNbtData("ench", enchantmentsTag);
    }

    /**
     * 增加物品附魔
     *
     * @param enchantments
     */
    @Deprecated
    public void addEnchantments(List<Enchantment> enchantments) {
        //更新Enchantment
        boolean updated = false;

        List<Enchantment> enchantmentHaved = this.getEnchantments();
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
        this.setNbtData("ench", enchantmentsTag);
    }

    /**
     * 获取物品lore
     *
     * @return
     */
    public Lore getLore() {
        return lore;
    }

    /**
     * 设置物品lore
     *
     * @param lore
     */
    public void setLore(Lore lore) {
        this.lore = lore;
    }

    public Set<String> getItemBindStates() {
        return itemBindStates;
    }

    public void setItemBindStates(Set<String> itemBindStates) {
        this.itemBindStates = itemBindStates;
    }

    public NBTTagCompound getNbt() {
        return nbt;
    }

    public NBTTagCompound getNbt(boolean createIfNotExist) {
        if (this.nbt == null) {
            this.nbt = new NBTTagCompound();
        }

        return this.nbt;
    }

    public void setNbtData(String id, int data) {
        if (this.nbt == null) {
            this.nbt = new NBTTagCompound();
        }

        this.nbt.setInteger(id, data);
    }

    public void setNbtData(String id, long data) {
        if (this.nbt == null) {
            this.nbt = new NBTTagCompound();
        }

        this.nbt.setLong(id, data);
    }

    public void setNbtData(String id, boolean data) {
        if (this.nbt == null) {
            this.nbt = new NBTTagCompound();
        }

        this.nbt.setBoolean(id, data);
    }

    public void setNbtData(String id, String data) {
        if (this.nbt == null) {
            this.nbt = new NBTTagCompound();
        }

        this.nbt.setString(id, data);
    }

    public NBTTagCompound getNbtData(String id) {
        if (this.nbt == null) {
            return null;
        }

        if (this.nbt.hasKey(id)) {
            return this.nbt.getCompoundTag(id);
        } else {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            this.nbt.setTag(id, nbtTagCompound);
            return nbtTagCompound;
        }
    }

    public void setNbtData(String id, NBTTagCompound data) {
        if (this.nbt == null) {
            this.nbt = new NBTTagCompound();
        }

        this.nbt.setTag(id, data);
    }

    public void setNbtData(String id, NBTTagList data) {
        if (this.nbt == null) {
            this.nbt = new NBTTagCompound();
        }

        this.nbt.setTag(id, data);
    }

    /**
     * 更新NBT，此操作会级联更新enchangment、lore
     *
     * @param nbt
     */
    public void updateNBT(NBTTagCompound nbt) {
        this.nbt = nbt;

        /**
         * 更新Enchangment
         */
        NBTTagList nbtEnchantMents = nbt.getTagList("ench", 10);
        if (nbtEnchantMents != null) {
            this.enchantments = new LinkedList<>();

            for (int i = 0; i < nbtEnchantMents.tagCount(); i++) {
                NBTTagCompound nbtEnchantment = (NBTTagCompound) nbtEnchantMents.get(i);

                if (nbtEnchantment != null) {
                    short id = nbtEnchantment.getShort("id");
                    IEnchantmentType enchantmentType = Enchantments.fromId(id);
                    Enchantment enchantment = new Enchantment(enchantmentType, nbtEnchantment.getShort("lvl"));

                    this.enchantments.add(enchantment);
                }
            }
        }

        /**
         * 更新Lore
         */
        if (nbt.hasKey("display")) {
            NBTTagCompound nbtTagCompound = nbt.getCompoundTag("display");

            if (nbtTagCompound.hasKey("Lore")) {
                NBTTagList loreTag = nbtTagCompound.getTagList("Lore", 8);

                Lore lore = new Lore();
                for (int i = 0; i < loreTag.tagCount(); i++) {
                    NBTTagString nbtTagString = (NBTTagString) loreTag.get(i);

                    if (nbtTagString != null) {
                        String stringData = nbtTagString.getString();

                        if (stringData.startsWith(LORE_PREFIX)) {
                            lore.appendLore(stringData.substring(LORE_PREFIX.length()));
                        }
                    }
                }

                this.lore = lore;
            }
        }

        if (nbt.hasKey("state")) {
            NBTTagList stateList = nbt.getTagList("state", 8);

            for (int i = 0; i < stateList.tagCount(); i++) {
                NBTTagString stringTag = (NBTTagString) stateList.get(i);
                if (stringTag != null) {
                    this.itemBindStates.add(stringTag.getString());
                }
            }
        }
    }

    /**
     * 判断是否存在nbt信息
     *
     * @return
     */
    public boolean isExistedNbt() {
        return this.nbt != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemStack) {
            return this.equalsWithDamage((ItemStack) obj);
        } else {
            return super.equals(obj);
        }
    }

    /**
     * 只比较是否同一类物品
     *
     * @param itemStack
     * @return
     */
    public final boolean equalsType(ItemStack itemStack) {
        return this.itemType == itemStack.itemType;
    }

    /**
     * 比较是否同一类物品，同时比对伤害
     *
     * @param itemStack
     * @return
     */
    public final boolean equalsWithDamage(ItemStack itemStack) {
        return this.equalsType(itemStack) && this.meta == itemStack.meta;
    }

    /**
     * 比较同一类物品，同时会比对nbt
     *
     * @param itemStack
     * @return
     */
    public final boolean equalWithCompound(ItemStack itemStack) {
        if (this.equalsType(itemStack)) {
            if (this.nbt != null) {
                return this.nbt.equals(itemStack.nbt);
            } else {
                return itemStack.nbt == null;
            }
        } else {
            return false;
        }
    }

    public final boolean equalsEnchantments(ItemStack itemStack) {
        List<Enchantment> currentEnchantments = new LinkedList<>(this.getEnchantments());
        for (Enchantment enchantment : itemStack.getEnchantments()) {
            // 匹配是否有该附魔
            boolean matched = false;
            for (int i = 0; i < currentEnchantments.size(); i++) {
                Enchantment currentEnchant = currentEnchantments.get(i);
                // 匹配成功则移除缓存
                if (enchantment.getType().getId() == currentEnchant.getType().getId() && enchantment.getLevel() == currentEnchant.getLevel()) {
                    currentEnchantments.remove(i);
                    matched = true;
                    break;
                }
            }

            // 没有匹配成功
            if (!matched) {
                return false;
            }
        }

        // 检查是否还有没匹配的
        return currentEnchantments.size() == 0;
    }

    /**
     * 比较id, damage, nbt三个信息
     */
    public final boolean equalsAll(ItemStack itemStack) {
        return this.equalsWithDamage(itemStack) && this.equalWithCompound(itemStack);
    }

    /**
     * 比较id, damage, nbt, counts四个信息
     */
    public final boolean equalsWithCounts(ItemStack itemStack) {
        return this.equalsAll(itemStack) && this.count == itemStack.count;
    }

    /**
     * 比较id, damage, counts三个值
     *
     * @param itemStack
     * @return
     */
    public final boolean equalsDamageWithCounts(ItemStack itemStack) {
        return this.equalsWithDamage(itemStack) && this.count == itemStack.count;
    }

    /**
     * 该Item是空的
     *
     * @return
     */
    public boolean isNull() {
        return this.itemType.getId() == ItemPrototype.AIR.getId() || this.getCount() <= 0;
    }

    /**
     * 获取修复经验损失
     *
     * @return
     */
    public int getRepairedCost() {
        if (this.nbt == null) {
            return 0;
        }
        if (!this.nbt.hasKey("RepairCost", 3)) {
            return 0;
        }
        int repairCost = this.nbt.getInteger("RepairCost");
        return repairCost;
    }

    /**
     * ItemStack对象的克隆方法
     */
    @Override
    public ItemStack clone() {
        try {
            ItemStack itemStack = (ItemStack) super.clone();
            itemStack.itemType = this.itemType;
            itemStack.setMeta(this.meta);
            itemStack.setCount(this.count);
            itemStack.itemBindStates.addAll(this.itemBindStates);
            if (this.nbt != null) {
                itemStack.updateNBT((NBTTagCompound) this.nbt.copy());
            }
            return itemStack;
        } catch (CloneNotSupportedException cnse) {
            logger.error("clone error : ", cnse);
            return ItemStack.getItem(ItemPrototype.AIR);
        }
    }

    @Override
    public String toString() {
        return "ItemStack{" +
                "itemType=" + itemType +
                ", count=" + count +
                ", meta=" + meta +
                ", itemBindStates=" + (itemBindStates == null ? "" : Arrays.toString(itemBindStates.toArray())) +
                ", nbt=" + nbt +
                '}';
    }
}
