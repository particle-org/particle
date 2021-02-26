package com.particle.network.handler.v274;

import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.data.InventoryContentPacket;
import com.particle.network.encoder.ItemStackEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class InventoryContentPacketHandler extends AbstractPacketHandler<InventoryContentPacket> {

    private ItemStackEncoder itemStackEncoder = ItemStackEncoder.getInstance();

    @Override
    protected void doDecode(InventoryContentPacket dataPacket, int version) {
        dataPacket.setContainerId(dataPacket.readUnsignedVarInt());
        int slotCounts = dataPacket.readUnsignedVarInt();
        ItemStack[] slots = new ItemStack[0];
        for (int s = 0; s < slotCounts && dataPacket.canRead(); s++) {
            slots[s] = this.itemStackEncoder.decode(dataPacket, version);
        }
        dataPacket.setSlots(slots);
    }

    @Override
    protected void doEncode(InventoryContentPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarInt(dataPacket.getContainerId());
        ItemStack[] slots = dataPacket.getSlots();
        if (slots == null) {
            dataPacket.writeUnsignedVarInt(0);
        } else {
            dataPacket.writeUnsignedVarInt(slots.length);
            for (ItemStack itemStack : slots) {
                this.itemStackEncoder.encode(dataPacket, itemStack, version);
            }
        }
    }

}
