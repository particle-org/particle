package com.particle.game.player.inventory.service.impl;

import com.particle.game.entity.state.EntityStateService;
import com.particle.game.item.ItemAttributeService;
import com.particle.game.player.inventory.holder.EntityInventoryHolder;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.entity.Entity;
import com.particle.model.inventory.ArmorInventory;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.api.InventoryHolder;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.InventoryContentPacket;
import com.particle.model.network.packets.data.InventorySlotPacket;
import com.particle.model.network.packets.data.MobArmorEquipmentPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Set;

/**
 * 用于盔甲
 */
@Singleton
public class ArmorInventoryAPI extends BaseInventoryAPI {

    private static final Logger logger = LoggerFactory.getLogger(ArmorInventoryAPI.class);

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Inject
    private EntityStateService entityStateService;

    @Override
    public void notifyPlayerContentChanged(Inventory inventory) {
        InventoryHolder holder = inventory.getInventoryHolder();
        if (holder == null) {
            return;
        }
        DataPacket mobArmorEquipmentPacket = this.constructEquipmentPacket(inventory);
        Set<Player> views = inventory.getViewers();
        for (Player player : views) {
            if (holder instanceof EntityInventoryHolder && player.getRuntimeId() == holder.getRuntimeId()) {
                InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
                inventoryContentPacket.setContainerId(InventoryConstants.CONTAINER_ID_ARMOR);
                inventoryContentPacket.setSlots(this.getArmorContents(inventory));
                networkManager.sendMessage(player.getClientAddress(), inventoryContentPacket);
            } else {
                //
                this.networkManager.sendMessage(player.getClientAddress(), mobArmorEquipmentPacket);
            }
        }

        // 通知区块内其他玩家
        if (holder.getLevel() != null && holder instanceof EntityInventoryHolder) {
            this.broadcastServiceProxy.broadcast(holder.getOwn(), mobArmorEquipmentPacket);
        }
    }

    /**
     * 通知盔甲的某个槽数据有更新
     * 对于本身玩家，需要发送槽更新数据
     *
     * @param inventory
     * @param index
     */
    @Override
    public void notifyPlayerSlotChanged(Inventory inventory, int index) {
        InventoryHolder holder = inventory.getInventoryHolder();
        if (holder == null) {
            return;
        }
        DataPacket mobArmorEquipmentPacket = this.constructEquipmentPacket(inventory);

        ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>();
        Set<Player> views = inventory.getViewers();
        for (Player player : views) {
            if (holder instanceof EntityInventoryHolder && player.getRuntimeId() == holder.getRuntimeId()) {
                InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
                inventorySlotPacket.setContainerId(InventoryConstants.CONTAINER_ID_ARMOR);
                inventorySlotPacket.setSlot(index);
                inventorySlotPacket.setItemStack(this.getItem(inventory, index));
                networkManager.sendMessage(player.getClientAddress(), inventorySlotPacket);
            } else {
                sendPlayers.add(player.getClientAddress());
            }
        }

        // 广播
        this.networkManager.broadcastMessage(sendPlayers, mobArmorEquipmentPacket);

        // 通知区块内其他玩家
        this.broadcastServiceProxy.broadcast(holder.getOwn(), mobArmorEquipmentPacket);
    }

    @Override
    protected void onSlotChange(Inventory inventory, int index, ItemStack old, ItemStack newItem, boolean notify) {
        super.onSlotChange(inventory, index, old, newItem, notify);

        // 装备效果
        InventoryHolder inventoryHolder = inventory.getInventoryHolder();
        if (inventoryHolder instanceof EntityInventoryHolder) {
            Entity own = inventoryHolder.getOwn();

            if (own instanceof Player) {
                Player player = (Player) own;
                if (old != null) {
                    for (String key : ItemAttributeService.getBindState(old)) {
                        this.entityStateService.unbindState(player, key);
                    }
                }

                if (newItem != null) {
                    for (String key : ItemAttributeService.getBindState(newItem)) {
                        this.entityStateService.bindState(player, key);
                    }
                }

            }
        }

    }


    /**
     * 构造MobArmorEquipmentPacket
     *
     * @param inventory
     * @return
     */
    @Override
    public DataPacket constructEquipmentPacket(Inventory inventory) {
        MobArmorEquipmentPacket mobArmorEquipmentPacket = new MobArmorEquipmentPacket();
        long eid = inventory.getInventoryHolder().getRuntimeId();
        mobArmorEquipmentPacket.setTargetRuntimeId(eid);
        mobArmorEquipmentPacket.setHead(this.getHelmet(inventory));
        mobArmorEquipmentPacket.setChestItem(this.getChestplate(inventory));
        mobArmorEquipmentPacket.setLegsItem(this.getLeggings(inventory));
        mobArmorEquipmentPacket.setFootItem(this.getBoots(inventory));
        return mobArmorEquipmentPacket;
    }

    /**
     * 头盔
     *
     * @param inventory
     * @return
     */
    private ItemStack getHelmet(Inventory inventory) {
        return this.getItem(inventory, ArmorInventory.HELMET);
    }

    /**
     * 胸甲
     *
     * @param inventory
     * @return
     */
    private ItemStack getChestplate(Inventory inventory) {
        return this.getItem(inventory, ArmorInventory.CHESTPLATE);
    }

    /**
     * 护膝
     *
     * @param inventory
     * @return
     */
    private ItemStack getLeggings(Inventory inventory) {
        return this.getItem(inventory, ArmorInventory.LEGGINGS);
    }

    /**
     * 鞋子
     *
     * @param inventory
     * @return
     */
    private ItemStack getBoots(Inventory inventory) {
        return this.getItem(inventory, ArmorInventory.BOOTS);
    }

    /**
     * 头盔
     *
     * @param inventory
     * @param helmet
     * @return
     */
    private boolean setHelmet(Inventory inventory, ItemStack helmet) {
        if (!(inventory instanceof ArmorInventory)) {
            return false;
        }
        return this.setItem(inventory, ArmorInventory.HELMET, helmet);
    }

    /**
     * 胸甲
     *
     * @param inventory
     * @param chestplate
     * @return
     */
    private boolean setChestplate(Inventory inventory, ItemStack chestplate) {
        if (!(inventory instanceof ArmorInventory)) {
            return false;
        }
        return this.setItem(inventory, ArmorInventory.CHESTPLATE, chestplate);
    }

    /**
     * 护膝
     *
     * @param inventory
     * @param leggings
     * @return
     */
    private boolean setLeggings(Inventory inventory, ItemStack leggings) {
        if (!(inventory instanceof ArmorInventory)) {
            return false;
        }
        return this.setItem(inventory, ArmorInventory.LEGGINGS, leggings);
    }

    /**
     * 头盔
     *
     * @param inventory
     * @param boots
     * @return
     */
    private boolean setBoots(Inventory inventory, ItemStack boots) {
        if (!(inventory instanceof ArmorInventory)) {
            return false;
        }
        return this.setItem(inventory, ArmorInventory.BOOTS, boots);
    }

    /**
     * 获取盔甲的所有数据
     *
     * @param inventory
     * @return
     */
    private ItemStack[] getArmorContents(Inventory inventory) {
        ItemStack[] armor = new ItemStack[4];
        for (int i = 0; i < 4; i++) {
            armor[i] = this.getItem(inventory, i);
        }
        return armor;
    }
}
