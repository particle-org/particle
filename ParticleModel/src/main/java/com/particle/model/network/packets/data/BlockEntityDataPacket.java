package com.particle.model.network.packets.data;

import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class BlockEntityDataPacket extends DataPacket {

    private int x;
    private int y;
    private int z;
    private NBTTagCompound nbtTagCompound;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public NBTTagCompound getNbtTagCompound() {
        return nbtTagCompound;
    }

    public void setNbtTagCompound(NBTTagCompound nbtTagCompound) {
        this.nbtTagCompound = nbtTagCompound;
    }

    @Override
    public int pid() {
        return ProtocolInfo.BLOCK_ENTITY_DATA_PACKET;
    }
}
