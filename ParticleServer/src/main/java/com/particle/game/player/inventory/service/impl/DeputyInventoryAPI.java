package com.particle.game.player.inventory.service.impl;

import com.particle.game.player.inventory.holder.EntityInventoryHolder;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.inventory.DeputyInventory;
import com.particle.model.inventory.Inventory;
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
 * 处理副手
 */
@Singleton
public class DeputyInventoryAPI extends BaseInventoryAPI {

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    /**
     * 装备副手
     *
     * @param slot
     * @return
     */
    @Override
    public boolean equipItem(Inventory inventory, int slot, ItemStack toItem, boolean notify) {
        InventoryHolder holder = inventory.getInventoryHolder();
        if (holder == null) {
            return false;
        }
        // 更新
        if (notify) {
            // 通知本人副手物品变更
            this.notifyPlayerHoldItemChanged((DeputyInventory) inventory, inventory.getViewers());
        }
        // 通知区块内其他玩家
        if (holder.getLevel() != null && holder instanceof EntityInventoryHolder) {
            this.broadcastServiceProxy.broadcast(holder.getOwn(), this.constructEquipmentPacket(inventory));
        }

        return true;
    }

    /**
     * 构造MobEquipmentPacket
     *
     * @param inventory
     * @return
     */
    @Override
    public DataPacket constructEquipmentPacket(Inventory inventory) {
        ItemStack holdItem = this.getItem(inventory, DeputyInventory.DEPUTY);
        // 其主人的eid
        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.setItemStack(holdItem);
        mobEquipmentPacket.setSlot((byte) DeputyInventory.DEPUTY);
        mobEquipmentPacket.setSelectedSlot((byte) DeputyInventory.DEPUTY);
        long eid = inventory.getInventoryHolder().getRuntimeId();
        mobEquipmentPacket.setTargetRuntimeId(eid);
        // 副手
        mobEquipmentPacket.setContainedId((byte) InventoryConstants.CONTAINER_ID_OFFHAND);
        return mobEquipmentPacket;
    }

    /**
     * 通知客户端副手物品发生变更
     *
     * @param inventory
     * @param players
     */
    private void notifyPlayerHoldItemChanged(DeputyInventory inventory, Collection<Player> players) {
        DataPacket mobEquipmentPacket = this.constructEquipmentPacket(inventory);
        ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>();
        for (Player player : players) {
            sendPlayers.add(player.getClientAddress());
        }
        this.networkManager.broadcastMessage(sendPlayers, mobEquipmentPacket);
    }
}
