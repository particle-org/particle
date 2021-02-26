package com.particle.network.handler.v410;

import com.particle.model.network.packets.data.MobEquipmentPacket;
import com.particle.network.encoder.ItemStackEncoder410;
import com.particle.network.handler.AbstractPacketHandler;

public class MobEquipmentPacketHandler410 extends AbstractPacketHandler<MobEquipmentPacket> {

    private ItemStackEncoder410 itemStackEncoder = ItemStackEncoder410.getInstance();

    @Override
    protected void doDecode(MobEquipmentPacket dataPacket, int version) {
        dataPacket.setTargetRuntimeId(dataPacket.readUnsignedVarLong());
        dataPacket.setItemStack(this.itemStackEncoder.decode(dataPacket, version));
        dataPacket.setSlot(dataPacket.readByte());
        dataPacket.setSelectedSlot(dataPacket.readByte());
        dataPacket.setContainedId(dataPacket.readByte());
    }

    @Override
    protected void doEncode(MobEquipmentPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getTargetRuntimeId());
        this.itemStackEncoder.encode(dataPacket, dataPacket.getItemStack(), version);
        dataPacket.writeByte(dataPacket.getSlot());
        dataPacket.writeByte(dataPacket.getSelectedSlot());
        dataPacket.writeByte(dataPacket.getContainedId());
    }
}
