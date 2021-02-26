package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class CompletedUsingItemPacket extends DataPacket {

    private short itemId;
    private int itemUseMethod;

    @Override
    public int pid() {
        return ProtocolInfo.COMPLETED_USING_ITEM_PACKET;
    }

    public short getItemId() {
        return itemId;
    }

    public void setItemId(short itemId) {
        this.itemId = itemId;
    }

    public int getItemUseMethod() {
        return itemUseMethod;
    }

    public void setItemUseMethod(int itemUseMethod) {
        this.itemUseMethod = itemUseMethod;
    }
}
