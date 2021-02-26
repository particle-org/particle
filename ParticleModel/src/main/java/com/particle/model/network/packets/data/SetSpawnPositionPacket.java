package com.particle.model.network.packets.data;

import com.particle.model.math.Vector3;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class SetSpawnPositionPacket extends DataPacket {
    private int spawnType;
    private Vector3 blockVector3;
    private int dimension = 0;

    @Override
    public int pid() {
        return ProtocolInfo.SET_SPAWN_POSITION_PACKET;
    }

    public int getSpawnType() {
        return spawnType;
    }

    public void setSpawnType(int spawnType) {
        this.spawnType = spawnType;
    }

    public Vector3 getBlockVector3() {
        return blockVector3;
    }

    public void setBlockVector3(Vector3 blockVector3) {
        this.blockVector3 = blockVector3;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
