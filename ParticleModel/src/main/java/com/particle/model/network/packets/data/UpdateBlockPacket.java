package com.particle.model.network.packets.data;

import com.particle.model.block.Block;
import com.particle.model.math.Vector3;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class UpdateBlockPacket extends DataPacket {

    public static final int Neighbors = 1;
    public static final int Clients = (1 << 1);
    public static final int Invisible = (1 << 2);
    public static final int ItemData = (1 << 4);
    public static final int All = (Neighbors | Clients);
    public static final int AllAndData = All | ItemData;
    public static final int None = Invisible;

    public static final int DATA_LAYER_NORMAL = 0;
    public static final int DATA_LAYER_LIQUID = 1;

    private Vector3 vector3;
    private int runtimeId;
    private int flag;
    private int layer;

    // 區分 runtimeId 用
    private Block block;

    @Override
    public int pid() {
        return ProtocolInfo.UPDATE_BLOCK_PACKET;
    }

    public Vector3 getVector3() {
        return vector3;
    }

    public void setVector3(Vector3 vector3) {
        this.vector3 = vector3;
    }

    public int getRuntimeId() {
        return runtimeId;
    }

    public void setRuntimeId(int runtimeId) {
        this.runtimeId = runtimeId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
