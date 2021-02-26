package com.particle.inputs.inventory;

import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.world.map.MapService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.events.level.player.PlayerMobEquipLevelEvent;
import com.particle.model.inventory.DeputyInventory;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.MobEquipmentPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MobEquipmentPacketHandle extends PlayerPacketHandle<MobEquipmentPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobEquipmentPacketHandle.class);

    @Inject
    private MapService mapService;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private InventoryAPIProxy inventoryServiceProxy;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Override
    protected void handlePacket(Player player, MobEquipmentPacket packet) {
        // 查询背包
        Inventory inventory = this.inventoryManager.getInventoryByContainerId(player, packet.getContainedId());
        if (inventory == null) {
            LOGGER.warn("Player {} equip item with illegal inventory id!", player.getIdentifiedStr());
            return;
        }

        // 校验手持物品
        ItemStack serviceItem = this.inventoryServiceProxy.getItem(inventory, packet.getSelectedSlot());

        // 发送事件
        PlayerMobEquipLevelEvent playerMobEquipLevelEvent = new PlayerMobEquipLevelEvent(player, packet.getTargetRuntimeId(), serviceItem, packet.getSlot(), packet.getSelectedSlot(), inventory);
        this.eventDispatcher.dispatchEvent(playerMobEquipLevelEvent);

        if (!playerMobEquipLevelEvent.isCancelled()) {
            if (inventory instanceof PlayerInventory) {
                this.mapService.updateHolderItem(player, serviceItem, packet.getSelectedSlot(), true);
            } else if (inventory instanceof DeputyInventory) {
                this.mapService.updateHolderItem(player, serviceItem, packet.getSelectedSlot(), false);
            }


            this.inventoryServiceProxy.equipItem(inventory, packet.getSelectedSlot(), serviceItem, false);
        }
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.MOB_EQUIPMENT_PACKET;
    }
}
