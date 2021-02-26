package com.particle.api.inventory;

import com.particle.model.inventory.Inventory;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface InventoryAPI {


    /**
     * 获取特定index槽的物品
     *
     * @param inventory 背包对象
     * @param index     槽
     * @return 返回指定物品，若为空，返回Air
     */
    ItemStack getItem(Inventory inventory, int index);

    /**
     * 设置特定index槽的物品，会主动通知客户端
     *
     * @param inventory 背包
     * @param index     槽
     * @param itemStack 物品
     * @return 结果
     */
    boolean setItem(Inventory inventory, int index, ItemStack itemStack);

    /**
     * 设置特定index槽的物品
     *
     * @param inventory 背包
     * @param index     槽
     * @param itemStack 物品
     * @param notify    是否通知给客户端
     * @return 结果
     */
    boolean setItem(Inventory inventory, int index, ItemStack itemStack, boolean notify);

    /**
     * 添加物品
     *
     * @param inventory 背包
     * @param itemStack 物品
     * @return 结果，true表示添加成功，false表示添加失败或者只添加了一部分
     */
    default List<ItemStack> addItem(Inventory inventory, ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemType() == ItemPrototype.AIR) {
            return new ArrayList<>();
        }
        List<ItemStack> itemStacks = new ArrayList<>();
        itemStacks.add(itemStack);
        return this.addItem(inventory, itemStacks);
    }

    /**
     * 添加物品
     *
     * @param inventory 背包
     * @param slots     槽
     * @return 未成功添加的物品列表
     */
    List<ItemStack> addItem(Inventory inventory, List<ItemStack> slots);

    /**
     * 是否允许添加
     *
     * @param inventory 背包
     * @param itemStack 物品
     * @return 结果 true or false
     */
    boolean isAllowAdd(Inventory inventory, ItemStack itemStack);

    /**
     * 去除物品
     *
     * @param inventory 背包
     * @param slots     槽
     * @return 返回未删除的物品列表
     */
    List<ItemStack> removeItem(Inventory inventory, List<ItemStack> slots);

    /**
     * 更新背包所有的槽数据
     *
     * @param inventory 背包
     * @param items     物品列表
     */
    void updateAllSlots(Inventory inventory, Map<Integer, ItemStack> items);

    /**
     * 通知玩家，背包有更新
     *
     * @param inventory 背包
     */
    void notifyPlayerContentChanged(Inventory inventory);


    /**
     * 通知玩家，背包的某个槽有更新
     *
     * @param inventory 背包
     * @param index     槽
     */
    void notifyPlayerSlotChanged(Inventory inventory, int index);

    /**
     * 是否包含此item
     *
     * @param inventory 背包
     * @param itemStack 物品
     * @return 结果
     */
    boolean contain(Inventory inventory, ItemStack itemStack);

    /**
     * 找出包含itemStack的所有槽
     *
     * @param itemStack 物品
     * @return 返回Map，key为槽， value为物品
     */
    Map<Integer, ItemStack> findAll(Inventory inventory, ItemStack itemStack);

    /**
     * 寻找存放此物品的第一个槽 （数量必须一致）
     *
     * @param inventory 背包
     * @param itemStack 物品
     * @return 槽，若未找到返回-1
     */
    default int first(Inventory inventory, ItemStack itemStack) {
        return this.first(inventory, itemStack, false);
    }

    /**
     * 寻找存放此物品的第一个槽
     *
     * @param inventory  背包
     * @param itemStack  物品
     * @param strictMode 若为true，需要严格校验数量，若为false，则不需要校验
     * @return 返回槽的index，若未找到返回-1
     */
    int first(Inventory inventory, ItemStack itemStack, boolean strictMode);

    /**
     * 第一个空槽的位置
     *
     * @param inventory 背包
     * @return 槽，若未找到，返回-1
     */
    int firstEmpty(Inventory inventory);

    /**
     * 清空特定index槽，会通知客户端
     *
     * @param inventory 背包
     * @param index     槽
     * @return 返回清理结果
     */
    default boolean clear(Inventory inventory, int index) {
        return this.clear(inventory, index, true);
    }

    /**
     * 清空特定index槽，并通知给客户端
     *
     * @param inventory 背包
     * @param index     槽
     * @param notify    是否通知客户端
     * @return 返回清理结果
     */
    boolean clear(Inventory inventory, int index, boolean notify);

    /**
     * 清空背包，会通知客户端
     *
     * @param inventory 背包
     * @return 返回结果
     */
    boolean clearAll(Inventory inventory);

    /**
     * 清空背包
     *
     * @param inventory 背包
     * @param notify    是否通知客户端
     * @return 返回清理结果
     */
    boolean clearAll(Inventory inventory, boolean notify);

    /**
     * 背包是否为空
     *
     * @param inventory 背包
     * @return 返回结果
     */
    boolean isEmpty(Inventory inventory);

    /**
     * 背包是否满了
     *
     * @param inventory 背包
     * @return 返回结果
     */
    boolean isFull(Inventory inventory);

    /**
     * 将该玩家加到背包的可见列表中
     *
     * @param player    玩家
     * @param inventory 背包
     */
    void addView(Player player, Inventory inventory);


    /**
     * 将该玩家移出到背包的可见列表中
     *
     * @param player     玩家
     * @param inventory  背包
     * @param serverSide 是否是服务端发起，若为true,会向客户端发送关闭UI的packet
     */
    void removeView(Player player, Inventory inventory, boolean serverSide);

    /**
     * 构造装备的packet，仅用于玩家背包、副手背包、装备背包
     *
     * @param inventory 背包
     * @return 返回packet
     */
    DataPacket constructEquipmentPacket(Inventory inventory);


    /**
     * 装备物品，仅用于玩家背包、副手背包
     *
     * @param inventory 背包
     * @param slot      槽
     * @param toItem    目标物品，当为玩家背包时，该参数可为空。
     * @param notify    是否通知客户端
     * @return 执行结果
     */
    boolean equipItem(Inventory inventory, int slot, ItemStack toItem, boolean notify);

}
