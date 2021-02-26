package com.particle.network.handler.v410;

import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.data.InventorySlotPacket;
import com.particle.network.encoder.ItemStackEncoder410;
import com.particle.network.handler.AbstractPacketHandler;

public class InventorySlotPacketHandler410 extends AbstractPacketHandler<InventorySlotPacket> {

    private ItemStackEncoder410 itemStackEncoder = ItemStackEncoder410.getInstance();

    @Override
    protected void doDecode(InventorySlotPacket dataPacket, int version) {
        dataPacket.setContainerId(dataPacket.readUnsignedVarInt());
        dataPacket.setSlot(dataPacket.readUnsignedVarInt());
        dataPacket.setItemStack(ItemStack.getItem(dataPacket.readSignedVarInt()));
        dataPacket.setItemStack(this.itemStackEncoder.decode(dataPacket, version));
    }

    @Override
    protected void doEncode(InventorySlotPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarInt(dataPacket.getContainerId());
        dataPacket.writeUnsignedVarInt(dataPacket.getSlot());
        dataPacket.writeSignedVarInt(dataPacket.getItemStack().getItemType().getId());
        this.itemStackEncoder.encode(dataPacket, dataPacket.getItemStack(), version);
    }
}
