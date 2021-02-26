package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class PlayerHotbarPacket extends DataPacket {

    private int selectedSlot;

    private int containerId;

    private boolean shouldSelectSlot;

    @Override
    public int pid() {
        return ProtocolInfo.PLAYER_HOTBAR_PACKET;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public boolean isShouldSelectSlot() {
        return shouldSelectSlot;
    }

    public void setShouldSelectSlot(boolean shouldSelectSlot) {
        this.shouldSelectSlot = shouldSelectSlot;
    }

    @Override
    public String toString() {
        return "PlayerHotbarPacket{" +
                "selectedSlot=" + selectedSlot +
                ", containerId=" + containerId +
                ", shouldSelectSlot=" + shouldSelectSlot +
                '}';
    }
}
