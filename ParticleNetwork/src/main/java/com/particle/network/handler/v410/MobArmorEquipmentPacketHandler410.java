package com.particle.network.handler.v410;

import com.particle.model.network.packets.data.MobArmorEquipmentPacket;
import com.particle.network.encoder.ItemStackEncoder410;
import com.particle.network.handler.AbstractPacketHandler;

public class MobArmorEquipmentPacketHandler410 extends AbstractPacketHandler<MobArmorEquipmentPacket> {

    private ItemStackEncoder410 itemStackEncoder = ItemStackEncoder410.getInstance();

    @Override
    protected void doDecode(MobArmorEquipmentPacket dataPacket, int version) {
        dataPacket.setTargetRuntimeId(dataPacket.readUnsignedVarLong());
        dataPacket.setHead(this.itemStackEncoder.decode(dataPacket, version));
        dataPacket.setChestItem(this.itemStackEncoder.decode(dataPacket, version));
        dataPacket.setLegsItem(this.itemStackEncoder.decode(dataPacket, version));
        dataPacket.setFootItem(this.itemStackEncoder.decode(dataPacket, version));
    }

    @Override
    protected void doEncode(MobArmorEquipmentPacket dataPacket, int version) {
        dataPacket.writeUnsignedVarLong(dataPacket.getTargetRuntimeId());
        this.itemStackEncoder.encode(dataPacket, dataPacket.getHead(), version);
        this.itemStackEncoder.encode(dataPacket, dataPacket.getChestItem(), version);
        this.itemStackEncoder.encode(dataPacket, dataPacket.getLegsItem(), version);
        this.itemStackEncoder.encode(dataPacket, dataPacket.getFootItem(), version);
    }
}
