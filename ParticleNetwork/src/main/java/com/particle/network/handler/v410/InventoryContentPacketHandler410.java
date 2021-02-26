package com.particle.network.handler.v410;

import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.data.InventoryContentPacket;
import com.particle.network.encoder.ItemStackEncoder410;
import com.particle.network.handler.AbstractPacketHandler;

public class InventoryContentPacketHandler410 extends AbstractPacketHandler<InventoryContentPacket> {

    private ItemStackEncoder410 itemStackEncoder410 = ItemStackEncoder410.getInstance();

    @Override
    protected void doDecode(InventoryContentPacket dataPacket, int version) {
        dataPacket.setContainerId(dataPacket.readUnsignedVarInt());
        int slotCounts = dataPacket.readUnsignedVarInt();
        ItemStack[] slots = new ItemStack[0];
        for (int s = 0; s < slotCounts && dataPacket.canRead(); s++) {
            int netId = dataPacket.readSignedVarInt();
            slots[s] = this.itemStackEncoder410.decode(dataPacket, version);
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
                if (itemStack == null) {
                    dataPacket.writeSignedVarInt(0);
                } else {
                    dataPacket.writeSignedVarInt(itemStack.getItemType().getId());
                }

                this.itemStackEncoder410.encode(dataPacket, itemStack, version);
            }
        }
    }

}
