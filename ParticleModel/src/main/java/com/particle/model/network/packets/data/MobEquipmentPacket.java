package com.particle.model.network.packets.data;

import com.particle.model.item.ItemStack;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class MobEquipmentPacket extends DataPacket {

    private long targetRuntimeId;

    private ItemStack itemStack;

    private byte slot;

    private byte selectedSlot;

    private byte containedId;

    @Override
    public int pid() {
        return ProtocolInfo.MOB_EQUIPMENT_PACKET;
    }

    public long getTargetRuntimeId() {
        return targetRuntimeId;
    }

    public void setTargetRuntimeId(long targetRuntimeId) {
        this.targetRuntimeId = targetRuntimeId;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public byte getSlot() {
        return slot;
    }

    public void setSlot(byte slot) {
        this.slot = slot;
    }

    public byte getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(byte selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public byte getContainedId() {
        return containedId;
    }

    public void setContainedId(byte containedId) {
        this.containedId = containedId;
    }

    @Override
    public String toString() {
        return "MobEquipmentPacket{" +
                "targetRuntimeId=" + targetRuntimeId +
                ", itemStack=" + itemStack +
                ", slot=" + slot +
                ", selectedSlot=" + selectedSlot +
                ", containedId=" + containedId +
                '}';
    }
}
