package com.particle.model.network.packets.data;

import com.particle.model.math.Vector2f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;

public class PlayerInputPacket extends DataPacket {

    private Vector2f moveVector;
    private boolean jumping;
    private boolean sneaking;

    public Vector2f getMoveVector() {
        return moveVector;
    }

    public void setMoveVector(Vector2f moveVector) {
        this.moveVector = moveVector;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    @Override
    public int pid() {
        return ProtocolInfo.PLAYER_INPUT_PACKET;
    }
}
