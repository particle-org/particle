package com.particle.model.network.packets.data;

import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class InventorySlotPacket extends DataPacket {

    @Override
    public int pid() {
        return ProtocolInfo.INVENTORY_SLOT_PACKET;
    }

    private int containerId;

    private int slot;

    private ItemStack itemStack;

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
