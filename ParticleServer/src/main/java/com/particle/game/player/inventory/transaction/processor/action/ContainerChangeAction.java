package com.particle.game.player.inventory.transaction.processor.action;

import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.FurnaceInventory;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.data.InventoryActionData;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.player.Player;

import java.util.Set;

/**
 * 处理背包槽的item移动等操作的动作
 */
public class ContainerChangeAction extends InventoryAction {

    public ContainerChangeAction(Inventory inventory, InventoryActionData actionData) {
        super(inventory, actionData);
    }

    /**
     * 当slot的槽和fromItem的槽的物品属性一样，表示合法
     *
     * @param inventoryServiceProxy
     * @param player
     * @return
     */
    @Override
    public boolean isValid(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        // 检查数量是否合法
        if (this.getToItem().getCount() > this.getToItem().getMaxStackSize()) {
            return false;
        }

        // 获取原物品用于校验
        ItemStack slotItem = inventoryServiceProxy.getItem(this.getInventory(), this.getSlot());
        return slotItem.equalsWithCounts(this.getFromItem());
    }

    /**
     * 直接将目标item替换为slot
     *
     * @param inventoryServiceProxy
     * @param player
     * @return
     */
    @Override
    public boolean execute(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        // 两次校验，保证在traction过程中异常发生异常时，不会产生额外的物品
        // 这里的逻辑用于可靠性保证，不做失败处理
        if (!this.isValid(inventoryManager, inventoryServiceProxy, player)) {
            return false;
        }

        boolean result = inventoryServiceProxy.setItem(this.getInventory(), this.getSlot(), this.getToItem(), false);
        if (result) {
            this.executeFurnaceAction(inventoryServiceProxy, player);
        }
        return result;
    }

    /**
     * 处理执行成功
     * 通知其他玩家，背包的槽已更新
     *
     * @param inventoryServiceProxy
     * @param player
     */
    @Override
    public void onSuccess(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        Set<Player> viewers = this.getInventory().getViewers();
        inventoryServiceProxy
                .notifyPlayerSlotChanged(this.getInventory(), this.getSlot());
    }

    /**
     * 处理失败
     * 通知本人，将原来的背包塞回位置
     *
     * @param inventoryServiceProxy
     * @param player
     */
    @Override
    public void onFailed(InventoryManager inventoryManager, InventoryAPIProxy inventoryServiceProxy, Player player) {
        inventoryServiceProxy
                .notifyPlayerSlotChanged(this.getInventory(), this.getSlot());
    }

    /**
     * 针对熔炉的额外执行逻辑
     *
     * @param inventoryServiceProxy
     * @param player
     * @return
     */
    private boolean executeFurnaceAction(InventoryAPIProxy inventoryServiceProxy, Player player) {
        if (this.getSlot() != FurnaceInventory.ResultIndex
                || this.getFromItem() == null
                || this.getFromItem().getItemType().getId() == ItemPrototype.AIR.getId()) {
            return true;
        }
        ItemStack fromItem = this.getFromItem();
        // todo 根据不同的fromItem会将物品丢到地上，并管理该物品的可见者
        return true;
    }

    public ContainerChangeAction shadowCopy() {
        return new ContainerChangeAction(this.getInventory(), this.getInventoryActionData().shallowCopy());
    }
}
