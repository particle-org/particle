package com.particle.model.network.packets.data;

import com.particle.model.math.Vector3;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.player.action.PlayerActionsType;

public class PlayerActionPacket extends DataPacket {
    private long entityID;
    private PlayerActionsType action;
    private Vector3 position;
    private int face;

    @Override
    public int pid() {
        return ProtocolInfo.PLAYER_ACTION_PACKET;
    }

    public long getEntityID() {
        return entityID;
    }

    public void setEntityID(long entityID) {
        this.entityID = entityID;
    }

    public PlayerActionsType getAction() {
        return action;
    }

    public void setAction(PlayerActionsType action) {
        this.action = action;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    @Override
    public String toString() {
        return "PlayerActionPacket{" +
                "entityID=" + entityID +
                ", action=" + action +
                ", position=" + position +
                ", face=" + face +
                '}';
    }
}
