package com.particle.network.handler.v410;

import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.data.CreativeContentPacket;
import com.particle.network.encoder.ItemStackEncoder410;
import com.particle.network.handler.AbstractPacketHandler;

public class CreativeContentPacketHandler410 extends AbstractPacketHandler<CreativeContentPacket> {

    private ItemStackEncoder410 itemStackEncoder410 = ItemStackEncoder410.getInstance();

    @Override
    protected void doDecode(CreativeContentPacket dataPacket, int version) {
        int slotCounts = dataPacket.readUnsignedVarInt();
        ItemStack[] slots = new ItemStack[0];
        for (int s = 0; s < slotCounts && dataPacket.canRead(); s++) {
            slots[s] = this.itemStackEncoder410.decode(dataPacket, version);
        }
        dataPacket.setSlots(slots);
    }

    @Override
    protected void doEncode(CreativeContentPacket dataPacket, int version) {
        ItemStack[] slots = dataPacket.getSlots();
        if (slots == null) {
            dataPacket.writeUnsignedVarInt(0);
        } else {
            dataPacket.writeUnsignedVarInt(slots.length);
            for (int i = 0; i < slots.length; i++) {
                dataPacket.writeUnsignedVarInt(i + 1);
                this.itemStackEncoder410.encode(dataPacket, slots[i], version);
            }
        }
    }

}
