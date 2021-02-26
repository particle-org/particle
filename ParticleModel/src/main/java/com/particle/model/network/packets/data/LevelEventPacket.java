package com.particle.model.network.packets.data;

import com.particle.model.block.Block;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class LevelEventPacket extends DataPacket {

    private Vector3f position;
    private int data = 0;

    /**
     * 因为枚举没有包含全，所以用int表示
     */
    private int eventType;


    // 區分 runtimeId 用
    private Block block;

    @Override
    public int pid() {
        return ProtocolInfo.LEVEL_EVENT_PACKET;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
