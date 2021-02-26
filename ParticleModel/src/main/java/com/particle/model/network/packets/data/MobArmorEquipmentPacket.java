package com.particle.model.network.packets.data;

import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class MobArmorEquipmentPacket extends DataPacket {

    private long targetRuntimeId;

    private ItemStack head;

    private ItemStack chestItem;

    private ItemStack legsItem;

    private ItemStack footItem;

    @Override
    public int pid() {
        return ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET;
    }

    public long getTargetRuntimeId() {
        return targetRuntimeId;
    }

    public void setTargetRuntimeId(long targetRuntimeId) {
        this.targetRuntimeId = targetRuntimeId;
    }

    public ItemStack getHead() {
        return head;
    }

    public void setHead(ItemStack head) {
        this.head = head;
    }

    public ItemStack getChestItem() {
        return chestItem;
    }

    public void setChestItem(ItemStack chestItem) {
        this.chestItem = chestItem;
    }

    public ItemStack getLegsItem() {
        return legsItem;
    }

    public void setLegsItem(ItemStack legsItem) {
        this.legsItem = legsItem;
    }

    public ItemStack getFootItem() {
        return footItem;
    }

    public void setFootItem(ItemStack footItem) {
        this.footItem = footItem;
    }
}
