package com.particle.model.network.packets.data;

import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class CreativeContentPacket extends DataPacket {
    private ItemStack[] slots = new ItemStack[0];

    @Override
    public int pid() {
        return ProtocolInfo.CREATIVE_CONTENT_PACKET;
    }

    public ItemStack[] getSlots() {
        return slots;
    }

    public void setSlots(ItemStack[] slots) {
        this.slots = slots;
    }
}
