package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.InventorySlotPacket;
import com.particle.network.encoder.ItemStackEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class InventorySlotPacketHandler extends AbstractPacketHandler<InventorySlotPacket> {

    private ItemStackEncoder itemStackEncoder = ItemStackEncoder.getInstance();

    @Override
    protected void doDecode(InventorySlotPacket dataPacket, int version) {
        dataPacket.setContainerId(dataPacket.readUnsignedVarInt());
        dataPacket.setSlot(dataPacket.readUnsignedVarInt());
        dataPacket.setItemStack(this.itemStackEncoder.decode(dataPacket, version));
    }

    @Override
    protected void doEncode(InventorySlotPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarInt(dataPacket.getContainerId());
        dataPacket.writeUnsignedVarInt(dataPacket.getSlot());
        this.itemStackEncoder.encode(dataPacket, dataPacket.getItemStack(), version);
    }
}
