package com.particle.game.player.inventory.service.impl;

import com.particle.game.player.inventory.holder.EntityInventoryHolder;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.events.level.entity.InventoryChangedEvent;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.MobEquipmentPacket;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 处理玩家背包
 */
@Singleton
public class PlayerInventoryAPI extends BaseInventoryAPI {

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Override
    public boolean equipItem(Inventory inventory, int slot, ItemStack toItem, boolean notify) {
        InventoryHolder holder = inventory.getInventoryHolder();
        if (holder == null) {
            return false;
        }
        PlayerInventory playerInventory = (PlayerInventory) inventory;
        if (!playerInventory.isHotBarSlot(slot)) {
            this.notifyPlayerContentChanged(inventory);
            return false;
        }
        this.setHoldItemIndex(playerInventory, slot, notify);

        // 通知区块内其他玩家
        if (holder.getLevel() != null && holder instanceof EntityInventoryHolder) {
            this.broadcastServiceProxy.broadcast(holder.getOwn(), this.constructEquipmentPacket(inventory));
        }
        return true;
    }

    @Override
    public boolean setItem(Inventory inventory, int index, ItemStack itemStack, boolean notify) {
        boolean result = super.setItem(inventory, index, itemStack, notify);
        if (notify) {
            this.notifyPlayerHoldItemChanged((PlayerInventory) inventory, inventory.getViewers());
        }
        return result;
    }

    /**
     * 构造MobEquipmentPacket
     *
     * @param inventory
     * @return
     */
    @Override
    public DataPacket constructEquipmentPacket(Inventory inventory) {
        PlayerInventory playerInventory = (PlayerInventory) inventory;
        ItemStack holdItem = this.getHoldItem(playerInventory);
        // 其主人的eid
        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.setItemStack(holdItem);
        mobEquipmentPacket.setSlot((byte) playerInventory.getItemInHandle());
        mobEquipmentPacket.setSelectedSlot((byte) playerInventory.getItemInHandle());
        long eid = playerInventory.getInventoryHolder().getRuntimeId();
        mobEquipmentPacket.setTargetRuntimeId(eid);
        mobEquipmentPacket.setContainedId((byte) InventoryConstants.CONTAINER_ID_PLAYER);
        return mobEquipmentPacket;
    }

    @Override
    protected void onSlotChange(Inventory inventory, int index, ItemStack old, ItemStack newItem, boolean notify) {
        InventoryHolder holder = inventory.getInventoryHolder();
        if (!(holder instanceof EntityInventoryHolder)) {
            return;
        }

        EntityInventoryHolder entityInventoryHolder = (EntityInventoryHolder) holder;
        if (entityInventoryHolder.getOwn() != null) {
            InventoryChangedEvent inventoryChangedEvent = new InventoryChangedEvent(entityInventoryHolder.getOwn());
            inventoryChangedEvent.setFrom(old);
            inventoryChangedEvent.setTo(newItem);
            inventoryChangedEvent.setSlot(index);
            inventoryChangedEvent.setOperation(inventory.getName());
            this.eventDispatcher.dispatchEvent(inventoryChangedEvent);
        }

        super.onSlotChange(inventory, index, old, newItem, notify);
    }

    /**
     * 设置手执物品
     *
     * @param inventory
     * @param index
     * @param notify
     */
    private void setHoldItemIndex(PlayerInventory inventory, int index, boolean notify) {
        inventory.setItemInHandle(index);
        if (notify) {
            this.notifyPlayerHoldItemChanged(inventory, inventory.getViewers());
        }
    }

    /**
     * 返回手执物品
     *
     * @param inventory
     * @return
     */
    private ItemStack getHoldItem(PlayerInventory inventory) {
        return this.getItem(inventory, inventory.getItemInHandle());
    }

    /**
     * 修改手执物品
     *
     * @param inventory
     * @return
     */
    private void setHoldItem(PlayerInventory inventory, ItemStack itemStack, boolean notify) {
        InventoryHolder holder = inventory.getInventoryHolder();
        if (!(holder instanceof EntityInventoryHolder)) {
            return;
        }
        this.setItem(inventory, inventory.getItemInHandle(), itemStack, notify);
    }

    /**
     * 通知客户端手执物品发生变更
     *
     * @param inventory
     * @param players
     */
    private void notifyPlayerHoldItemChanged(PlayerInventory inventory, Collection<Player> players) {
        DataPacket mobEquipmentPacket = this.constructEquipmentPacket(inventory);
        ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>();
        for (Player player : players) {
            sendPlayers.add(player.getClientAddress());
        }
        this.networkManager.broadcastMessage(sendPlayers, mobEquipmentPacket);
    }
}
