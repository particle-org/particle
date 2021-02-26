package com.particle.model.network.packets.data;

import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class InventoryContentPacket extends DataPacket {

    private int containerId;

    private ItemStack[] slots = new ItemStack[0];

    @Override
    public int pid() {
        return ProtocolInfo.INVENTORY_CONTENT_PACKET;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public ItemStack[] getSlots() {
        return slots;
    }

    public void setSlots(ItemStack[] slots) {
        this.slots = slots;
    }
}
